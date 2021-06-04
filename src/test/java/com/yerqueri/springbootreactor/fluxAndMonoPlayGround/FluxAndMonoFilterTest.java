package com.yerqueri.springbootreactor.fluxAndMonoPlayGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {

    List<String> names = Arrays.asList("Adam","Anna","Jack","Jenny");

    @Test
    public void filterTest(){
        Flux<String> stringFlux = Flux.fromIterable(names);
    }
}
