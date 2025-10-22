package ir.netpick.scrape.scrapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Service
public class LinkParser {

    private static final Logger log = LogManager.getLogger(LinkParser.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Data
    public static class LinkResult {
        private String title;
        private String link;
        private String snippet;
    }

    public static List<LinkResult> parse(String jsonBody) {
        List<LinkResult> results = new ArrayList<>();

        if (jsonBody == null || jsonBody.isBlank()) {
            log.warn("Empty or null Google API response");
            return results;
        }

        try {
            JsonNode root = objectMapper.readTree(jsonBody);

            if (!root.has("items") || !root.get("items").isArray()) {
                log.info("No 'items' field found in Google response");
                return results;
            }

            for (JsonNode item : root.get("items")) {
                if (item == null || item.isNull())
                    continue;

                String link = getSafeText(item, "link");
                String title = getSafeText(item, "title");
                String snippet = getSafeText(item, "snippet");

                if (link != null && !link.isBlank()) {
                    LinkResult result = new LinkResult();
                    result.setLink(link);
                    result.setTitle(title);
                    result.setSnippet(snippet);
                    results.add(result);
                }
            }

        } catch (Exception e) {
            log.error("Failed to parse Google API response: {}", e.getMessage());
        }

        return results;
    }

    private static String getSafeText(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() ? node.get(field).asText() : null;
    }

}
