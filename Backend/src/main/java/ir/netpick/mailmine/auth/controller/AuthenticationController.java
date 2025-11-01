package ir.netpick.mailmine.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ir.netpick.mailmine.auth.dto.AuthenticationSigninRequest;
import ir.netpick.mailmine.auth.dto.AuthenticationSignupRequest;
import ir.netpick.mailmine.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("sign-up")
    public ResponseEntity<?> signup(@RequestBody AuthenticationSignupRequest request) {
        String jwtToken = authenticationService.signup(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();
    }

    @PostMapping("sign-in")
    public ResponseEntity<?> signin(@RequestBody AuthenticationSigninRequest request) {
        String jwtToken = authenticationService.signin(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();
    }

}
