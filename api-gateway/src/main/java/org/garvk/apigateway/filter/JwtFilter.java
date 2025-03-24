package org.garvk.apigateway.filter;

import org.garvk.apigateway.config.JwtConfig;
import org.garvk.apigateway.endpoints.AuthServiceEndpoints;
import org.garvk.apigateway.validations.RouteValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class JwtFilter extends AbstractGatewayFilterFactory<JwtConfig> {

    @Autowired
    RouteValidator routeValidator;

    @Autowired
    WebClient.Builder webClientBuilder;

    public JwtFilter() {
        super(JwtConfig.class);
    }


    @Override
    public GatewayFilter apply(JwtConfig config) {

        return (((exchange, chain) -> {

            if(routeValidator.isSecured.test(exchange.getRequest())){

                if(!exchange.getRequest().getHeaders().containsKey("Authorization")){
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().writeWith(Mono.just(
                            exchange.getResponse().bufferFactory().wrap(
                                    "{\"message\":\"Missing auth header\"}".getBytes()
                            )
                    ));
                }

                String authHeader = exchange.getRequest().getHeaders().get("Authorization").get(0);

                if(null == authHeader){
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().writeWith(Mono.just(
                            exchange.getResponse().bufferFactory().wrap(
                                    "{\"message\":\"Invalid auth header\"}".getBytes()
                            )));
                }

                return webClientBuilder.build()
                        .get()
                        .uri(AuthServiceEndpoints.TOKEN_VALIDATE_URI)
                        .header("Authorization", authHeader)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .flatMap(isValid -> {
                            if (Boolean.TRUE.equals(isValid)) {
                                return chain.filter(exchange);
                            } else {
                                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                return exchange.getResponse().writeWith(Mono.just(
                                        exchange.getResponse().bufferFactory().wrap(
                                                "{\"message\":\"Invalid token\"}".getBytes()
                                        )
                                ));
                            }
                        })
                        .onErrorResume(e -> {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().writeWith(Mono.just(
                                    exchange.getResponse().bufferFactory().wrap(
                                            ("{\"message\":\"Auth service error\"} => " + e.getMessage()).getBytes()
                                    )
                            ));
                        });

            }

            return chain.filter(exchange);

        }));

    }

}
