package com.gft.reactivetest.employee.repository;

import com.gft.reactivetest.employee.Employee;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class EmployeeInMemRepository implements EmployeeRepository {

    private Map<Long, Employee> db;
    private AtomicLong idSequence;

    public EmployeeInMemRepository() {
        db = new HashMap<>();
        db.put(0L, new Employee(0L, "John"));
        db.put(1L, new Employee(1L, "Paul"));
        db.put(2L, new Employee(2L, "Smith"));
        db.put(3L, new Employee(3L, "Bob"));
        idSequence = new AtomicLong(4);
    }

    @Override
    public Optional<Employee> findById(Long id) {
        simulateExecution(500);
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public List<Employee> findAll() {
        simulateExecution(200);
        return new ArrayList<>(db.values());
    }

    public Employee save(Employee employee) {
        simulateExecution(700);
        if (employee.getId() == null)
            employee.setId(idSequence.getAndIncrement());
        db.put(employee.getId(), employee);
        return employee;
    }

    private void simulateExecution(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
