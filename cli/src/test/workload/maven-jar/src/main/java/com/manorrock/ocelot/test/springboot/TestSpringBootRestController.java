package com.manorrock.ocelot.test.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestSpringBootRestController {

    @GetMapping(value = "/")
    public String getCities() {
        return "Hello World";
    }    
}
