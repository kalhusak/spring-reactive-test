package com.gft.reactivetest.employee.service;

import com.gft.reactivetest.employee.Employee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {

    /**
     * Get employee immediately using origin thread
     * @param id employee id
     * @return Mono
     */
    Mono<Employee> getByIdBlocking(Long id);

    /**
     * Defer employee fetching and use another thread for it
     * @param id employee id
     * @return
     */
    Mono<Employee> getByIdNonBlocking(Long id);

    Flux<Employee> findAll();

    Mono<Employee> save(Mono<Employee> employee);
}
