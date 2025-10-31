package ir.netpick.mailmine.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ir.netpick.mailmine.auth.dto.AuthenticationSignupRequest;
import ir.netpick.mailmine.auth.service.AuthenticationService;

@RestController
@RequestMapping("/admins")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AdminController {
    private final AuthenticationService authenticationService;

    public AdminController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("create")
    public ResponseEntity<?> createAdministrator(@RequestBody AuthenticationSignupRequest request) {

        String jwtToken = authenticationService.registerAdmin(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();

    }

}
