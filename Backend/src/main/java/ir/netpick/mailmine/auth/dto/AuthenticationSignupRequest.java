package ir.netpick.mailmine.auth.dto;

public record AuthenticationSignupRequest(
                String email,
                String password,
                String name) {

}
