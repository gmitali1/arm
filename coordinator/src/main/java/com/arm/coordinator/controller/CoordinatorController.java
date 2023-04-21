package com.arm.coordinator.controller;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CoordinatorController {

    @GetMapping("/products")
    @ResponseStatus(HttpStatus.OK)
    private void getUsers() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String uri = "http://localhost:8080/api/products";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<List> result =
                restTemplate.exchange(uri, HttpMethod.GET, entity, List.class);
        System.out.println(result.getBody());
    }
}
