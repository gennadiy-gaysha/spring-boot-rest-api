package com.rest.api.crud.service;

import com.rest.api.crud.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();
}
