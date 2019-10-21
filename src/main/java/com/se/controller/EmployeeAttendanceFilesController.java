package com.se.controller;

import com.se.exception.ResourceNotFoundException;
import com.se.model.EmployeeAttendanceFiles;
import com.se.repository.EmployeeAttendanceFilesRepository;
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
@RequestMapping("/employeeAttendanceFilesApi")
public class EmployeeAttendanceFilesController {

    @Autowired
    EmployeeAttendanceFilesRepository employeeAttendanceFilesRepository;

    @GetMapping("/all")
    public List<EmployeeAttendanceFiles> getAllEmployeeAttendanceFiless() {
        return employeeAttendanceFilesRepository.findAll();
    }

    @PostMapping("/create")
    public EmployeeAttendanceFiles createOrUpdateEmployeeAttendanceFiles(@Valid @RequestBody EmployeeAttendanceFiles employeeAttendanceFiles) {
        return employeeAttendanceFilesRepository.save(employeeAttendanceFiles);
    }

    @GetMapping("/getById/{id}")
    public EmployeeAttendanceFiles getEmployeeAttendanceFilesById(@PathVariable(value = "id") Long employeeAttendanceFilesId) {
        return employeeAttendanceFilesRepository.findById(employeeAttendanceFilesId)
                .orElseThrow(() -> new ResourceNotFoundException("EmployeeAttendanceFiles", "id", employeeAttendanceFilesId));
    }

   

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteEmployeeAttendanceFiles(@PathVariable(value = "id") Long employeeAttendanceFilesId) {
        EmployeeAttendanceFiles employeeAttendanceFiles = employeeAttendanceFilesRepository.findById(employeeAttendanceFilesId)
                .orElseThrow(() -> new ResourceNotFoundException("EmployeeAttendanceFiles", "id", employeeAttendanceFilesId));

        employeeAttendanceFilesRepository.delete(employeeAttendanceFiles);

        return ResponseEntity.ok().build();
    }
}
