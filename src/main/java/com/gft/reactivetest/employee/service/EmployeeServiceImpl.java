package com.gft.reactivetest.employee.service;

import brave.Span;
import brave.Tracer;
import com.gft.reactivetest.employee.Employee;
import com.gft.reactivetest.employee.repository.EmployeeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.net.URI;
import java.time.Duration;
import java.util.Optional;

@Log4j2
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private WebClient webClient;
    private Tracer tracer;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, WebClient webClient, Tracer tracer) {
        this.employeeRepository = employeeRepository;
        this.webClient = webClient;
        this.tracer = tracer;
    }

    @Override
    public Mono<Employee> getByIdBlocking(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return Mono.justOrEmpty(employee);
    }

    @Override
    public Mono<Employee> getByIdNonBlocking(Long id) {
        return Mono
                .fromCallable(() -> {
                    Span span = tracer.currentSpan();
                    span.annotate("Employee fetching");
                    Employee employee = employeeRepository.findById(id).orElse(null);
                    span.annotate("End of employee fetching");
                    return employee;
                })
                .subscribeOn(Schedulers.elastic()); // Execute using another thread
    }

    @Override
    public Flux<Employee> findAll() {
        return Flux
                .defer(() -> Flux.fromIterable(employeeRepository.findAll()))
                .delayElements(Duration.ofMillis(1000))
                .subscribeOn(Schedulers.elastic());
    }

    @Override
    public Mono<Employee> save(Mono<Employee> employee) {
        return employee
                .map(employeeRepository::save)
                .subscribeOn(Schedulers.elastic());
    }
}
