package com.yerqueri.springbootreactor.fluxAndMonoPlayGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;

public class FluxAndMonoErrorTest {

    @Test
    public void ErrorTestHAndling(){
        Flux<String> flux = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("D"));

        StepVerifier.create(flux.log())
                .expectNext("A","B","C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void ErrorHandling_ErrorResume(){
        Flux<String> flux = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("D"))
                .onErrorResume(e->Mono.empty());

        StepVerifier.create(flux.log())
                .expectNext("A","B","C")
                .verifyComplete();

        flux = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("D"))
                .onErrorResume(e->Mono.just("E"));

        StepVerifier.create(flux.log())
                .expectNext("A","B","C")
                .expectNext("E")
                .verifyComplete();
    }

    @Test
    public void ErrorHandling_ErrorReturn(){
        Flux<String> flux = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("fallback");

        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNext("A","B","C")
                .expectNext("fallback")
                .verifyComplete();

    }

    @Test
    public void ErrorHandling_ErrorMap(){
        Flux<String> flux = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("D"))
                .onErrorMap(CustomException::new)
                .onErrorResume(e->Mono.empty());

        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNext("A","B","C")
                .verifyComplete();

        flux = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("D"))
                .onErrorMap(CustomException::new);

        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNext("A","B","C")
                .expectError(CustomException.class)
                .verify();

    }

    @Test
    public void ErrorHandling_ErrorRetry(){
        Flux<String> flux = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("D"))
                .onErrorMap(CustomException::new)
                .retry(2);

        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNext("A","B","C")
                .expectNext("A","B","C")
                .expectNext("A","B","C")
                .expectError(CustomException.class)
                .verify();

    }

    @Test
    public void ErrorHandling_Error_BackOff(){
        Flux<String> flux = Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("D"))
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(3)))
                .onErrorMap(CustomException::new);

        StepVerifier.create(flux.log())
                .expectSubscription()
                .expectNext("A","B","C")
                .expectNext("A","B","C")
                .expectNext("A","B","C")
                .expectError(CustomException.class)
                .verify();

    }
}
