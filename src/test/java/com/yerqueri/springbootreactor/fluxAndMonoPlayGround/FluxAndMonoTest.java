package com.yerqueri.springbootreactor.fluxAndMonoPlayGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

    @Test
    public void testFlux(){
        Flux<String> sFlux = Flux.just("A","B","C")
//                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .concatWith(Flux.just("1","2","3"))
                .log();
        sFlux.subscribe(System.out::println ,
                (error)->System.err.println(error),
                ()->System.out.println("Completed")
        );
    }

    @Test
    public void fluxTestElements_WithoutError(){
        Flux<String> flux = Flux.just("String","Spring Boot","Reactive Spring")
                .log();
        StepVerifier
                .create(flux)
                .expectNext("String")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .verifyComplete();

    }

    @Test
    public void fluxTestElements_WithError(){
        Flux<String> flux = Flux.just("String","Spring Boot","Reactive Spring")
                .log()
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")));
        StepVerifier
                .create(flux)
                //.expectNextCount(3)
                .expectNext("String")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                //.expectErrorMessage("Exception Occurred")  // only one of the two work at a time.
                .expectError(RuntimeException.class)
                .verify();

        StepVerifier
                .create(flux)
                //.expectNextCount(3)
                .expectNext("String","Spring Boot","Reactive Spring")
                //.expectErrorMessage("Exception Occurred")  // only one of the two work at a time.
                .expectError(RuntimeException.class)
                .verify();

        StepVerifier
                .create(flux)
                .expectNextCount(3)
                //.expectErrorMessage("Exception Occurred")  // only one of the two work at a time.
                .expectError(RuntimeException.class)
                .verify();

        StepVerifier
                .create(flux)
                .expectNextCount(3)
                .expectErrorMessage("Exception Occurred")  // only one of the two work at a time.
                .verify();
    }

    @Test
    public void MonoTest(){
        Mono<String> stringMono = Mono.just("Spring");

        StepVerifier.create(stringMono.log())
                .expectNext("Spring")
                .verifyComplete();
    }

    @Test
    public void MonoTest_Error(){

        StepVerifier.create(Mono.error(new RuntimeException("Exception Occurred")).log())
                .expectError(RuntimeException.class)
                .verify();
    }
}

