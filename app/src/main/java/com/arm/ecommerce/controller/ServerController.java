package com.arm.ecommerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/server")
public class ServerController {

    @GetMapping
    public ResponseEntity<String> isServerAlive() {
        return new ResponseEntity<>("Server is Alive", HttpStatus.OK);
    }

}
