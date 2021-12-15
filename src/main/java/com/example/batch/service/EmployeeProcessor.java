package com.example.batch.service;

import org.springframework.batch.item.ItemProcessor;

import com.example.batch.dao.Employee;

public class EmployeeProcessor implements ItemProcessor<Employee, Employee>{
    @Override
    public Employee process(final Employee employee) {
        return new Employee(employee.getEmpName(),employee.getEmpAge());
    }
}