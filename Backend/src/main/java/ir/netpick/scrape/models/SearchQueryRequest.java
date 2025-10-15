package ir.netpick.scrape.models;

public record SearchQueryRequest(
        String sentence,
        int link_count,
        String description
        ) {

}
