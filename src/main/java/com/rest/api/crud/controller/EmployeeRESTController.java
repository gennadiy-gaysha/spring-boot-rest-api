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
    public EmployeeRESTController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    // expose "/employees" and return a list of employees
    @GetMapping("/employees")
    public List<Employee> findAll(){
        return employeeService.findAll();
    }

    // Get employee by their id
    // add mapping for GET /employees/{employeeId}
    @GetMapping("/employees/{employeeId}")
    public Employee findById(@PathVariable int employeeId){
        return employeeService.findById(employeeId);
    }

    // Create new employee
    // add mapping for POST /employees
    @PostMapping("/employees")
    // Tells Spring to convert the incoming JSON request body into an Employee object.
    // This allows the method to accept employee data from the client.
    Employee addEmployee(@RequestBody Employee theEmployee){
        // This ensures that a new employee is created instead of updating an existing one.
        //In databases with auto-generated primary keys, setting id = 0 signals that this
        // is a new record.
        theEmployee.setId(0);
        Employee dbEmployee = employeeService.save(theEmployee);
        return dbEmployee;
    }

}
