package com.snippr.api.controller;

import com.snippr.api.dto.CreateSnippetRequest;
import com.snippr.api.model.Snippet;
import com.snippr.api.service.SnippetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/snippets")
public class SnippetController {

    private final SnippetService service;

    public SnippetController(SnippetService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Snippet> create(@Valid @RequestBody CreateSnippetRequest body) {
        Snippet created = service.create(body.getLang(), body.getCode());
        return ResponseEntity
                .created(URI.create("/snippets/" + created.getId()))
                .body(created);
    }

    @GetMapping
    public List<Snippet> all(@RequestParam(value = "lang", required = false) String lang) {
        return service.getAll(lang);
    }

    @GetMapping("/{id}")
    public Snippet byId(@PathVariable("id") Long id) {
        return service.getById(id);
    }
}
