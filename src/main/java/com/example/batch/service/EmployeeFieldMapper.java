package com.example.batch.service;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

import com.example.batch.dao.Employee;

@Component
public class EmployeeFieldMapper implements FieldSetMapper<Employee> {
    @Override
    public Employee mapFieldSet(FieldSet fieldSet) {
        final Employee voltage = new Employee(fieldSet.readString("emp_name"),fieldSet.readInt("emp_age"));
        return voltage;
    }
}