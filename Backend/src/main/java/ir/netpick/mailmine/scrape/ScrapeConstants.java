package ir.netpick.mailmine.scrape;

import java.util.regex.Pattern;

public class ScrapeConstants {
    public static final int MAX_ATTEMPTS = 3;
    public static final int PAGE_LOAD_TIMEOUT_SECONDS = 10;

    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b",
            Pattern.CASE_INSENSITIVE);
    public static final Pattern PHONE_PATTERN = Pattern.compile("\\+?[0-9][0-9\\s().\\-]{6,}[0-9]");
    public static final Pattern LINKEDIN_PATTERN = Pattern.compile("(?i)linkedin\\.com/(?:in|pub)/[a-zA-Z0-9\\-_/]+");
    public static final Pattern TWITTER_PATTERN = Pattern.compile("(?i)twitter\\.com/([a-zA-Z0-9_]{1,15})");
    public static final Pattern GITHUB_PATTERN = Pattern.compile("(?i)github\\.com/([a-zA-Z0-9\\-_]+)");
    public static final Pattern NAME_PATTERN = Pattern.compile("\\b([A-Z][a-z]+(?:\\s[A-Z][a-z]+){1,3})\\b");

}
