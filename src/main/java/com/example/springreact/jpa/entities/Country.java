package com.example.springreact.jpa.entities;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "Country")
@NoArgsConstructor
public class Country {

    @Id
    private Long id;
    private String name;
    private String continent;
    private String population;
    private String cities;
}
