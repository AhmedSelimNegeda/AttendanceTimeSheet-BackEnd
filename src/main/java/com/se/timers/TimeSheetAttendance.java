package com.se.timers;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.se.model.Employee;
import com.se.model.EmployeeAttendance;
import com.se.model.EmployeeAttendanceFiles;
import com.se.repository.EmployeeAttendanceFilesRepository;
import com.se.repository.EmployeeAttendanceRepository;
import com.se.repository.EmployeeRepository;

@Service
public class TimeSheetAttendance {

	@PersistenceContext
	private EntityManager em;
	@Autowired
	EmployeeAttendanceFilesRepository employeeAttendanceFilesRepository;
	@Autowired
	EmployeeAttendanceRepository employeeAttendanceRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private SimpleJdbcCall simpleJdbcCall;

	@Scheduled(fixedRate = 100000)
	public void insertAttedanceFiles() {
		System.out.println("schedule is running");
		String sourcePath = "D:/Attend/logs";
		String destinationPath = "D:/Attend/logs/backup";

		String department = "Software Development 3";
		try {
			readNewFiles(sourcePath, destinationPath, department);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String secondsToString(TimeUnit greatestUnit, long sourceDuration, TimeUnit sourceUnit) {

		int ordinal = greatestUnit.ordinal();
		if (ordinal <= sourceUnit.ordinal())
			return String.format("%02d", sourceDuration);

		final long greatestDuration = greatestUnit.convert(sourceDuration, sourceUnit);
		final long rest = sourceDuration - sourceUnit.convert(greatestDuration, greatestUnit);

		return String.format("%02d:", greatestDuration)
				+ secondsToString(TimeUnit.values()[--ordinal], rest, sourceUnit);
	}

	public void extractUserByDepart(String departmentId, File filePath, Long fileId) throws Exception {
		FileInputStream fis = new FileInputStream(filePath);
		Workbook wb = new XSSFWorkbook(fis);

		int sheetIdx = wb.getSheetIndex("Net Attendance");
		Sheet sheet = wb.getSheetAt(sheetIdx);

		for (int i = 0; i <= sheet.getLastRowNum(); i++) {

			Row row = sheet.getRow(i);

			Cell depCellId = row.getCell(4);

			System.out.println(depCellId.getStringCellValue());

			if (depCellId != null && validateCell(depCellId) != null)
				if (validateCell(depCellId).trim().equalsIgnoreCase(departmentId)) {

					String localEmpNum = validateCell(row.getCell(0));
					String globalId = validateCell(row.getCell(1));
					String pranch = validateCell(row.getCell(2));
					String name = validateCell(row.getCell(3));
					String department = validateCell(row.getCell(4));
					String position = validateCell(row.getCell(5));
					Date todayDate = validateCellDate(row.getCell(6));
					String timeIn = validateCell(row.getCell(7));
					String timeOut = validateCell(row.getCell(8));
					String netHours = validateCell(row.getCell(9)).replaceAll(" ", "");
					String totalOut = validateCell(row.getCell(10)).replaceAll(" ", "");
					String totalHours = validateCell(row.getCell(11)).replaceAll(" ", "");
					String totalWorkingHours = validateCell(row.getCell(12));
					String variance1 = validateCell(row.getCell(13));

//					 LocalTime time = LocalTime.parse(validateString(netHours));

					employeeAttendanceRepository.save(new EmployeeAttendance(Long.parseLong(localEmpNum), globalId,
							pranch, name, department, position, todayDate, timeIn, timeOut, netHours, totalOut,
							totalHours, totalWorkingHours, variance1, 0L, fileId));

					// all.put(cellDate, time);
					// System.out.println(cellDate + "\t" + time);
				}

		}

	}

	private static String validateString(String splitString) {

		String res = "";

		if (splitString.endsWith("xlsx")) {

			String[] splitStr = splitString.trim().split(".xlsx");

			for (int i = 0; i < splitStr.length; i++) {
				String string = splitStr[i];

				res = res + string;

			}
//			return res;
		} else {
			String[] hrMiSs = splitString.trim().replaceAll(" ", "").split(":");

			for (int i = 0; i < hrMiSs.length; i++) {
				String string = hrMiSs[i];

				if (string.length() < 2) {
					string = "0" + string;
				}

				res = res + string + ":";

			}

			res = res.substring(0, res.length() - 1);
		}
		return res;
	}

	private static String validateCell(Cell cell) {
		String val = "";
		if (cell != null) {

			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				// val = ((Double) cell.getNumericCellValue()).toString();
				try {
					DecimalFormat numberFormat = new DecimalFormat("#######################.######################");
					val = numberFormat.format(cell.getNumericCellValue());

					if (val.endsWith(".0")) {
						val = val.substring(0, val.indexOf(".0"));
					}

				} catch (Exception e) {

				}
				return val;
			} else {
				/*
				 * String tst = cell.getRichStringCellValue().toString().trim(); if(tst == null
				 * || tst.isEmpty()) System.out.println( "NULL STRING"); else
				 * System.out.println(tst);
				 */
				return cell.getRichStringCellValue().getString().trim();
			}
		} else {

			return "";
		}
	}

	private static Date validateCellDate(Cell cell) {
		Date dateValue = null;
		try {
			if (cell != null) {
				if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
					double val = cell.getNumericCellValue();
					if (val == 0.0)
						return null;
					dateValue = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(val);
					return dateValue;
				}
				return null;
			} else
				return null;
		} catch (Exception ex) {
			// ex.printStackTrace();
			return null;
		}
	}

