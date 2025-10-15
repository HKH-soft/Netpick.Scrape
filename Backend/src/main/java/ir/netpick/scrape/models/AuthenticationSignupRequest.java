package ir.netpick.scrape.models;

public record AuthenticationSignupRequest(
        String email,
        String password,
        String name) {

}
