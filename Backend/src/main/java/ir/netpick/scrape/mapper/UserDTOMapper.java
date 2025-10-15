package ir.netpick.scrape.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import ir.netpick.scrape.models.User;
import ir.netpick.scrape.models.UserDTO;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {

    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().getName(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getLastLoginAt());
    }
}
