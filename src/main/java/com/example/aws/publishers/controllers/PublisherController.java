package com.example.aws.publishers.controllers;

import com.example.aws.publishers.controllers.dtos.requests.MessageRequestDTO;
import com.example.aws.publishers.services.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/publishers")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    @PostMapping
    public ResponseEntity<Void> postMessage(@Valid @RequestBody MessageRequestDTO messageRequestDTO) {
        publisherService.postMessage(messageRequestDTO);
        return ResponseEntity.ok().build();
    }

}
