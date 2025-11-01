package ir.netpick.mailmine.scrape.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.api.client.util.Value;

import ir.netpick.mailmine.common.exception.ResourceNotFoundException;
import ir.netpick.mailmine.scrape.file.FileManagement;
import ir.netpick.mailmine.scrape.model.ScrapeData;
import ir.netpick.mailmine.scrape.model.ScrapeJob;
import ir.netpick.mailmine.scrape.repository.ScrapeDataRepository;
import ir.netpick.mailmine.scrape.repository.ScrapeJobRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrapeDataService {

    private final ScrapeDataRepository scrapeDataRepository;
    private final ScrapeJobRepository scrapeJobRepository;
    private final FileManagement fileManagment;

    @Value("${env.page-size:10}")
    private int pageSize;

    public List<ScrapeData> allDatas() {
        return scrapeDataRepository.findAll();
    }

    public List<ScrapeData> allDatas(int page) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return scrapeDataRepository.findAll(pageable).getContent();
    }

    public void createScrapeData(String pageData, UUID scrapeJobId) {
        ScrapeJob scrapeJob = scrapeJobRepository.findById(scrapeJobId).orElseThrow(
                () -> new ResourceNotFoundException("ScrapeJob with id [%s] was not found!".formatted(scrapeJobId)));
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd_HH.mm");
        String fileName = dateFormat.format(LocalDateTime.now()) + ".txt";
        fileManagment.CreateAFile(scrapeJobId, scrapeJob.getAttempt(), fileName, pageData);
        ScrapeData scrapeData = new ScrapeData(fileName, scrapeJob.getAttempt(), scrapeJob);
        scrapeDataRepository.save(scrapeData);
    }

}
