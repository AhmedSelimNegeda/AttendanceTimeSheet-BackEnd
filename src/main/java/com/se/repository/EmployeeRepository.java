package com.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.se.model.Employee;

/**
 * Created by rajeevkumarsingh on 27/06/17.
 */

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	
	Employee getByEmployeeName(String empNaame);
}
