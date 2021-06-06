package com.yerqueri.springbootreactor.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "items")
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    private String id;
    private String description;
    private Double price;
}
