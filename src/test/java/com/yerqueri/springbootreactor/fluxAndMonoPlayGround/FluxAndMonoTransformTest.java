package com.yerqueri.springbootreactor.fluxAndMonoPlayGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoTransformTest {
    List<String> names = Arrays.asList("Adam","Anna","Jack","Jenny");

    @Test
    public void TranformUsingMap(){
        Flux<String> flux = Flux
                .fromIterable(names)
                .map(name->name.toLowerCase())
                .log();
        StepVerifier.create(flux.log())
                .expectNext("adam","anna","jack","jenny")
                .verifyComplete();
    }

    @Test
    public void TranformUsingMap_Length(){
        Flux<Integer> flux = Flux
                .fromIterable(names)
                .map(name->name.length())
                .log();
        StepVerifier.create(flux.log())
                .expectNext(4,4,4,5)
                .verifyComplete();
    }

    @Test
    public void TranformUsingMap_Length_Repeat(){
        Flux<Integer> flux = Flux
                .fromIterable(names)
                .map(name->name.length())
                .repeat(1)
                .log();
        StepVerifier.create(flux.log())
                .expectNext(4,4,4,5,4,4,4,5)
                .verifyComplete();
    }

    @Test
    public void TranformUsingMap_Filter(){
        Flux<String> flux = Flux
                .fromIterable(names)
                .filter(name->name.length()>4)
                .map(name->name.toUpperCase())
                .repeat(1)
                .log();
        StepVerifier.create(flux.log())
                .expectNext("JENNY","JENNY")
                .verifyComplete();
    }

    @Test
    public void TranformUsingFlatMap(){
        Flux<String> flux = Flux
                .fromIterable(names)
                .flatMap(name->{
                    return Flux.fromIterable(convertNamesToFlux(name));
                })
                .log();
        StepVerifier.create(flux)
                .expectNextCount(8)
                .verifyComplete();
    }

    @Test
    public void TranformUsingFlatMap_Parallel(){
        Flux<String> flux = Flux
                .fromIterable(names)
                .window(2) //(adam,anna),(jack,jenny) ->Flux<Flux<String>>
                .flatMap(nameFlux->nameFlux // Flux<String>
                        .map(this::convertNamesToFlux) // Flux<List<String>>
                        .subscribeOn(Schedulers.parallel()))
                .flatMap(Flux::fromIterable)
                .log();
        StepVerifier.create(flux)
                .expectNextCount(8)
                .verifyComplete();
    }

    @Test
    public void TranformUsingFlatMap_Parallel_Maintain_Order(){
        Flux<String> flux = Flux
                .fromIterable(names)
                .window(2) //(adam,anna),(jack,jenny) ->Flux<Flux<String>>
                .concatMap(nameFlux->nameFlux // Flux<String>
                        .map(this::convertNamesToFlux) // Flux<List<String>>
                        .subscribeOn(Schedulers.parallel()))
                .flatMap(Flux::fromIterable)
                .log();
        StepVerifier.create(flux)
                .expectNextCount(8)
                .verifyComplete();

        flux = Flux
                .fromIterable(names)
                .window(2) //(adam,anna),(jack,jenny) ->Flux<Flux<String>>
                .flatMapSequential(nameFlux->nameFlux // Flux<String>
                        .map(this::convertNamesToFlux) // Flux<List<String>>
                        .subscribeOn(Schedulers.parallel()))
                .flatMap(Flux::fromIterable)
                .log();
        StepVerifier.create(flux)
                .expectNextCount(8)
                .verifyComplete();
    }

    private List<String> convertNamesToFlux(String name) {
        try {
            Thread.sleep(2000);
        }catch (InterruptedException e){

        }
        return Arrays.asList(name,"salt");
    }
}
