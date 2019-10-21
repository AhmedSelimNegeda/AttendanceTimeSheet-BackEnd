package com.se.controller;

import com.se.exception.ResourceNotFoundException;
import com.se.model.EmployeeAttendance;
import com.se.repository.EmployeeAttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by rajeevkumarsingh on 27/06/17.
 */

@CrossOrigin
@RestController
@RequestMapping("/employeeAttendanceApi")
public class EmployeeAttendanceController {

    @Autowired
    EmployeeAttendanceRepository employeeAttendanceRepository;

    @GetMapping("/all")
    public List<EmployeeAttendance> getAllEmployeeAttendances() {
        return employeeAttendanceRepository.findAll();
    }

    @PostMapping("/create")
    public EmployeeAttendance createOrUpdateEmployeeAttendance(@Valid @RequestBody EmployeeAttendance employeeAttendance) {
        return employeeAttendanceRepository.save(employeeAttendance);
    }

    @GetMapping("/getById/{id}")
    public EmployeeAttendance getEmployeeAttendanceById(@PathVariable(value = "id") Long employeeAttendanceId) {
        return employeeAttendanceRepository.findById(employeeAttendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("EmployeeAttendance", "id", employeeAttendanceId));
    }

   

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteEmployeeAttendance(@PathVariable(value = "id") Long employeeAttendanceId) {
        EmployeeAttendance employeeAttendance = employeeAttendanceRepository.findById(employeeAttendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("EmployeeAttendance", "id", employeeAttendanceId));

        employeeAttendanceRepository.delete(employeeAttendance);

        return ResponseEntity.ok().build();
    }
}
