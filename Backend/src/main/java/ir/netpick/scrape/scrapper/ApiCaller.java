package ir.netpick.scrape.scrapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ir.netpick.scrape.models.ApiKey;
import ir.netpick.scrape.models.SearchQuery;
import ir.netpick.scrape.models.ScrapeJob;
import ir.netpick.scrape.repositories.ApiKeyRepository;
import ir.netpick.scrape.repositories.SearchQueryRepository;
import ir.netpick.scrape.repositories.ScrapeJobRepository;
import jakarta.transaction.Transactional;

@Service
public class ApiCaller {

    private final ApiKeyRepository apiKeyRepository;
    private final ScrapeJobRepository scrapeJobRepository;
    private final SearchQueryRepository searchQueryRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public ApiCaller(ApiKeyRepository apiKeyRepository,
            ScrapeJobRepository scrapeJobRepository, SearchQueryRepository searchQueryRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.scrapeJobRepository = scrapeJobRepository;
        this.searchQueryRepository = searchQueryRepository;
    }

    @Transactional
    public void caller() {
        List<ApiKey> keys = apiKeyRepository.findAll();
        List<SearchQuery> searchQueries = searchQueryRepository.findAllBelowTenOrderByLinkCount();

        for (SearchQuery searchQuery : searchQueries) {
            for (int index = 0; index < 15; index++) {
                String sentence = searchQuery.getSentence().replace(' ', '+');

                String uri = keys.getFirst().getApiLink()
                        .replace("<query>", sentence)
                        .replace("<count>", "")
                        .replace("<start_index>", "")
                        .replace("<search_engine_id>", "")
                        .replace("<api_key>", keys.getFirst().getKey());

                ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
                if (response.getStatusCode() == HttpStatus.OK) {
                    linkParser(response.getBody());
                }
                if (keys.getFirst().getPoint() <= 0) {
                    keys.removeFirst();
                }
            }
        }
    }

    public void linkParser(String json) {
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        JsonArray items = root.getAsJsonArray("items");
        List<String> titles = new ArrayList<String>();
        List<String> links = new ArrayList<String>();
        for (JsonElement element : items) {
            JsonObject item = element.getAsJsonObject();
            titles.add(item.get("title").getAsString());
            links.add(item.get("link").getAsString());
        }
        scrapeJobCreator(links, titles);
    }

    public Integer scrapeJobCreator(List<String> links) {
        int count = 0;
        for (String link : links) {
            if (scrapeJobRepository.existsByLink(link)) {
                break;
            }
            ScrapeJob job = new ScrapeJob(link);
            scrapeJobRepository.save(job);
            count++;
        }
        return count;
    }

    public Integer scrapeJobCreator(List<String> links, List<String> descriptions) {
        int count = 0;
        for (int i = 0; i < links.size(); i++) {
            if (scrapeJobRepository.existsByLink(links.get(i))) {
                break;
            }
            ScrapeJob job = new ScrapeJob(links.get(i), descriptions.get(1));
            scrapeJobRepository.save(job);
            count++;
        }
        for (String link : links) {
            System.out.println(link);
        }
        return count;
    }
}
