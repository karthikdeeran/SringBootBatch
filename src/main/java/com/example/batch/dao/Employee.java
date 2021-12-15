package com.example.batch.dao;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "EMPLOYEE_TBL")
public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;

	public Employee() {
	}

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "EMPLOYEE_NAME", nullable = false)
	private String empName;

	@Column(name = "EMPLOYEE_AGE", nullable = false)
	private Integer empAge;

	public Employee(String empName, Integer empAge) {
		this.empAge = empAge;
		this.empName = empName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public Integer getEmpAge() {
		return empAge;
	}

	public void setEmpAge(Integer empAge) {
		this.empAge = empAge;
	}

}
