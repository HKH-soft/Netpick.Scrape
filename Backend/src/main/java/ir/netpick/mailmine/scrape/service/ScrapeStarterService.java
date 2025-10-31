package ir.netpick.mailmine.scrape.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ir.netpick.mailmine.common.exception.ResourceNotFoundExeption;
import ir.netpick.mailmine.scrape.file.FileManagement;
import ir.netpick.mailmine.scrape.model.ScrapeData;
import ir.netpick.mailmine.scrape.repository.ScrapeDataRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrapeStarterService {

    private static final Logger logger = LogManager.getLogger(FileManagement.class);

    private final ApiCaller apiCaller;
    private final ScrapeService scrapeService;
    private final PageScrape pageScrape;
    private final ScrapeDataRepository scrapeDataRepository;
    private final FileManagement fileManagement;

    public void linkGrabber() {
        if (scrapeService.isSearchQuerysEmpty()) {
            throw new ResourceNotFoundExeption("There is no Search Query please add one...");
        }
        logger.info("starting the google call...");
        apiCaller.callGoogleSearch();
        logger.info("Link Grabber is Done.");
    }

    public void pageSourceGrabber() {
        logger.info("starting to download the pages...");
        pageScrape.webGet();
        logger.info("Page Grabber is Done.");
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
                    logger.warn("Missing file for record {}", scrapeData.getId());
                    continue;
                }

                String content = Files.readString(filePath);
                ContactInfoParser.parse(content); // parse and save contact info

                scrapeData.setParsed(true);
                scrapeDataRepository.save(scrapeData);

                logger.info("Successfully parsed and updated record {}", scrapeData.getId());
            } catch (IOException e) {
                logger.error("Error processing file for record {}", scrapeData.getId(), e);
            }
        }
    }
}
