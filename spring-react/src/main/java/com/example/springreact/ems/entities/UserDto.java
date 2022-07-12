package com.example.springreact.ems.entities;

import lombok.Data;

@Data
public class UserDto{
    private Long id;
    private String name;
    private String email;
    private String password;
}
