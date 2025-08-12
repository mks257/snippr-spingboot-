package com.snippr.api.service;

import com.snippr.api.model.Snippet;
import com.snippr.api.repository.SnippetRepository;
import com.snippr.api.security.CryptoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SnippetService {
    private final SnippetRepository repository;
    private final CryptoService crypto;

    public SnippetService(SnippetRepository repository, CryptoService crypto) {
        this.repository = repository;
        this.crypto = crypto;
    }

    public Snippet create(String lang, String code) {
        String enc = crypto.encrypt(code);                 // Encrypt before saving
        Snippet s = new Snippet(null, lang, enc, null);
        return repository.save(s);
    }

    public List<Snippet> getAll(String lang) {
        return (lang != null && !lang.isBlank() ? repository.findByLang(lang) : repository.findAll())
                .stream().map(this::decryptView).collect(Collectors.toList());
    }

    public Snippet getById(Long id) {
        Snippet s = repository.findById(id).orElseThrow(() -> new SnippetNotFoundException(id));
        return decryptView(s);
    }

    private Snippet decryptView(Snippet s) {
        return new Snippet(s.getId(), s.getLang(), crypto.decrypt(s.getCode()), s.getCreatedAt());
    }
}
