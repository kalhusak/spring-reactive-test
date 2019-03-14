package com.gft.reactivetest.employee.repository;

import com.gft.reactivetest.employee.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {

    Optional<Employee> findById(Long id);

    List<Employee> findAll();

    Employee save(Employee employee);
}
