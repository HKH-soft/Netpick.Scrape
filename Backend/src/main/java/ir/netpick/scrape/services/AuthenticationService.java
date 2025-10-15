package ir.netpick.scrape.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import ir.netpick.scrape.models.AuthenticationSigninRequest;
import ir.netpick.scrape.models.AuthenticationSignupRequest;
import ir.netpick.scrape.models.User;
import ir.netpick.scrape.models.UserDTO;
import ir.netpick.scrape.mapper.UserDTOMapper;
import ir.netpick.scrape.jwt.JWTUtil;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserDTOMapper userDTOMapper;
    private final UserService userService;

    public AuthenticationService(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
            UserDTOMapper userDTOMapper, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDTOMapper = userDTOMapper;
        this.userService = userService;
    }

    public String signin(AuthenticationSigninRequest request) {
        Authentication authenticationRsponse = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        UserDTO user = userDTOMapper.apply((User) authenticationRsponse.getPrincipal());
        String token = jwtUtil.issueToken(user.email(), user.role().toString());
        userService.updateLastSign(request.email());
        return token;
    }

    public String signup(AuthenticationSignupRequest request) {
        User user = userService.createUser(request);
        String token = jwtUtil.issueToken(request.email(), user.getRole().getName().toString());
        return token;
    }

    public String registerAdmin(AuthenticationSignupRequest request) {
        User user = userService.createAdministrator(request);
        String token = jwtUtil.issueToken(request.email(), user.getRole().getName().toString());
        return token;
    }

}
