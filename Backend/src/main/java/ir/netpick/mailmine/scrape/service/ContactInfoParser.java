package ir.netpick.mailmine.scrape.service;

import org.apache.commons.text.StringEscapeUtils;

import ir.netpick.mailmine.scrape.model.Contact;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactInfoParser {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern PHONE_PATTERN = Pattern.compile("\\+?[0-9][0-9\\s().\\-]{6,}[0-9]");
    private static final Pattern LINKEDIN_PATTERN = Pattern.compile("(?i)linkedin\\.com/(?:in|pub)/[a-zA-Z0-9\\-_/]+");
    private static final Pattern TWITTER_PATTERN = Pattern.compile("(?i)twitter\\.com/([a-zA-Z0-9_]{1,15})");
    private static final Pattern GITHUB_PATTERN = Pattern.compile("(?i)github\\.com/([a-zA-Z0-9\\-_]+)");
    private static final Pattern NAME_PATTERN = Pattern.compile("\\b([A-Z][a-z]+(?:\\s[A-Z][a-z]+){1,3})\\b");

    public static Contact parse(String htmlContent) {
        Contact contact = new Contact();

        if (htmlContent == null || htmlContent.isBlank())
            return contact;

        String html = StringEscapeUtils.unescapeHtml4(htmlContent.replaceAll("<[^>]+>", ""));

        extract(html, EMAIL_PATTERN, contact.getEmails(), "");
        extract(html, PHONE_PATTERN, contact.getPhoneNumbers(), "");
        extract(html, LINKEDIN_PATTERN, contact.getLinkedInUrls(), "");
        extract(html, TWITTER_PATTERN, contact.getTwitterHandles(), "@");
        extract(html, GITHUB_PATTERN, contact.getGithubProfiles(), "github.com/");
        extract(html, NAME_PATTERN, contact.getNames(), "");

        return contact;
    }

    private static void extract(String text, Pattern pattern, Set<String> target, String prefix) {
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String val = matcher.group(1) != null ? matcher.group(1) : matcher.group();
            target.add(prefix + val.trim());
        }
    }
}
