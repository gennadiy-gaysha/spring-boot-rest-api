package com.rest.api.crud.controller;

import com.rest.api.crud.entity.Employee;
import com.rest.api.crud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller LAYER
@RestController
@RequestMapping("/api")
public class EmployeeRESTController {
    private final EmployeeService employeeService;

    @Autowired // one constructor - annotation is not obligatory
    public EmployeeRESTController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Expose "/employees" and return a list of employees
    @GetMapping("/employees")
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    // Get employee by their id
    // Add mapping for GET /employees/{employeeId}
    @GetMapping("/employees/{employeeId}")
    public Employee findById(@PathVariable int employeeId) {
        Employee theEmployee = employeeService.findById(employeeId);

        if(theEmployee == null){
            throw new RuntimeException("Employee id is not found - " + employeeId);
        }

        return theEmployee;
    }

    // Create new employee
    // add mapping for POST /employees - create new employee
    @PostMapping("/employees")
    // Tells Spring to convert the incoming JSON request body into an Employee object.
    // This allows the method to accept employee data from the client.
    Employee addEmployee(@RequestBody Employee theEmployee) {
        // This ensures that a new employee is created instead of updating an existing one.
        //In databases with auto-generated primary keys, setting id = 0 signals that this
        // is a new record.
        theEmployee.setId(0);

        return employeeService.save(theEmployee);

    }

    // add mapping for PUT /employees - update existing employee
    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee theEmployee) {

        return employeeService.save(theEmployee);
    }

    // add mapping fot DELETE /employees/{employeeId} - delete existing employee
    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable int employeeId) {
        Employee tempEmployee = employeeService.findById(employeeId);

        if (tempEmployee == null) {
            throw new RuntimeException("Employee id not found - " + employeeId);
        }

        employeeService.deleteById(employeeId);

        return "Deleted employee id - " + employeeId;
    }
}