	private static String validateTime(String time) {

		String res = "";
		String[] hrMiSs = time.trim().replaceAll(" ", "").split(":");

		for (int i = 0; i < hrMiSs.length; i++) {
			String string = hrMiSs[i];

			if (string.length() < 2) {
				string = "0" + string;
			}

			res = res + string + ":";

		}

		return res.substring(0, res.length() - 1);
	}

	@Transactional
	public void readNewFiles(String sourcePath, String destinationPath, String department) throws Exception {

		List<Path> files = Files.walk(Paths.get(sourcePath)).filter(s -> s.toString().toLowerCase().endsWith(".xlsx"))
				.collect(Collectors.toList());

//		files.forEach(latestModifiedFile ->  

		for (int i = 0; i < files.size(); i++) {

			Path latestModifiedFile = files.get(i);

//			Path latestModifiedFile = Files.walk(Paths.get(sourcePath))
//					.filter(s -> s.toString().toLowerCase().endsWith(".xlsx"))
//					.sorted((f1, f2) -> -(int) (f1.toFile().lastModified() - f2.toFile().lastModified())).skip(0)
//					.findFirst().orElse(null);

			System.out.println(latestModifiedFile.toString());
			if (!latestModifiedFile.toString().startsWith("D:\\Attend\\logs\\backup")) {
				EmployeeAttendanceFiles employeeAttendanceFiles = employeeAttendanceFilesRepository
						.getByFileName(latestModifiedFile.toString());

				if (employeeAttendanceFiles == null) {
					Path dest = Paths.get(destinationPath + "/" + latestModifiedFile.getFileName().toString());
					if (!dest.toFile().exists())
						Files.copy(latestModifiedFile, dest);

					employeeAttendanceFiles = employeeAttendanceFilesRepository
							.save(new EmployeeAttendanceFiles(latestModifiedFile.toString(), 0l, 0l));

				}

				if (employeeAttendanceFiles.getImportFlag() == 0) {
					employeeAttendanceRepository.deleteByFileId(employeeAttendanceFiles.getId());
					extractUserByDepart(department, new File(employeeAttendanceFiles.getFileName()),
							employeeAttendanceFiles.getId());
					employeeAttendanceFiles.setImportFlag(1L);
					employeeAttendanceFilesRepository.save(employeeAttendanceFiles);

				}

				if (employeeAttendanceFiles.getNotificationFlag() == 0) {

					List<String> gIds = employeeAttendanceRepository.getUniqeGlobalId(employeeAttendanceFiles.getId());

					for (int j = 0; j < gIds.size(); j++) {

						List<EmployeeAttendance> list = employeeAttendanceRepository
								.getByFileIdAndGlobalId(employeeAttendanceFiles.getId(), gIds.get(j));

						int totalHoursCalaulated = 8;

						if (gIds.get(j) == "A61245") {
							totalHoursCalaulated = 7;
						}

						int empAllSeconds = list.stream().sequential().mapToInt(e -> {
							System.out.println(e.getGlobalId() + "\t" + e.getTodayDate() + "\t" + e.getNetHours());
							LocalTime l = LocalTime.parse(validateTime(e.getNetHours()));
							return l.getHour() * 60 * 60 + l.getMinute() * 60 + l.getSecond();
						}).sum();

						int differentSeconds = ((list.size()) * totalHoursCalaulated * 60 * 60) - empAllSeconds;
						htmlDesign(daysToHours(secondsToString(TimeUnit.DAYS, differentSeconds, TimeUnit.SECONDS)),
								daysToHours(secondsToString(TimeUnit.DAYS, empAllSeconds, TimeUnit.SECONDS)), list);
//					System.out.println(gIds.get(j) + "\t" + empAllSeconds + "\t"
//							+ secondsToString(TimeUnit.DAYS, differentSeconds, TimeUnit.SECONDS));

						System.out.println(empAllSeconds + " Total Employee Net hours");

					}

					employeeAttendanceFiles.setNotificationFlag(1L);
					employeeAttendanceFilesRepository.save(employeeAttendanceFiles);

				}
			} else {
				continue;
			}
		}
	}

