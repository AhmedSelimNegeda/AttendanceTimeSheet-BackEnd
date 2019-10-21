package com.se.controller;

import com.se.exception.ResourceNotFoundException;
import com.se.model.Employee;
import com.se.repository.EmployeeRepository;
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
@RequestMapping("/employeeApi")
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping("/all")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @PostMapping("/create")
    public Employee createOrUpdateEmployee(@Valid @RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @GetMapping("/getById/{id}")
    public Employee getEmployeeById(@PathVariable(value = "id") Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
    }

   

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable(value = "id") Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        employeeRepository.delete(employee);

        return ResponseEntity.ok().build();
    }
}
