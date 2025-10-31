package ir.netpick.mailmine.scrape.dto;

public record SearchQueryRequest(
        String sentence,
        int link_count,
        String description) {

}
