package com.gft.reactivetest.web;

import com.gft.reactivetest.employee.EmployeeHandler;
import com.gft.reactivetest.employee.EmployeeRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {

    @Autowired
    private EmployeeHandler employeeHandler;

    @Bean
    public RouterFunction<ServerResponse> router() {
        EmployeeRouter employeeRouter = new EmployeeRouter(employeeHandler);
        return employeeRouter.create();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }
}
