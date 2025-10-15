package ir.netpick.scrape.models;

public record ApiKeyRequest(
                String key,
                Integer point,
                String apiLink,
                String description) {

}
