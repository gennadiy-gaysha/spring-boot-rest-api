package com.rest.api.crud.dao;

import com.rest.api.crud.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

// DAO LAYER - acts as the data access layer for interacting with the database
// to retrieve employee information
@Repository
public class EmployeeDAOImpl implements EmployeeDAO{
    // define field for EntityManager
    private final EntityManager entityManager;

    // set up constructor injection of the EntityManager bean
    @Autowired
    public EmployeeDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Employee> findAll() {
        // create a query
        TypedQuery<Employee> theQuery = entityManager.createQuery(
                "from Employee", Employee.class);

        // execute query and get result list
        List<Employee> employees = theQuery.getResultList();

        // return the result
        return employees;
    }
}
