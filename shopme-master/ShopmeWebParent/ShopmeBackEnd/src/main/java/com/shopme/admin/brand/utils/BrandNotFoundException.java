package com.shopme.admin.brand.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BrandNotFoundException extends Exception{

    public BrandNotFoundException(String message) {
        super(message);
    }
}
