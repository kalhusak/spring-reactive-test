package com.gft.reactivetest.accesslog;

import brave.Tracer;
import com.gft.reactivetest.security.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


/**
 * Access logger executed in spring filter chain.
 */

@Log4j2
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)  // It has to be first filter in chain
public class AccessLogFilter implements WebFilter {

    private Tracer tracer;

    public AccessLogFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final AccessLog accessLog = new AccessLog(System.currentTimeMillis());
        exchange.getResponse().beforeCommit(
                () -> ReactiveSecurityContextHolder
                        .getContext()
                        .doOnSuccessOrError((sc, e) -> logRequest(accessLog, sc, exchange.getRequest(), exchange.getResponse()))
                        .then()
        );
        return chain.filter(exchange);
    }

    private void logRequest(AccessLog accessLog, SecurityContext securityContext, ServerHttpRequest request, ServerHttpResponse response) {
        accessLog
                .remoteAddress(request.getRemoteAddress())
                .status(response.getStatusCode())
                .method(request.getMethodValue())
                .uri(request.getURI())
                .user(securityContext != null ? (User) securityContext.getAuthentication().getPrincipal() : null)
                .traceId(tracer.currentSpan().context().traceIdString())
                .log();
    }
}
