package ir.netpick.scrape.models;

import java.time.LocalDateTime;
import java.util.UUID;

import ir.netpick.scrape.enums.RoleEnum;

public record UserDTO(
        UUID id,
        String email,
        String name,
        RoleEnum role,
        LocalDateTime created_at,
        LocalDateTime updatedAt,
        LocalDateTime lastLoginAt

) {

}
