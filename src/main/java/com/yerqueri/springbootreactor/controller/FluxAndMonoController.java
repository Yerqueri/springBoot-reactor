package com.yerqueri.springbootreactor.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class FluxAndMonoController {

    @GetMapping("/flux")
    public Flux<Integer> getFlux(){
        return Flux.range(1,10)
                .delayElements(Duration.ofMillis(200))
                .log();
    }

    @GetMapping(value = "/fluxStream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Integer> getFluxStream(){
        return Flux.interval(Duration.ofMillis(200))
                .map(Long::intValue)
                .log();
    }

    @GetMapping(value = "/mono", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Integer> getMono(){
        return Mono.justOrEmpty(1)
                .log();
    }
}
