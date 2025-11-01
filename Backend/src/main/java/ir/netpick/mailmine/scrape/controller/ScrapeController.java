package ir.netpick.mailmine.scrape.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ir.netpick.mailmine.scrape.service.ApiCaller;
import ir.netpick.mailmine.scrape.service.Scraper;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/scrape")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class ScrapeController {

    private final Scraper scrape;
    private final ApiCaller apiCaller;

    @PostMapping("start-google")
    public void startSearch() {
        apiCaller.callGoogleSearch();
    }

    @PostMapping("start-scrape")
    public void startScrapping(@RequestBody String entity) {
        scrape.scrapePendingJobs(true);
    }

}