package com.snippr.api.service;

public class SnippetNotFoundException extends RuntimeException {
    public SnippetNotFoundException(Long id) {
        super("Snippet not found with id=" + id);
    }
}
