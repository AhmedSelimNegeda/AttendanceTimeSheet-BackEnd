package com.se.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "EMPLOYEE_ATTENDANCE")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public class EmployeeAttendance {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_att_id_Sequence")
	@SequenceGenerator(name = "emp_att_id_Sequence", sequenceName = "emp_att_seq")
	private Long id;
	private Long localEmpNum;
	private String globalId;
	private String pranch;
	private String name;
	private String department;
	private String position;
	@Column(nullable = false, updatable = false)
	@DateTimeFormat
	private Date todayDate;
	private String timeIn;
	private String timeOut;
	private String netHours;
	private String totalOut;
	private String totalHours;
	private String totalWorkingHours;

	public EmployeeAttendance() {

	}

	public EmployeeAttendance(Long localEmpNum, String globalId, String pranch, String name, String department,
			String position, Date todayDate, String timeIn, String timeOut, String netHours, String totalOut,
			String totalHours, String totalWorkingHours, String variance1, Long flag, Long fileId) {
		super();

		this.localEmpNum = localEmpNum;
		this.globalId = globalId;
		this.pranch = pranch;
		this.name = name;
		this.department = department;
		this.position = position;
		this.todayDate = todayDate;
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.netHours = netHours;
		this.totalOut = totalOut;
		this.totalHours = totalHours;
		this.totalWorkingHours = totalWorkingHours;
		this.variance1 = variance1;
		this.flag = flag;
		this.fileId = fileId;
	}

	private String variance1;
	private Long flag;
	private Long fileId;
	@Column(name = "STORE_DATE", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date storeDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLocalEmpNum() {
		return localEmpNum;
	}

	public void setLocalEmpNum(Long localEmpNum) {
		this.localEmpNum = localEmpNum;
	}

	public String getGlobalId() {
		return globalId;
	}

	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}

	public String getPranch() {
		return pranch;
	}

	public void setPranch(String pranch) {
		this.pranch = pranch;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Date getTodayDate() {
		return todayDate;
	}

	public void setTodayDate(Date todayDate) {
		this.todayDate = todayDate;
	}

	public String getTimeIn() {
		return timeIn;
	}

	public void setTimeIn(String timeIn) {
		this.timeIn = timeIn;
	}

	public String getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	public String getNetHours() {
		return netHours;
	}

	public void setNetHours(String netHours) {
		this.netHours = netHours;
	}

	public String getTotalOut() {
		return totalOut;
	}

	public void setTotalOut(String totalOut) {
		this.totalOut = totalOut;
	}

	public String getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(String totalHours) {
		this.totalHours = totalHours;
	}

	public String getTotalWorkingHours() {
		return totalWorkingHours;
	}

	public void setTotalWorkingHours(String totalWorkingHours) {
		this.totalWorkingHours = totalWorkingHours;
	}

	public String getVariance1() {
		return variance1;
	}

	public void setVariance1(String variance1) {
		this.variance1 = variance1;
	}

	public Long getFlag() {
		return flag;
	}

	public void setFlag(Long flag) {
		this.flag = flag;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Date getStoreDate() {
		return storeDate;
	}

	public void setStoreDate(Date storeDate) {
		this.storeDate = storeDate;
	}

}
