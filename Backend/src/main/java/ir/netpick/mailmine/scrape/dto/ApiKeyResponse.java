package ir.netpick.mailmine.scrape.dto;

import java.time.LocalDateTime;

public record ApiKeyResponse(
                String key,
                Integer point,
                String apiLink,
                String description,
                LocalDateTime created_at,
                LocalDateTime updatedAt) {

}
