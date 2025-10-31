package ir.netpick.mailmine.auth.dto;

public record AuthenticationSigninRequest(
        String email,
        String password) {

}
