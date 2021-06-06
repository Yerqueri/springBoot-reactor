package com.yerqueri.springbootreactor.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@WebFluxTest
@AutoConfigureWebTestClient(timeout = "36000")
@Slf4j
public class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void getFlux_1(){
        Flux<Integer> testFlux =webTestClient
                .get()
                .uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(testFlux)
                .expectSubscription()
                .expectNext(1,2,3,4,5,6,7,8,9,10)
                .verifyComplete();
    }

    @Test
    public void getFlux_2(){
        webTestClient
                .get()
                .uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .hasSize(10);
    }

    @Test
    public void getFlux_3(){
        EntityExchangeResult<List<Integer>> result = webTestClient
                .get()
                .uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .hasSize(10)
                .returnResult();

        List<Integer> expectedIntegerList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        assertEquals(expectedIntegerList,result.getResponseBody());
    }

    @Test
    public void getFlux_4(){
        List<Integer> expectedIntegerList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        webTestClient
                .get()
                .uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .hasSize(10)
                .consumeWith((response)->assertEquals(expectedIntegerList,response.getResponseBody()));
    }

    @Test
    public void getFluxStream_1(){
        Flux<Integer> testFlux =webTestClient
                .get()
                .uri("/fluxStream")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(testFlux)
                .expectSubscription()
                .expectNext(0,1,2,3,4,5,6,7,8,9,10)
                .thenCancel()
                .verify();
    }

    @Test
    public void getMono_1(){
        log.info("{}",Runtime.getRuntime().availableProcessors());
        webTestClient
                .get()
                .uri("/mono")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith((response)->assertEquals(1,response.getResponseBody()));
    }
}
