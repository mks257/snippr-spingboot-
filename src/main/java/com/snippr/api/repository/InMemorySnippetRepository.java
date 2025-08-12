package com.snippr.api.repository;

import com.snippr.api.model.Snippet;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemorySnippetRepository implements SnippetRepository {

    private final Map<Long, Snippet> store = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(0);

    @Override
    public Snippet save(Snippet snippet) {
        if (snippet.getId() == null) {
            snippet.setId(idGen.incrementAndGet());
        }
        if (snippet.getCreatedAt() == null) {
            snippet.setCreatedAt(Instant.now());
        }
        store.put(snippet.getId(), snippet);
        return snippet;
    }

    @Override
    public List<Snippet> findAll() {
        return store.values().stream()
                .sorted(Comparator.comparing(Snippet::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Snippet> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Snippet> findByLang(String lang) {
        if (lang == null) return findAll();
        final String needle = lang.toLowerCase();
        return store.values().stream()
                .filter(s -> s.getLang() != null && s.getLang().toLowerCase().equals(needle))
                .sorted(Comparator.comparing(Snippet::getId))
                .collect(Collectors.toList());
    }
}
