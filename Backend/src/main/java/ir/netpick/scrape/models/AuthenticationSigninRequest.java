package ir.netpick.scrape.models;

public record AuthenticationSigninRequest(
    String email,
    String password
) {

}