	private String daysToHours(String cc) {
		String[] x = cc.split(":");

		Long days = Long.parseLong(x[0]);

		if (days > 0)
			x[0] = (days * 24) + "";

		String res = "";
		for (int i = 0; i < x.length; i++) {

			res += ":" + x[i];
		}

		return res.substring(1, res.length());

	}

	public void htmlDesign(String netHours, String totalNet, List<EmployeeAttendance> list) {
		String htmlTr1 = "<head><style>#customers {  font-family: \"Trebuchet MS\", Arial, Helvetica, sans-serif;  border-collapse: collapse;  width: 100%;}#customers td, #customers th {  border: 1px solid #ddd;  padding: 8px;}#customers tr:nth-child(even){background-color: #f2f2f2;}#customers tr:hover {background-color: #ddd;}#customers th {  padding-top: 12px;  padding-bottom: 12px;  text-align: left;  background-color: #4CAF50;  color: white;}</style></head><body><p> Hello Eng. "
				+ list.get(0).getName()
				+ " , </p><p> Please review below missed hours report. </p><table id=\"customers\">  <tr>    <th>Global ID</th>    <th>Date</th>    <th>Total Hours</th>    <th>Net Hours</th>  </tr>\r\n";

		String rows = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		for (int i = 0; i < list.size(); i++) {
			EmployeeAttendance array_element = list.get(i);

			System.out.println(dateFormat.format(array_element.getTodayDate()) );
			rows += "  <tr>     <td>" + array_element.getGlobalId() + "</td>     <td>"
					+ dateFormat.format(array_element.getTodayDate()) + "</td>     <td>" + array_element.getTotalHours()
					+ "</td>      <td>" + array_element.getNetHours() + "</td>   </tr>";
		}

/*		String tblFooter = "<tfoot>     <tr>     <td style=\"border:none\"></td>  <td style=\"border:none\"></td>      <td><b>Total Net Hours</b></td>       <td><b>"
				+ totalNet
				+ "</b></td>          </tr>    <tr>     <td style=\"border:none\"></td>  <td style=\"border:none\"></td>      <td><b>Total Missed Hours</b></td>       <td><b>"
				+ netHours + "</b></td>          </tr>   </tfoot> </table>";
*/
		
		String totalMisedHours = "</table> <p> <table>\r\n" + 
				"  <tr>\r\n" + 
				"    <th>Total missed Hours</th>\r\n" + 
				"    <th style=\"color: red;\"><p>"+netHours+"</p></th>\r\n" + 
				"  </tr>\r\n" + 
				"  \r\n" + 
				"</table>";
		String htmlTr5 = "<p> Best Regards</p>  <p>-------------------------------------</p> <p> Ahmed Selim</p> <p> SiliconExpert SW Developer</p><p><a href=\"\">Ahmed_Selim@siliconexpert.com </a> </p>  </body> ";

//		System.out.println("<!DOCTYPE html><html>" + htmlTr1 + rows + tblFooter + htmlTr5 + "</html>");
		
//		System.out.println("<!DOCTYPE html><html>" + htmlTr1 + rows + totalMisedHours + htmlTr5 + "</html>");
//		String htmlBody = htmlTr1 + rows + tblFooter + htmlTr5;
		String htmlBody = htmlTr1 + rows + totalMisedHours + htmlTr5;
		
		Employee emp = employeeRepository.getByEmployeeName(list.get(0).getName());

		if (emp != null) {

			Employee teamLeader = employeeRepository.findById(emp.getTlId()).orElse(null);

//			String query = "CALL AUTOMATION2.HTML_EMAIL(  '" + emp.getEmail() + "','" + teamLeader.getEmail()
//					+ ";dareen@siliconexpert.com' ,'Attendace Time  Sheet <Ba-SW@siliconexpert.com>',   'Attendace Time  Sheet '  ,    LENGTH( '"
//					+ htmlBody + "') + 10 ,     '" + htmlBody + "'  ) ";
//		
//			System.out.println(query);
			
			employeeAttendanceRepository.callProcedure(emp.getEmail() , teamLeader.getEmail() + "; dareen@siliconexpert.com " , htmlBody);

			System.out.println("Missed Email Was Sent Well .......");
		} else {
			System.out.println("Emploee Not Have Email in Employee Table  " + list.get(0).getName());

		}
//		jdbcTemplate.setDataSource((DataSource) em.getEntityManagerFactory().getProperties()
//				.get("javax.persistence.nonJtaDataSource"));
//		
//		jdbcTemplate.execute("CALL AUTOMATION2.HTML_EMAIL(  '"+emp.getEmail()   + "','ahmed_selim@siliconexpert.com' ,'Attendace Time  Sheet <Ba-SW@siliconexpert.com>',   'Attendace Time  Sheet '  ,    LENGTH( '"+htmlBody+"') + 10 ,     '"+htmlBody+"'  ) ");

//		AUTOMATION2.HTML_EMAIL(  emp.getEmail()   ,'@siliconexpert.com' ,'Attendace Time  Sheet <Ba-SW@siliconexpert.com>',   'Attendace Time  Sheet '  ,    LENGTH( htmlBody) + 10 ,     htmlBody  ) ;

	}

	public static void main(String[] args) throws Exception {
//		String sourcePath = "D:/Attend/logs";
//		String destinationPath = "D:/Attend/logs/backup";
//		String department = "";
//		TimeSheetAttendance obj = new TimeSheetAttendance();
//		obj.readNewFiles(sourcePath, destinationPath, department);
//		htmlDesign("", "", new Date(), "", "");

		String cc = "01:06:25:24";

		String[] x = cc.split(":");

		Long days = Long.parseLong(x[0]);

		if (days > 0)
			x[0] = (days * 24) + "";

		String res = "";
		for (int i = 0; i < x.length; i++) {

			res += ":" + x[i];
		}
		System.out.println(res.substring(1, res.length()));

	}

}
