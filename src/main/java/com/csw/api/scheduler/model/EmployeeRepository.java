package com.csw.api.scheduler.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csw.api.scheduler.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

	public Optional<Employee> findByEmployeeId(String employeeId);
}
