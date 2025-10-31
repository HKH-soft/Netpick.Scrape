package ir.netpick.mailmine.auth.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ir.netpick.mailmine.auth.dto.AllUsersResponse;
import ir.netpick.mailmine.auth.dto.UserDTO;
import ir.netpick.mailmine.auth.jwt.JWTUtil;
import ir.netpick.mailmine.auth.service.UserService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    public UserController(UserService userService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("profile")
    public ResponseEntity<UserDTO> getProfile(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        String email = jwtUtil.getSubject(token.substring(7));
        return ResponseEntity.ok()
                .body(userService.getUser(email));
    }

    @GetMapping("users")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<AllUsersResponse> getUsers(@RequestParam(defaultValue = "1") Integer page) {
        return ResponseEntity.ok()
                .body(userService.allUsers(page));
    }

}
