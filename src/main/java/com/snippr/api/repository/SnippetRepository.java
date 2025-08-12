package com.snippr.api.repository;

import com.snippr.api.model.Snippet;
import java.util.List;
import java.util.Optional;

public interface SnippetRepository {
    Snippet save(Snippet snippet);
    List<Snippet> findAll();
    Optional<Snippet> findById(Long id);
    List<Snippet> findByLang(String lang);
}
