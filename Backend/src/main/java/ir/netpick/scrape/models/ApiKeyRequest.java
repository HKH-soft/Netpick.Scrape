package ir.netpick.scrape.models;

public record ApiKeyRequest(
        String key,
        Integer point,
        String searchEngineId,
        String apiLink,
        String description) {

}
