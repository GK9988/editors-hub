package org.garvk.apigateway.validations;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static List<String> securedApiEndpoints = List.of(
            "/api/auth/register",
            "/api/auth/validate",
            "/api/auth/login",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured = req -> {
        boolean shouldSecure = securedApiEndpoints
                .stream()
                .noneMatch(uri -> req.getURI().getPath().contains(uri));
        System.out.println("Is secured: " + shouldSecure + " for path: " + req.getURI().getPath());
        return shouldSecure;
    };
}
