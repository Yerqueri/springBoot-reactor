package com.yerqueri.springbootreactor.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class HandlerFunction {

    public Mono<ServerResponse> flux(ServerRequest serverRequest){
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(
                        Flux.range(1,4).delayElements(Duration.ofSeconds(2)).log()
                        ,Integer.class
                );
    }

    public Mono<ServerResponse> mono(ServerRequest serverRequest){
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(
                        Mono.justOrEmpty(1).log()
                        ,Integer.class
                );
    }
}
