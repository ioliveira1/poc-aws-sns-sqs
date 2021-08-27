package com.example.aws.consumers.controllers;

import com.example.aws.consumers.controllers.dtos.response.MessageResponseDTO;
import com.example.aws.consumers.services.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/consumers")
@Slf4j
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @GetMapping
    public ResponseEntity<List<MessageResponseDTO>> getMessages() {
        return ResponseEntity.ok(consumerService.getMessage());
    }

}
