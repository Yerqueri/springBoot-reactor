package com.yerqueri.springbootreactor.fluxAndMonoPlayGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class VirtualTimeTest {

    @Test
    public void testing_without_virtualTime(){
        Flux<Long> flux = Flux.interval(Duration.ofSeconds(1))
                .take(3);

        StepVerifier.create(flux.log())
                .expectNext(0l,1l,2l)
                .verifyComplete();
    }

    @Test
    public void testing_with_virtualTime(){
        VirtualTimeScheduler.getOrSet();
        Flux<Long> flux = Flux.interval(Duration.ofSeconds(1))
                .take(3);

        StepVerifier.withVirtualTime(()->flux.log())
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(3))
                .expectNext(0l,1l,2l)
                .verifyComplete();
    }
}
