package com.rest.api.crud.dao;

import com.rest.api.crud.entity.Employee;

import java.util.List;

public interface EmployeeDAO {
    List<Employee> findAll();
}
