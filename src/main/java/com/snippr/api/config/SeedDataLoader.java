package com.snippr.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.snippr.api.model.Snippet;
import com.snippr.api.repository.SnippetRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Component
public class SeedDataLoader {

    private static final Logger log = LoggerFactory.getLogger(SeedDataLoader.class);
    private final SnippetRepository repository;

    public SeedDataLoader(SnippetRepository repository) {
        this.repository = repository;
    }

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
                            repository.save(s);
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

    private List<Snippet> parse(String filename, InputStream in) throws IOException {
        if (filename.endsWith(".json")) {
            ObjectMapper om = new ObjectMapper();
            Snippet[] arr = om.readValue(in, Snippet[].class);
            return Arrays.asList(arr);
        } else if (filename.endsWith(".yaml") || filename.endsWith(".yml")) {
            ObjectMapper om = new ObjectMapper(new YAMLFactory());
            Snippet[] arr = om.readValue(in, Snippet[].class);
            return Arrays.asList(arr);
        } else if (filename.endsWith(".xml")) {
            XmlMapper om = new XmlMapper();
            Snippet[] arr = om.readValue(in, Snippet[].class);
            return Arrays.asList(arr);
        }
        return null;
    }
}
