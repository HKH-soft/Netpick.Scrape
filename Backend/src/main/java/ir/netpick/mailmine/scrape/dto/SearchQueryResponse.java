package ir.netpick.mailmine.scrape.dto;

import java.time.LocalDateTime;

public record SearchQueryResponse(
        String sentence,
        int link_count,
        String description,
        LocalDateTime created_at,
        LocalDateTime updatedAt) {

}
