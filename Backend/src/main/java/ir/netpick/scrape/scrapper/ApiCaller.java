package ir.netpick.scrape.scrapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ir.netpick.scrape.models.ApiKey;
import ir.netpick.scrape.models.SearchQuery;
import ir.netpick.scrape.repositories.ApiKeyRepository;
import ir.netpick.scrape.repositories.SearchQueryRepository;
import ir.netpick.scrape.services.ScrapeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiCaller {
    private static final Logger logger = LogManager.getLogger(ApiCaller.class);

    private final ApiKeyRepository apiKeyRepository;
    private final ScrapeService scrapeService;
    private final SearchQueryRepository searchQueryRepository;

    private final WebClient webClient = WebClient.create();

    @Transactional
    public void callGoogleSearch() {
        List<ApiKey> keys = new ArrayList<>(apiKeyRepository.findAll());
        if (keys.isEmpty())
            throw new RuntimeException("No API keys configured");

        List<SearchQuery> queries = searchQueryRepository.findAllBelowTenOrderByLinkCount();

        System.out.println(buildUri(queries.get(0).getSentence(), 0, keys.get(0)));

        for (SearchQuery query : queries) {
            for (int i = 0; i < 3; i++) {
                String uri = buildUri(query.getSentence(), i, keys.get(0));
                try {
                    String json = webClient.get().uri(uri)
                            .retrieve()
                            .bodyToMono(String.class)
                            .block();

                    List<LinkParser.LinkResult> parsedLinks = LinkParser.parse(json);
                    List<String> urls = parsedLinks.stream().map(LinkParser.LinkResult::getLink).toList();
                    List<String> titles = parsedLinks.stream().map(LinkParser.LinkResult::getTitle).toList();
                    System.out.println(urls);
                    System.out.println(titles);
                    scrapeService.createScrapeJobList(urls, titles);

                } catch (Exception e) {
                    logger.error("API call failed for {}", query.getSentence(), e);
                }
            }
        }
    }

    private String buildUri(String sentence, int index, ApiKey key) {
        int count = 10;
        return key.getApiLink()
                .replace("<query>", sentence.replace(" ", "+"))
                .replace("<api_key>", key.getKey())
                .replace("<search_engine_id>", key.getSearchEngineId())
                .replace("<start_index>", String.valueOf(index * count + 1))
                .replace("<count>", String.valueOf(count));
    }

}
