package ir.netpick.mailmine.scrape.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ir.netpick.mailmine.common.exception.ResourceNotFoundException;
import ir.netpick.mailmine.scrape.file.FileManagement;
import ir.netpick.mailmine.scrape.model.ScrapeData;
import ir.netpick.mailmine.scrape.parser.ContactInfoParser;
import ir.netpick.mailmine.scrape.repository.ScrapeDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ScrapePipeline {

    private final ApiCaller apiCaller;
    private final SearchQueryService searchQueryService;
    private final Scraper scraper;
    private final ScrapeDataRepository scrapeDataRepository;
    private final FileManagement fileManagement;

    public void linkGrabber() {
        if (searchQueryService.isEmpty()) {
            throw new ResourceNotFoundException("There is no Search Query please add one...");
        }
        log.info("starting the google call...");
        apiCaller.callGoogleSearch();
        log.info("Link Grabber is Done.");
    }

    public void pageSourceGrabber() {
        log.info("starting to download the pages...");
        scraper.scrapePendingJobs(true);
        log.info("Page Grabber is Done.");
    }

    @Transactional
    public void processUnparsedFiles() {
        List<ScrapeData> unparsedFiles = scrapeDataRepository.findByParsedFalse();

        for (ScrapeData scrapeData : unparsedFiles) {
            try {
                Path filePath = fileManagement.getFilePath(
                        scrapeData.getScrapeJob().getId(),
                        scrapeData.getAttemptNumber(),
                        scrapeData.getFileName());

                if (!Files.exists(filePath)) {
                    log.warn("Missing file for record {}", scrapeData.getId());
                    continue;
                }

                String content = Files.readString(filePath);
                ContactInfoParser.parse(content);

                scrapeData.setParsed(true);
                scrapeDataRepository.save(scrapeData);

                log.info("Successfully parsed and updated record {}", scrapeData.getId());
            } catch (IOException e) {
                log.error("Error processing file for record {}", scrapeData.getId(), e);
            }
        }
    }
}
