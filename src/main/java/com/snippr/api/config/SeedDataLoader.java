package com.snippr.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.snippr.api.model.Snippet;
import com.snippr.api.service.SnippetService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Component
public class SeedDataLoader {
    private static final Logger log = LoggerFactory.getLogger(SeedDataLoader.class);
    private final SnippetService service;

    public SeedDataLoader(SnippetService service) { this.service = service; }

    @PostConstruct
    public void load() {
        String[] candidates = {"seedData.json", "seedData.yaml", "seedData.xml"};
        for (String name : candidates) {
            ClassPathResource res = new ClassPathResource(name);
            if (res.exists()) {
                try (InputStream in = res.getInputStream()) {
                    List<Snippet> snippets = parse(name, in);
                    if (snippets != null) {
                        for (Snippet s : snippets) {
                            service.create(s.getLang(), s.getCode()); // encrypts on save
                        }
                        log.info("Loaded {} snippets from {}", snippets.size(), name);
                        return;
                    }
                } catch (Exception e) {
                    log.warn("Failed to load {}: {}", name, e.getMessage());
                }
            }
        }
        log.info("No seed data file found. Starting with empty repository.");
    }

    private List<Snippet> parse(String filename, InputStream in) throws Exception {
        if (filename.endsWith(".json")) {
            ObjectMapper om = new ObjectMapper();
            return Arrays.asList(om.readValue(in, Snippet[].class));
        } else if (filename.endsWith(".yaml") || filename.endsWith(".yml")) {
            ObjectMapper om = new ObjectMapper(new YAMLFactory());
            return Arrays.asList(om.readValue(in, Snippet[].class));
        } else if (filename.endsWith(".xml")) {
            XmlMapper om = new XmlMapper();
            return Arrays.asList(om.readValue(in, Snippet[].class));
        }
        return null;
    }
}
