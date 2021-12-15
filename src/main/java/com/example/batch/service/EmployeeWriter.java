package com.example.batch.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.batch.dao.Employee;
import com.example.batch.dao.IEmployeeRepository;

@Component
public class EmployeeWriter implements ItemWriter<Employee> {
	
    private final Logger logger = LoggerFactory.getLogger(EmployeeWriter.class);
	
	@Autowired
	private IEmployeeRepository employeeRepository;

	@Override
	public void write(List<? extends Employee> items) throws Exception {
		logger.debug("#write#");
		employeeRepository.saveAll(items);
	}

}
