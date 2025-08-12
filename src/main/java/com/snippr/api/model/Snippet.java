package com.snippr.api.model;

import java.time.Instant;

public class Snippet {
    private Long id;
    private String lang;
    private String code;
    private Instant createdAt;

    public Snippet() {}

    public Snippet(Long id, String lang, String code, Instant createdAt) {
        this.id = id;
        this.lang = lang;
        this.code = code;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLang() { return lang; }
    public void setLang(String lang) { this.lang = lang; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
