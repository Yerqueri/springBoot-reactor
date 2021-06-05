package com.yerqueri.springbootreactor.fluxAndMonoPlayGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoCombineTest {

    @Test
    public void combineUsingMerge(){
        Flux<String> flux = Flux.just("A","B","C");
        Flux<String> flux2 = Flux.just("D","E","F");
        Flux<String> merge = Flux.merge(flux,flux2);
        StepVerifier.create(merge.log())
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_delay(){
        Flux<String> flux = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));
        Flux<String> merge = Flux.merge(flux,flux2);
        StepVerifier.create(merge.log())
                .expectNextCount(6)
                //.expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_concat(){
        Flux<String> flux = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D","E","F");
        Flux<String> merge = Flux.concat(flux,flux2);
        StepVerifier.create(merge.log())
                //.expectNextCount(6)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    public void combine_zip(){
        Flux<String> flux = Flux.just("A","B","C");
        Flux<String> flux2 = Flux.just("D","E","F");
        Flux<String> merge = Flux.zip(flux,flux2,(elementFlux1,elementFlux2)->elementFlux2.concat(elementFlux1));
        StepVerifier.create(merge.log())
                //.expectNextCount(6)
                .expectNext("DA","EB","FC")
                .verifyComplete();
    }
}
