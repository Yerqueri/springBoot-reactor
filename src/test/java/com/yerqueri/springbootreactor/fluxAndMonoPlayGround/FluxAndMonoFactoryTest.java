package com.yerqueri.springbootreactor.fluxAndMonoPlayGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactoryTest {

    List<String> names = Arrays.asList("Adam","Anna","Jack","Jenny");

    @Test
    public void fluxUsingIterable(){
        Flux<String> flux = Flux.fromIterable(names);
        StepVerifier.create(flux.log())
                .expectNext("Adam","Anna","Jack","Jenny")
                .verifyComplete();
    }

    @Test
    public void FluxUsingArray(){
        String[] arr = new String[]{"Adam","Anna","Jack","Jenny"};
        Flux<String> flux = Flux.fromArray(arr);
        StepVerifier.create(flux.log())
                .expectNext("Adam","Anna","Jack","Jenny")
                .verifyComplete();
    }

    @Test
    public void FluxUsingStream(){
        Flux<String> flux = Flux.fromStream(names.stream());
        StepVerifier.create(flux.log())
                .expectNext("Adam","Anna","Jack","Jenny")
                .verifyComplete();
    }

    @Test
    public void MonoUsingJustOrEmpty(){
        Mono<String> mono = Mono.justOrEmpty(null);
        StepVerifier.create(mono.log())
                .verifyComplete();
    }

    @Test
    public void MonoUsingSupplier(){

        Supplier<String> stringSupplier = ()-> "adam";

        Mono<String> mono = Mono.fromSupplier(stringSupplier);
        StepVerifier.create(mono.log())
                .expectNext("adam")
                .verifyComplete();
    }

    @Test
    public void fluxUsingRange(){
        StepVerifier.create(Flux.range(1,100).log())
                .expectNextCount(100)
                .verifyComplete();
    }

}
