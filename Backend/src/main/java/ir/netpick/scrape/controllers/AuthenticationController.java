package ir.netpick.scrape.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ir.netpick.scrape.models.AuthenticationSigninRequest;
import ir.netpick.scrape.models.AuthenticationSignupRequest;
import ir.netpick.scrape.services.AuthenticationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

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
