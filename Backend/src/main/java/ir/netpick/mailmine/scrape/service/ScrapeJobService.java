package ir.netpick.mailmine.scrape.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import ir.netpick.mailmine.common.exception.RequestValidationException;
import ir.netpick.mailmine.common.exception.ResourceNotFoundException;
import ir.netpick.mailmine.scrape.model.ScrapeJob;
import ir.netpick.mailmine.scrape.repository.ScrapeJobRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Validated
@Service
@RequiredArgsConstructor
public class ScrapeJobService {

    private final ScrapeJobRepository scrapeJobRepository;

    @Value("${env.page-size:10}")
    private int pageSize;

    public boolean scrapeJobExists(@NotNull UUID id) {
        return scrapeJobRepository.existsById(id);
    }

    public boolean scrapeJobExists(@NotNull String link) {
        return scrapeJobRepository.existsByLink(link);
    }

    public Page<ScrapeJob> allJobs(int page) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        return scrapeJobRepository.findAll(pageable);
    }

    public ScrapeJob getScrapeJob(@NotNull UUID id) {
        return scrapeJobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ScrapeJob with ID [%s] not found.".formatted(id)));
    }

    public ScrapeJob getScrapeJob(@NotNull String link) {
        return scrapeJobRepository.findByLink(link)
                .orElseThrow(
                        () -> new ResourceNotFoundException("ScrapeJob with link [%s] not found.".formatted(link)));
    }

    public void createScrapeJob(@NotNull String link, String description) {
        ScrapeJob scrapeJob = new ScrapeJob(link, description);
        scrapeJobRepository.save(scrapeJob);
        log.info("Created ScrapeJob for link: {}", link);
    }

    public void createScrapeJobList(@NotNull @Valid List<String> urls, @NotNull @Valid List<String> titles) {
        if (urls.size() != titles.size()) {
            throw new RequestValidationException("URLs and titles lists must be of equal size.");
        }

        Set<String> existingLinks = scrapeJobRepository.findAllByLinkIn(urls)
                .stream()
                .map(ScrapeJob::getLink)
                .collect(Collectors.toSet());

        List<ScrapeJob> newJobs = urls.stream()
                .filter(url -> !existingLinks.contains(url))
                .map(url -> {
                    int index = urls.indexOf(url);
                    return new ScrapeJob(url, titles.get(index));
                })
                .toList();

        if (!newJobs.isEmpty()) {
            scrapeJobRepository.saveAll(newJobs);
            log.info("Created {} new ScrapeJobs.", newJobs.size());
        } else {
            log.info("No new ScrapeJobs to create; all links exist.");
        }
    }

    public void updateScrapeJob(@NotNull ScrapeJob updates, @NotNull UUID jobId) {
        ScrapeJob existing = scrapeJobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("ScrapeJob with ID [%s] not found.".formatted(jobId)));

        boolean changed = false;

        if (updates.getLink() != null && !Objects.equals(updates.getLink(), existing.getLink())) {
            existing.setLink(updates.getLink());
            changed = true;
        }

        if (updates.getAttempt() != null && !Objects.equals(updates.getAttempt(), existing.getAttempt())) {
            existing.setAttempt(updates.getAttempt());
            changed = true;
        }

        if (updates.getDescription() != null && !Objects.equals(updates.getDescription(), existing.getDescription())) {
            existing.setDescription(updates.getDescription());
            changed = true;
        }

        if (!changed) {
            throw new RequestValidationException("No changes found in ScrapeJob update request.");
        }

        scrapeJobRepository.save(existing);
        log.info("Updated ScrapeJob with ID: {}", jobId);
    }
}