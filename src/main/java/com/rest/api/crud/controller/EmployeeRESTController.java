package com.rest.api.crud.controller;

import com.rest.api.crud.dao.EmployeeDAO;
import com.rest.api.crud.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeRESTController {
    private final EmployeeDAO employeeDAO;

    // quick DAO check: inject employee dao via constructor
    @Autowired // one constructor - annotation is not obligatory
    public EmployeeRESTController(EmployeeDAO employeeDAO){
        this.employeeDAO = employeeDAO;
    }

    // expose "/employees" and return a list of employees
    @GetMapping("/employees")
    public List<Employee> findAll(){
        return employeeDAO.findAll();
    }


}
