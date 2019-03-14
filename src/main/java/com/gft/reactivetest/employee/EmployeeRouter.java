package com.gft.reactivetest.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class EmployeeRouter {

    private EmployeeHandler employeeHandler;

    @Autowired
    public EmployeeRouter(EmployeeHandler employeeHandler) {
        this.employeeHandler = employeeHandler;
    }

    public RouterFunction<ServerResponse> create() {
        return route()
                .GET("/employee/nonblocking/{id}", employeeHandler::getNonBlocking)
                .GET("/employee/blocking/{id}", employeeHandler::getBlocking)
                .GET("/employee/all", employeeHandler::getAll)
                .POST("/employee", employeeHandler::save)
                .build();
    }
}
