package ir.netpick.scrape.services;

import ir.netpick.scrape.exception.RequestValidationExeption;
import ir.netpick.scrape.exception.ResourceNotFoundExeption;
import ir.netpick.scrape.models.ApiKey;
import ir.netpick.scrape.models.ApiKeyRequest;
import ir.netpick.scrape.models.ScrapeData;
import ir.netpick.scrape.models.ScrapeJob;
import ir.netpick.scrape.models.SearchQuery;
import ir.netpick.scrape.repositories.ApiKeyRepository;
import ir.netpick.scrape.repositories.ScrapeDataRepository;
import ir.netpick.scrape.repositories.ScrapeJobRepository;
import ir.netpick.scrape.repositories.SearchQueryRepository;
import ir.netpick.scrape.scrapper.FileManagment;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScrapeService {

    private final ApiKeyRepository apiKeyRepository;
    private final SearchQueryRepository searchQueryRepository;
    private final ScrapeJobRepository scrapeJobRepository;
    private final FileManagment fileManagment;
    private final ScrapeDataRepository scrapeDataRepository;

    @Value("${env.page-size:10}")
    private int pageSize;

    // google search

    // scrapping

    // scrape data

    public List<ScrapeData> allDatas() {
        return scrapeDataRepository.findAll();
    }

    public List<ScrapeData> allDatas(int page) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return scrapeDataRepository.findAll(pageable).getContent();
    }

    public void createScrapeData(String pageData, UUID scrapeJobId) {
        ScrapeJob scrapeJob = scrapeJobRepository.findById(scrapeJobId).orElseThrow(
                () -> new ResourceNotFoundExeption("ScrapeJob with id [%s] was not found!".formatted(scrapeJobId)));
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd_HH.mm");
        String fileName = dateFormat.format(LocalDateTime.now()) + ".txt";
        fileManagment.CreateAFile(scrapeJobId, scrapeJob.getAttempt(), fileName, pageData);
        ScrapeData scrapeData = new ScrapeData(fileName, scrapeJob.getAttempt(), scrapeJob);
        scrapeDataRepository.save(scrapeData);
    }

    // scrape jobs

    public boolean scrapeJobExists(UUID id) {
        return scrapeJobRepository.existsById(id);
    }

    public boolean scrapeJobExists(String link) {
        return scrapeJobRepository.existsByLink(link);
    }

    public List<ScrapeJob> allJobs(int page) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return scrapeJobRepository.findAll(pageable).getContent();
    }

    public ScrapeJob getScrapeJob(UUID id) {
        return scrapeJobRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundExeption("Scrape Job with id [%s] was not found!".formatted(id)));
    }

    public ScrapeJob getScrapeJob(String link) {
        return scrapeJobRepository.findByLink(link).orElseThrow(
                () -> new ResourceNotFoundExeption("Scrape Job with link [%s] was not found!".formatted(link)));
    }

    public void createScrapeJob(ScrapeJob scrapeJob) {
        scrapeJobRepository.save(scrapeJob);
    }

    public void createScrapeJob(String link) {
        ScrapeJob scrapeJob = new ScrapeJob(link);
        scrapeJobRepository.save(scrapeJob);
    }

    public void createScrapeJob(String link, String description) {
        ScrapeJob scrapeJob = new ScrapeJob(link, description);
        scrapeJobRepository.save(scrapeJob);
    }

    public void createScrapeJobList(List<String> urls, List<String> titles) {
        if (urls.size() != titles.size()) {
            return;
        }
        for (int i = 0; i < urls.size(); i++) {
            if (!scrapeJobRepository.existsByLink(urls.get(i))) {
                ScrapeJob scrapeJob = new ScrapeJob(urls.get(i), titles.get(i));
                scrapeJobRepository.save(scrapeJob);
            }
        }
    }

    public void updateScrapeJob(ScrapeJob scrapeJob, UUID jobId) {
        ScrapeJob before = scrapeJobRepository.findById(jobId).orElseThrow(
                () -> new ResourceNotFoundExeption("ScrapeJob with the id [%s] was not found!".formatted(jobId)));
        boolean changed = false;
        ScrapeJob newJob = new ScrapeJob();
        newJob.setId(jobId);

        if (scrapeJob.getLink() != null && scrapeJob.getLink() != before.getLink()) {
            changed = true;
            newJob.setLink(scrapeJob.getLink());
        } else {
            newJob.setLink(before.getLink());
        }

        if (scrapeJob.getAttempt() != null && scrapeJob.getAttempt() != before.getAttempt()) {
            changed = true;
            newJob.setAttempt(scrapeJob.getAttempt());
        } else {
            newJob.setAttempt(before.getAttempt());
        }

        if (scrapeJob.getDescription() != null && scrapeJob.getDescription() != before.getDescription()) {
            changed = true;
            newJob.setDescription(scrapeJob.getDescription());
        } else {
            newJob.setDescription(before.getDescription());
        }

        if (!changed) {
            throw new RequestValidationExeption("No changes where found in scrapeJob update request!");
        }

        scrapeJobRepository.save(newJob);

    }

    // api keys

    public Page<ApiKey> allKeys(int page) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return apiKeyRepository.findAll(pageable);
    }

    public ApiKey getKey(UUID id) {
        return apiKeyRepository.findById(id).orElseThrow(() -> new RuntimeException("ApiKey not found"));
    }

    public ApiKey createKey(ApiKeyRequest apiKeyRequest) {
        ApiKey apiKey = new ApiKey(apiKeyRequest.key(), apiKeyRequest.point(), apiKeyRequest.apiLink(),
                apiKeyRequest.searchEngineId(),
                apiKeyRequest.description());
        return apiKeyRepository.save(apiKey);
    }

    public ApiKey updateKey(UUID id, ApiKeyRequest updatedApiKey) {
        ApiKey existing = getKey(id);
        existing.setKey(updatedApiKey.key());
        existing.setPoint(updatedApiKey.point());
        existing.setApiLink(updatedApiKey.apiLink());
        existing.setSearchEngineId(updatedApiKey.searchEngineId());
        existing.setDescription(updatedApiKey.description());
        return apiKeyRepository.save(existing);
    }

    public void deleteKey(UUID id) {
        apiKeyRepository.deleteById(id);
    }

    // search querys

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