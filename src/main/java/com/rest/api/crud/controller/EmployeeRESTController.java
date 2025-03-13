package com.rest.api.crud.controller;

import com.rest.api.crud.entity.Employee;
import com.rest.api.crud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
