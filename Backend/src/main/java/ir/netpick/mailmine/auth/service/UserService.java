package ir.netpick.mailmine.auth.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ir.netpick.mailmine.auth.dto.AllUsersResponse;
import ir.netpick.mailmine.auth.dto.AuthenticationSignupRequest;
import ir.netpick.mailmine.auth.dto.UserDTO;
import ir.netpick.mailmine.auth.mapper.UserDTOMapper;
import ir.netpick.mailmine.auth.model.Role;
import ir.netpick.mailmine.auth.model.User;
import ir.netpick.mailmine.auth.repository.RoleRepository;
import ir.netpick.mailmine.auth.repository.UserRepository;
import ir.netpick.mailmine.common.enums.RoleEnum;
import ir.netpick.mailmine.common.exception.DuplicateResourceException;
import ir.netpick.mailmine.common.exception.RequestValidationException;
import ir.netpick.mailmine.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserDTOMapper userDTOMapper;

    @Value("${env.page-size:10}")
    private int pageSize;

    @Transactional
    public void updateLastSign(String email) {
        userRepository.updateLastLogin(LocalDateTime.now(), email);
    }

    public boolean isEmailValidation(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }

    public boolean isRegisterRequestValid(AuthenticationSignupRequest request) {
        String email = request.email();
        if (userRepository.existsUserByEmail(email)) {
            throw new DuplicateResourceException("A User with this email already exists.");
        }
        if (!isEmailValidation(email)) {
            throw new RequestValidationException("Email is not Valid.");
        }
        if (request.password() == null ||
                request.name() == null) {
            throw new RequestValidationException("There is an empty parameter.");
        }
        return true;
    }

    public User createAdministrator(AuthenticationSignupRequest request) {

        if (!isRegisterRequestValid(request)) {
            throw new RequestValidationException("Request is not valid!");
        }

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);

        if (optionalRole.isEmpty()) {
            return null;
        }

        User user = new User(request.email(),
                passwordEncoder.encode(request.password()),
                request.name(),
                optionalRole.get());

        return userRepository.save(user);
    }

    public User createUser(AuthenticationSignupRequest request) {

        if (!isRegisterRequestValid(request)) {
            throw new RequestValidationException("Request is not valid!");
        }

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        if (optionalRole.isEmpty()) {
            return null;
        }

        User user = new User(request.email(),
                passwordEncoder.encode(request.password()),
                request.name(),
                optionalRole.get());

        return userRepository.save(user);
    }

    public void updateUser() {
    };

    public UserDTO getUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User with email [%s] was not found!".formatted(email)));
        UserDTO userDTO = userDTOMapper.apply(user);
        return userDTO;
    };

    public AllUsersResponse allUsers(Integer pageNumber) {
        Page<User> page = userRepository
                .findAll(PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.ASC, "createdAt")));

        return new AllUsersResponse(
                page.getContent()
                        .stream()
                        .map(userDTOMapper)
                        .collect(Collectors.toList()),
                page.getTotalPages(),
                page.getNumber() + 1);
    };

    public AllUsersResponse allUsers(Integer pageNumber, String sortBy, Direction direction) {
        Page<User> page = userRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)));

        return new AllUsersResponse(
                page.getContent()
                        .stream()
                        .map(userDTOMapper)
                        .collect(Collectors.toList()),
                page.getTotalPages(),
                page.getNumber() + 1);
    };

    public void deleteUser() {
    };
}
