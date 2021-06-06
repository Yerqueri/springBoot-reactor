package com.yerqueri.springbootreactor.repository;

import com.yerqueri.springbootreactor.models.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@DataMongoTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    UUID uuid = UUID.randomUUID();

    List<Item> itemsList = Arrays.asList(new Item(null,"A",20D),
            new Item(null,"B",500D),
            new Item(null,"C",300D),
            new Item(null,"D",490D),
            new Item(uuid.toString(),"E",60D)
    );

    @Before
    public void init(){
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemsList))
                .log()
                .flatMap(itemReactiveRepository::save)
                .doOnNext(System.out::println)
                .blockLast(); //use blocklast to make this blocking. do not use in actual code.
    }

    @Test
    public void getItemById(){
        Mono<Item> itemMono= itemReactiveRepository.findById(Mono.just(uuid.toString())).log();
        StepVerifier.create(itemMono.log())
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("E") && item.getPrice().equals(60D))
                .verifyComplete();
    }

    @Test
    public void getAllItems(){
        Flux<Item> itemFlux= itemReactiveRepository.findAll().log();
        StepVerifier.create(itemFlux)
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void getItemByDescription(){
        Mono<Item> itemFlux= itemReactiveRepository.findByDescription("B").log();
        StepVerifier.create(itemFlux)
                .expectSubscription()
                .expectNextMatches(item->item.getDescription().equals("B"))
                .verifyComplete();

        itemFlux = itemReactiveRepository.findByDescription("Z").log();
        StepVerifier.create(itemFlux)
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void saveItem(){
        Item item = new Item(null,"F",98D);
        Mono<Item> savedItem = itemReactiveRepository.save(item);
        StepVerifier.create(savedItem.log("Saved --->"))
                .expectSubscription()
                .expectNextMatches(item1->item1.getId()!=null && item1.getDescription().equals("F"))
                .verifyComplete();
    }

    @Test
    public void updateItem(){
        Mono<Item> updatedItem =itemReactiveRepository.findByDescription("A")
                .map(item -> {
                    item.setPrice(1111D);
                    return item;
                })
                .flatMap(itemReactiveRepository::save);

        StepVerifier.create(updatedItem.log("Saved --->"))
                .expectSubscription()
                .expectNextMatches(item->item.getPrice().equals(1111D))
                .verifyComplete();
    }

    @Test
    public void deleteItem(){
        Mono<Void> deletedItem = itemReactiveRepository.findByDescription("A")
                .map(Item::getId)
                .flatMap(itemReactiveRepository::deleteById);

        StepVerifier.create(deletedItem.log("Saved --->"))
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findAll().log())
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();

        deletedItem = itemReactiveRepository.findByDescription("A")
                .flatMap(itemReactiveRepository::delete);

        StepVerifier.create(deletedItem.log("Saved --->"))
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findAll().log())
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }
}
