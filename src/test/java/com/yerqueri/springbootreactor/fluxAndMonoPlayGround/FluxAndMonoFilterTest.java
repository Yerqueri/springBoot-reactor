package com.yerqueri.springbootreactor.fluxAndMonoPlayGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {

    List<String> names = Arrays.asList("Adam","Anna","Jack","Jenny");

    @Test
    public void filterTest(){
        Flux<String> stringFlux = Flux.fromIterable(names);
        stringFlux = stringFlux
                .filter(name->name.startsWith("A"))
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Adam","Anna")
                .verifyComplete();

        stringFlux=Flux.fromIterable(names);
        StepVerifier.create(stringFlux
                    .filter(s->s.length()>4)
                    .log()
                ).expectNext("Jenny")
                .verifyComplete();
    }
}
