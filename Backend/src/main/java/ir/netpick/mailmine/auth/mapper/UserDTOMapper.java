package ir.netpick.mailmine.auth.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import ir.netpick.mailmine.auth.dto.UserDTO;
import ir.netpick.mailmine.auth.model.User;

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
