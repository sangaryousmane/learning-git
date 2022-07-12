package com.example.springreact.playground;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@Data
public class Apple {

    private final String name;
    private final String color;


    public static void main(String[] args) {

    }

}
