package ir.netpick.scrape.services;

import ir.netpick.scrape.models.ApiKey;
import ir.netpick.scrape.models.ApiKeyRequest;
import ir.netpick.scrape.models.SearchQuery;
import ir.netpick.scrape.repositories.ApiKeyRepository;
import ir.netpick.scrape.repositories.SearchQueryRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ScrapeService {

    private final ApiKeyRepository apiKeyRepository;
    private final SearchQueryRepository searchQueryRepository;

    @Value("${env.page-size:10}")
    private int pageSize;

    public ScrapeService(ApiKeyRepository apiKeyRepository, SearchQueryRepository searchQueryRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.searchQueryRepository = searchQueryRepository;
    }

    public Page<ApiKey> allKeys(int page) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return apiKeyRepository.findAll(pageable);
    }

    public ApiKey getKey(UUID id) {
        return apiKeyRepository.findById(id).orElseThrow(() -> new RuntimeException("ApiKey not found"));
    }

    public ApiKey createKey(ApiKeyRequest apiKeyRequest) {
        ApiKey apiKey = new ApiKey(apiKeyRequest.key(), apiKeyRequest.point(), apiKeyRequest.apiLink(),
                apiKeyRequest.description());
        return apiKeyRepository.save(apiKey);
    }

    public ApiKey updateKey(UUID id, ApiKeyRequest updatedApiKey) {
        ApiKey existing = getKey(id);
        existing.setKey(updatedApiKey.key());
        existing.setPoint(updatedApiKey.point());
        existing.setApiLink(updatedApiKey.apiLink());
        existing.setDescription(updatedApiKey.description());
        return apiKeyRepository.save(existing);
    }

    public void deleteKey(UUID id) {
        apiKeyRepository.deleteById(id);
    }

    public Page<SearchQuery> allSearchQuerys(int page) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return searchQueryRepository.findAll(pageable);
    }

    public SearchQuery getSearchQuery(UUID id) {
        return searchQueryRepository.findById(id).orElseThrow(() -> new RuntimeException("SearchQuery not found"));
    }

    public SearchQuery createSearchQuery(SearchQuery searchQuery) {
        return searchQueryRepository.save(searchQuery);
    }

    public SearchQuery updateSearchQuery(UUID id, SearchQuery updatedSearchQuery) {
        SearchQuery existing = getSearchQuery(id);
        existing.setSentence(updatedSearchQuery.getSentence());
        existing.setLink_count(updatedSearchQuery.getLink_count());
        existing.setDescription(updatedSearchQuery.getDescription());
        return searchQueryRepository.save(existing);
    }

    public void deleteSearchQuery(UUID id) {
        searchQueryRepository.deleteById(id);
    }

}