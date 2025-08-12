package com.snippr.api.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateSnippetRequest {
    @NotBlank(message = "lang is required")
    private String lang;

    @NotBlank(message = "code is required")
    private String code;

    public String getLang() { return lang; }
    public void setLang(String lang) { this.lang = lang; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
