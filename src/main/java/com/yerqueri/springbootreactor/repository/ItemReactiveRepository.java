package com.yerqueri.springbootreactor.repository;

import com.yerqueri.springbootreactor.models.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item,String> {

    Mono<Item> findByDescription(String description);
    Flux<Item> findItemByPriceGreaterThan(Double price);
}
