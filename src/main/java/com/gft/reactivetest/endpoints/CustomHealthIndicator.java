package com.gft.reactivetest.endpoints;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

// See https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html#production-ready-health

@Component("custom_health_indicator")
public class CustomHealthIndicator implements ReactiveHealthIndicator {

    @Override
    public Mono<Health> health() {
        return doHealthCheck()
                .onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build()));
    }

    private Mono<Health> doHealthCheck() {
        //perform some specific health check
        return Mono.just(new Health.Builder()
                .up()
                .withDetail("property1", "OK")
                .withDetail("property2", 1234)
                .build());
    }
}
