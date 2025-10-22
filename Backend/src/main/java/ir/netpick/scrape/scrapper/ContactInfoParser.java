// package ir.netpick.scrape.scrapper;

// import com.fasterxml.jackson.databind.JsonNode;
// import org.apache.commons.text.StringEscapeUtils;

// import java.util.*;
// import java.util.regex.Matcher;
// import java.util.regex.Pattern;

// public class ContactInfoParser {

// // ============ Patterns ============
// private static final Pattern EMAIL_PATTERN = Pattern
// .compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b",
// Pattern.CASE_INSENSITIVE);
// private static final Pattern PHONE_PATTERN =
// Pattern.compile("\\+?[0-9][0-9\\s().\\-]{6,}[0-9]");
// private static final Pattern LINKEDIN_PATTERN =
// Pattern.compile("(?i)linkedin\\.com/(?:in|pub)/[a-zA-Z0-9\\-_/]+");
// private static final Pattern TWITTER_PATTERN =
// Pattern.compile("(?i)twitter\\.com/([a-zA-Z0-9_]{1,15})");
// private static final Pattern GITHUB_PATTERN =
// Pattern.compile("(?i)github\\.com/([a-zA-Z0-9\\-_]+)");
// private static final Pattern GENERIC_CONTACT_PATTERN = Pattern
// .compile("(?i)(?:email|mail|phone|tel|contact)[:\\s]+([\\w@+\\-().\\s]{1,80})");
// private static final Pattern MAILTO_PATTERN = Pattern
// .compile("mailto:([A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})",
// Pattern.CASE_INSENSITIVE);
// private static final Pattern TEL_PATTERN =
// Pattern.compile("tel:([0-9+\\-()\\s]+)", Pattern.CASE_INSENSITIVE);
// private static final Pattern NAME_PATTERN =
// Pattern.compile("\\b([A-Z][a-z]+(?:\\s[A-Z][a-z]+){1,3})\\b");

// // ============ Result Model ============
// public static class ContactExtractionResult {
// public Set<String> emails = new HashSet<>();
// public Set<String> phoneNumbers = new HashSet<>();
// public Set<String> linkedInUrls = new HashSet<>();
// public Set<String> twitterHandles = new HashSet<>();
// public Set<String> githubProfiles = new HashSet<>();
// public Set<String> names = new HashSet<>();
// public double confidenceScore;

// public boolean hasContactInfo() {
// return !(emails.isEmpty() && phoneNumbers.isEmpty() &&
// linkedInUrls.isEmpty() && twitterHandles.isEmpty() &&
// githubProfiles.isEmpty() && names.isEmpty());
// }

// @Override
// public String toString() {
// return "ContactExtractionResult{" +
// "emails=" + emails +
// ", phones=" + phoneNumbers +
// ", linkedIn=" + linkedInUrls +
// ", twitter=" + twitterHandles +
// ", github=" + githubProfiles +
// ", names=" + names +
// ", confidence=" + confidenceScore +
// '}';
// }
// }

// // ============ Main Parser ============
// public static ContactExtractionResult parse(String url, JsonNode parsedData,
// String htmlContent) {
// ContactExtractionResult result = new ContactExtractionResult();

// if (parsedData != null && !parsedData.isNull())
// extractFromJson(result, parsedData);
// if (htmlContent != null && !htmlContent.isEmpty())
// extractFromHtml(result, htmlContent);

// result.confidenceScore = calculateConfidence(result);
// return result;
// }

// // ============ JSON Extraction ============
// private static void extractFromJson(ContactExtractionResult result, JsonNode
// node) {
// if (node == null)
// return;

// if (node.isObject()) {
// node.fields().forEachRemaining(entry -> {
// String key = entry.getKey().toLowerCase();
// JsonNode value = entry.getValue();

// if (value.isTextual()) {
// extractContactInfoFromText(result, value.asText(), key);
// } else {
// extractFromJson(result, value);
// }
// });
// } else if (node.isArray()) {
// node.forEach(v -> extractFromJson(result, v));
// }
// }

// // ============ HTML Extraction ============
// private static void extractFromHtml(ContactExtractionResult result, String
// htmlRaw) {
// String html = StringEscapeUtils.unescapeHtml4(htmlRaw.replaceAll("<[^>]+>", "
// "));

// scanPattern(result.emails, EMAIL_PATTERN, html, s -> s.toLowerCase().trim());
// scanPattern(result.emails, MAILTO_PATTERN, html, s ->
// s.toLowerCase().trim());
// scanPattern(result.phoneNumbers, TEL_PATTERN, html,
// ContactInfoParser::normalizePhone);
// scanPattern(result.phoneNumbers, PHONE_PATTERN, html,
// ContactInfoParser::normalizePhone);
// scanPattern(result.linkedInUrls, LINKEDIN_PATTERN, html,
// String::toLowerCase);
// scanPattern(result.twitterHandles, TWITTER_PATTERN, html, h -> "@" +
// h.toLowerCase());
// scanPattern(result.githubProfiles, GITHUB_PATTERN, html, p -> "github.com/" +
// p.toLowerCase());
// scanPattern(result.names, NAME_PATTERN, html, String::trim);

// Matcher contactMatcher = GENERIC_CONTACT_PATTERN.matcher(html);
// while (contactMatcher.find())
// extractContactInfoFromText(result, contactMatcher.group(1), "");
// }

// private static void scanPattern(Set<String> target, Pattern pattern, String
// text,
// java.util.function.Function<String, String> mapper) {
// Matcher m = pattern.matcher(text);
// while (m.find()) {
// String val = m.group(1) != null ? m.group(1) : m.group();
// target.add(mapper.apply(val));
// }
// }

// private static void extractContactInfoFromText(ContactExtractionResult
// result, String text, String field) {
// if (text == null || text.isBlank())
// return;
// String lower = field.toLowerCase();

// if (lower.contains("email"))
// result.emails.add(text.toLowerCase().trim());
// if (lower.contains("phone") || lower.contains("tel"))
// result.phoneNumbers.add(normalizePhone(text));
// if (lower.contains("linkedin"))
// result.linkedInUrls.add(text.trim().toLowerCase());
// if (lower.contains("twitter"))
// result.twitterHandles.add("@" + text.replaceAll("@", "").toLowerCase());
// if (lower.contains("github"))
// result.githubProfiles.add("github.com/" + text.toLowerCase());
// if (lower.contains("name") || lower.contains("author"))
// result.names.add(text.trim());

// // Regex fallback
// scanPattern(result.emails, EMAIL_PATTERN, text, s -> s.toLowerCase().trim());
// scanPattern(result.phoneNumbers, PHONE_PATTERN, text,
// ContactInfoParser::normalizePhone);
// }

// private static String normalizePhone(String phone) {
// if (phone == null)
// return "";
// String cleaned = phone.replaceAll("[^\\d+]", "");
// if (cleaned.startsWith("00"))
// cleaned = "+" + cleaned.substring(2);
// return cleaned;
// }

// private static double calculateConfidence(ContactExtractionResult r) {
// double score = 0;
// score += Math.min(r.emails.size() * 20, 40);
// score += Math.min(r.phoneNumbers.size() * 15, 30);
// score += Math.min(r.linkedInUrls.size() * 10, 15);
// score += Math.min(r.twitterHandles.size() * 5, 10);
// score += Math.min(r.githubProfiles.size() * 5, 10);
// score += Math.min(r.names.size() * 3, 5);
// return Math.min(score, 100);
// }
// }
