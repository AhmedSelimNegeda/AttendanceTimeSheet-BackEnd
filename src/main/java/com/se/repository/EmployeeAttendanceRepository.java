package com.se.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.se.model.EmployeeAttendance;

/**
 * Created by rajeevkumarsingh on 27/06/17.
 */

@Repository
public interface EmployeeAttendanceRepository extends JpaRepository<EmployeeAttendance, Long> {

//	@Query(nativeQuery = true, value = "delete from EMPLOYEE_ATTENDANCE where FILE_ID= ?1")
	@Transactional
	void deleteByFileId(Long id);

	List<EmployeeAttendance> getByFileIdAndGlobalId(Long id , String  gId);

	@Query(nativeQuery = true, value = "select UNIQUE GLOBAL_ID FROM EMPLOYEE_ATTENDANCE where FILE_ID = ?1 ")
	List<String> getUniqeGlobalId(Long id);
	
	
//	'; dareen@siliconexpert.com'
	@Modifying
	@Query(nativeQuery = true, value = "CALL AUTOMATION2.HTML_EMAIL(?1 , ?2   ,'Attendace Time  Sheet <Ba-SW@siliconexpert.com>',   'Attendace Time  Sheet '  ,    LENGTH( ?3) + 10 ,     ?3  )")
	@Transactional
	void callProcedure(String email , String leaderEmail , String body);
	
}
