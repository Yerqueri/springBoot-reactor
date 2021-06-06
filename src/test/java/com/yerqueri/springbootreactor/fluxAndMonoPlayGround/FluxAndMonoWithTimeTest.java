package com.yerqueri.springbootreactor.fluxAndMonoPlayGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoWithTimeTest {

    @Test
    public void infinite_seq() throws InterruptedException {
        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(20)).log();

        infiniteFlux.subscribe(System.out::println);

        Thread.sleep(100);
    }

    @Test
    public void infinite_seq_test(){
        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(20))
                .take(3)
                .log();

        StepVerifier.create(infiniteFlux.log())
                .expectSubscription()
                .expectNext(0L,1L,2L)
                .verifyComplete();

    }

    @Test
    public void infinite_seq_map_test(){
        Flux<Integer> infiniteFlux = Flux.interval(Duration.ofMillis(20))
                .map(Long::intValue)
                .take(3)
                .log();

        StepVerifier.create(infiniteFlux.log())
                .expectSubscription()
                .expectNext(0,1,2)
                .verifyComplete();

    }

    @Test
    public void infinite_seq_map_withDelay_test(){
        Flux<Integer> infiniteFlux = Flux.interval(Duration.ofMillis(100))
                .delayElements(Duration.ofSeconds(1))
                .map(Long::intValue)
                .take(3)
                .log();

        StepVerifier.create(infiniteFlux.log())
                .expectSubscription()
                .expectNext(0,1,2)
                .verifyComplete();

    }
}
