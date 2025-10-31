package ir.netpick.mailmine.scrape.dto;

public record ApiKeyRequest(
                String key,
                Integer point,
                String searchEngineId,
                String apiLink,
                String description) {

}
