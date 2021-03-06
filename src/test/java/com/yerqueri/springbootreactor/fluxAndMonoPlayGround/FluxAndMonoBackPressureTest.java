package com.yerqueri.springbootreactor.fluxAndMonoPlayGround;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoBackPressureTest {

    @Test
    public void back_pressure_test(){
        Flux<Integer>  finiteFlux = Flux.range(1,10).log();
        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify();
    }

    @Test
    public void back_pressure(){
        Flux<Integer>  finiteFlux = Flux.range(1,10).log();
        finiteFlux.subscribe(element->System.out.println("Element is ->"+element),
                e->System.err.println("Exception is -->"+e),
                ()->System.out.println("Done"),
                subscription -> subscription.request(3));
    }

    @Test
    public void back_pressure_cancel(){
        Flux<Integer>  finiteFlux = Flux.range(1,10).log();
        finiteFlux.subscribe(element->System.out.println("Element is ->"+element),
                e->System.err.println("Exception is -->"+e),
                ()->System.out.println("Done"),
                Subscription::cancel);
    }

    @Test
    public void back_pressure_customised(){
        Flux<Integer>  finiteFlux = Flux.range(1,10).log();
        finiteFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                request(1);
                System.out.println("Value recieved is:"+value);
                if(value==5){
                    cancel();
                }

            }
        });
    }

}
