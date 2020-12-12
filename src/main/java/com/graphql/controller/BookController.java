package com.graphql.controller;

import com.graphql.service.GraphQlService;
import graphql.ExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/rest/books")
@RestController
public class BookController {

    @Autowired
    GraphQlService graphQlService;

    @PostMapping
    public ResponseEntity<Object> getAllBooks(@RequestBody String query){
        ExecutionResult execute = graphQlService.getGraphQL().execute(query);

        return new ResponseEntity<Object>(execute, HttpStatus.OK);
    }
}
