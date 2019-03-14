package com.gft.reactivetest.employee;

import com.gft.reactivetest.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Component
public class EmployeeHandler {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeHandler(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public Mono<ServerResponse> getNonBlocking(ServerRequest request) {
        long employeeId = parseId(request);
        return employeeService
                .getByIdNonBlocking(employeeId)
                .flatMap(employee -> ok().syncBody(employee))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> getBlocking(ServerRequest request) {
        long employeeId = parseId(request);
        return employeeService
                .getByIdBlocking(employeeId)
                .flatMap(employee -> ok().syncBody(employee))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        Flux<Employee> employees = employeeService.findAll();
        return ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(employees, Employee.class);
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return employeeService
                .save(request.bodyToMono(Employee.class))
                .flatMap(employee -> created(URI.create("/employee/nonblocking/" + employee.getId())).syncBody(employee));
    }

    private long parseId(ServerRequest request) {
        String employeeIdStr = request.pathVariable("id");
        return Long.parseLong(employeeIdStr);
    }
}
