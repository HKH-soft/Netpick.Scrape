package ir.netpick.scrape.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ir.netpick.scrape.services.ScrapeService;
import ir.netpick.scrape.models.ApiKey;
import ir.netpick.scrape.models.ApiKeyRequest;
import ir.netpick.scrape.models.SearchQuery;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@RestController
@RequestMapping("/scrape")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class ScrapeController {

    private final ScrapeService scrapeService;

    public ScrapeController(ScrapeService scrapeService) {
        this.scrapeService = scrapeService;
    }

    // API Keys CRUD

    @GetMapping("/apikey")
    public ResponseEntity<?> getApiKeys(@RequestParam(defaultValue = "1") Integer page) {
        return ResponseEntity.ok().body(scrapeService.allKeys(page));
    }

    @GetMapping("/apikey/{id}")
    public ResponseEntity<?> getApiKey(@PathVariable UUID id) {
        return ResponseEntity.ok().body(scrapeService.getKey(id));
    }

    @PostMapping("/apikey")
    public ResponseEntity<?> createApiKey(@RequestBody ApiKeyRequest request) {
        return ResponseEntity.ok().body(scrapeService.createKey(request));
    }

    @PutMapping("/apikey/{id}")
    public ResponseEntity<?> updateApiKey(@PathVariable UUID id, @RequestBody ApiKeyRequest request) {
        return ResponseEntity.ok().body(scrapeService.updateKey(id, request));
    }

    @DeleteMapping("/apikey/{id}")
    public ResponseEntity<?> deleteApiKey(@PathVariable UUID id) {
        scrapeService.deleteKey(id);
        return ResponseEntity.noContent().build();
    }

    // Search Queries CRUD

    @GetMapping("/search_query")
    public ResponseEntity<?> getSearchQuerys(@RequestParam(defaultValue = "1") Integer page) {
        return ResponseEntity.ok().body(scrapeService.allSearchQuerys(page));
    }

    @GetMapping("/search_query/{id}")
    public ResponseEntity<?> getSearchQuery(@PathVariable UUID id) {
        return ResponseEntity.ok().body(scrapeService.getSearchQuery(id));
    }

    @PostMapping("/search_query")
    public ResponseEntity<?> createSearchQuery(@RequestBody SearchQuery searchQuery) {
        return ResponseEntity.ok().body(scrapeService.createSearchQuery(searchQuery));
    }

    @PutMapping("/search_query/{id}")
    public ResponseEntity<?> updateSearchQuery(@PathVariable UUID id, @RequestBody SearchQuery searchQuery) {
        return ResponseEntity.ok().body(scrapeService.updateSearchQuery(id, searchQuery));
    }

    @DeleteMapping("/search_query/{id}")
    public ResponseEntity<?> deleteSearchQuery(@PathVariable UUID id) {
        scrapeService.deleteSearchQuery(id);
        return ResponseEntity.noContent().build();
    }

}