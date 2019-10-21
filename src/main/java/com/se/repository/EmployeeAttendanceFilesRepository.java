package com.se.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.se.model.EmployeeAttendanceFiles;

/**
 * Created by rajeevkumarsingh on 27/06/17.
 */

@Repository
public interface EmployeeAttendanceFilesRepository extends JpaRepository<EmployeeAttendanceFiles, Long> {

 
	EmployeeAttendanceFiles getByFileName(String fileName);
}
