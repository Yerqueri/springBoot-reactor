package com.yerqueri.springbootreactor.fluxAndMonoPlayGround;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
public class ColdAndHotPublisherTest {

    @Test //This example is of Cold Publisher
    public void coldPublisherTest() throws InterruptedException {
        Flux<String> flux = Flux.just("A","B","C","D","E","F")
                .delayElements(Duration.ofSeconds(1));

        flux.subscribe(el ->log.info("Subscriber 1: {}",el)); // emits the value from beginning just like http request
        Thread.sleep(2000);
        flux.subscribe(el ->log.info("Subscriber 2: {}",el));// emits the value from beginning just like http request

        Thread.sleep(4000);
    }

    @Test //This example is of Cold Publisher
    public void hotPublisherTest() throws InterruptedException {
        Flux<String> flux = Flux.just("A","B","C","D","E","F")
                .delayElements(Duration.ofSeconds(1));

        ConnectableFlux<String> connectableFlux = flux.publish();
        connectableFlux.connect();
        connectableFlux.subscribe(el ->log.info("Subscriber 1: {}",el)); // does not emits the value from beginning
        Thread.sleep(3000);
        connectableFlux.subscribe(el ->log.info("Subscriber 2: {}",el));// does not emits the value from beginning
        Thread.sleep(4000);
    }
}
