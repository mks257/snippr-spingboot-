package com.snippr.api.service;

import com.snippr.api.model.Snippet;
import com.snippr.api.repository.SnippetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SnippetService {
    private final SnippetRepository repository;

    public SnippetService(SnippetRepository repository) {
        this.repository = repository;
    }

    public Snippet create(String lang, String code) {
        Snippet s = new Snippet(null, lang, code, null);
        return repository.save(s);
    }

    public List<Snippet> getAll(String lang) {
        if (lang != null && !lang.isBlank()) {
            return repository.findByLang(lang);
        }
        return repository.findAll();
    }

    public Snippet getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new SnippetNotFoundException(id));
    }
}
