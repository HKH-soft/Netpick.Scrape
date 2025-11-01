package ir.netpick.mailmine.scrape.parser;

import org.apache.commons.text.StringEscapeUtils;

import ir.netpick.mailmine.scrape.ScrapeConstants;
import ir.netpick.mailmine.scrape.model.Contact;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactInfoParser {

    public static Contact parse(String htmlContent) {
        Contact contact = new Contact();

        if (htmlContent == null || htmlContent.isBlank())
            return contact;

        String html = StringEscapeUtils.unescapeHtml4(htmlContent.replaceAll("<[^>]+>", ""));

        extract(html, ScrapeConstants.EMAIL_PATTERN, contact.getEmails(), "");
        extract(html, ScrapeConstants.PHONE_PATTERN, contact.getPhoneNumbers(), "");
        extract(html, ScrapeConstants.LINKEDIN_PATTERN, contact.getLinkedInUrls(), "");
        extract(html, ScrapeConstants.TWITTER_PATTERN, contact.getTwitterHandles(), "@");
        extract(html, ScrapeConstants.GITHUB_PATTERN, contact.getGithubProfiles(), "github.com/");
        extract(html, ScrapeConstants.NAME_PATTERN, contact.getNames(), "");

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
