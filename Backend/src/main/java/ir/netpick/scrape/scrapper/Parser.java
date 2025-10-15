// package ir.netpick.scrape.scrapper;

// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import java.util.*;
// import java.util.regex.Matcher;
// import java.util.regex.Pattern;

// /**
//  * Improved version of ContactInfoParser
//  * - Uses Sets for uniqueness
//  * - Global phone regex
//  * - Fixed confidence score scaling
//  * - Avoids redundant regex passes
//  * - Avoids double-recursion on known contact fields
//  */
// public class ContactInfoParser {

//     private static final ObjectMapper objectMapper = new ObjectMapper();

//     // =============================
//     // Regex Patterns
//     // =============================

//     private static final Pattern EMAIL_PATTERN = Pattern.compile(
//             "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b",
//             Pattern.CASE_INSENSITIVE
//     );

//     // International-friendly phone pattern (e.g. +44 123 456 789, (555) 123-4567, etc.)
//     private static final Pattern PHONE_PATTERN = Pattern.compile(
//             "\\+?[0-9][0-9\\s().\\-]{6,}[0-9]"
//     );

//     private static final Pattern LINKEDIN_PATTERN = Pattern.compile(
//             "(?i)linkedin\\.com/(?:in|pub)/[a-zA-Z0-9\\-_/]+"
//     );

//     private static final Pattern TWITTER_PATTERN = Pattern.compile(
//             "(?i)twitter\\.com/([a-zA-Z0-9_]{1,15})"
//     );

//     private static final Pattern GITHUB_PATTERN = Pattern.compile(
//             "(?i)github\\.com/([a-zA-Z0-9\\-_]+)"
//     );

//     // Additional weak-contact pattern (kept minimal to avoid noise)
//     private static final Pattern GENERIC_CONTACT_PATTERN = Pattern.compile(
//             "(?i)(?:email|mail|phone|tel|contact)[:\\s]+([\\w@+\\-().\\s]+)"
//     );

//     // =============================
//     // Result Model
//     // =============================

//     public static class ContactExtractionResult {
//         private Set<String> emails = new HashSet<>();
//         private Set<String> phoneNumbers = new HashSet<>();
//         private Set<String> linkedInUrls = new HashSet<>();
//         private Set<String> twitterHandles = new HashSet<>();
//         private Set<String> githubProfiles = new HashSet<>();
//         private Set<String> names = new HashSet<>();
//         private Map<String, Object> additionalData = new HashMap<>();
//         private double confidenceScore;

//         public Set<String> getEmails() { return emails; }
//         public Set<String> getPhoneNumbers() { return phoneNumbers; }
//         public Set<String> getLinkedInUrls() { return linkedInUrls; }
//         public Set<String> getTwitterHandles() { return twitterHandles; }
//         public Set<String> getGithubProfiles() { return githubProfiles; }
//         public Set<String> getNames() { return names; }
//         public Map<String, Object> getAdditionalData() { return additionalData; }
//         public double getConfidenceScore() { return confidenceScore; }

//         public void setConfidenceScore(double confidenceScore) { this.confidenceScore = confidenceScore; }

//         public boolean hasContactInfo() {
//             return !(emails.isEmpty() && phoneNumbers.isEmpty()
//                     && linkedInUrls.isEmpty() && twitterHandles.isEmpty()
//                     && githubProfiles.isEmpty() && names.isEmpty());
//         }

//         @Override
//         public String toString() {
//             return "ContactExtractionResult{" +
//                     "emails=" + emails +
//                     ", phoneNumbers=" + phoneNumbers +
//                     ", linkedInUrls=" + linkedInUrls +
//                     ", twitterHandles=" + twitterHandles +
//                     ", githubProfiles=" + githubProfiles +
//                     ", names=" + names +
//                     ", confidenceScore=" + confidenceScore +
//                     '}';
//         }
//     }

//     // =============================
//     // Main Parsing Method
//     // =============================

//     public static ContactExtractionResult parseContactInfoFromScrapeAttempt(
//             String scrapeAttemptId,
//             String url,
//             JsonNode parsedData,
//             String htmlContent) {

//         ContactExtractionResult result = new ContactExtractionResult();

//         if (parsedData != null && !parsedData.isNull()) {
//             extractFromParsedData(result, parsedData);
//         }

//         if (htmlContent != null && !htmlContent.isEmpty()) {
//             extractFromHtml(result, htmlContent);
//         }

//         result.setConfidenceScore(calculateConfidenceScore(result));
//         return result;
//     }

//     // =============================
//     // JSON Data Extraction
//     // =============================

//     private static void extractFromParsedData(ContactExtractionResult result, JsonNode parsedData) {
//         List<String> contactFields = Arrays.asList(
//                 "email", "emails", "phone", "phone_number", "telephone", "tel",
//                 "contact", "contact_info", "contact_email", "contact_phone",
//                 "linkedin", "twitter", "github", "social", "social_media",
//                 "name", "full_name", "first_name", "last_name", "title",
//                 "author", "author_email", "author_contact"
//         );

//         searchJsonForContactInfo(parsedData, contactFields, result);
//     }

//     private static void searchJsonForContactInfo(JsonNode node, List<String> contactFields, ContactExtractionResult result) {
//         if (node == null) return;

//         if (node.isObject()) {
//             Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
//             while (fields.hasNext()) {
//                 Map.Entry<String, JsonNode> field = fields.next();
//                 String fieldName = field.getKey().toLowerCase();

//                 if (contactFields.contains(fieldName)) {
//                     extractValue(result, field.getValue(), fieldName);
//                 } else {
//                     searchJsonForContactInfo(field.getValue(), contactFields, result);
//                 }
//             }
//         } else if (node.isArray()) {
//             for (JsonNode element : node) {
//                 searchJsonForContactInfo(element, contactFields, result);
//             }
//         }
//     }

//     private static void extractValue(ContactExtractionResult result, JsonNode value, String fieldName) {
//         if (value.isTextual()) {
//             extractContactInfoFromText(result, value.asText(), fieldName);
//         } else if (value.isArray()) {
//             for (JsonNode v : value) {
//                 if (v.isTextual()) extractContactInfoFromText(result, v.asText(), fieldName);
//             }
//         }
//     }

//     // =============================
//     // HTML Extraction
//     // =============================

//     private static void extractFromHtml(ContactExtractionResult result, String html) {
//         // Emails
//         Matcher emailMatcher = EMAIL_PATTERN.matcher(html);
//         while (emailMatcher.find()) {
//             result.emails.add(emailMatcher.group().toLowerCase().trim());
//         }

//         // Phones
//         Matcher phoneMatcher = PHONE_PATTERN.matcher(html);
//         while (phoneMatcher.find()) {
//             String formatted = phoneMatcher.group().replaceAll("[^\\d+]", "");
//             if (formatted.length() >= 7) result.phoneNumbers.add(formatted);
//         }

//         // LinkedIn
//         Matcher linkedinMatcher = LINKEDIN_PATTERN.matcher(html);
//         while (linkedinMatcher.find()) {
//             result.linkedInUrls.add(linkedinMatcher.group().toLowerCase().trim());
//         }

//         // Twitter
//         Matcher twitterMatcher = TWITTER_PATTERN.matcher(html);
//         while (twitterMatcher.find()) {
//             result.twitterHandles.add("@" + twitterMatcher.group(1).toLowerCase());
//         }

//         // GitHub
//         Matcher githubMatcher = GITHUB_PATTERN.matcher(html);
//         while (githubMatcher.find()) {
//             result.githubProfiles.add("github.com/" + githubMatcher.group(1).toLowerCase());
//         }

//         // Generic patterns
//         Matcher contactMatcher = GENERIC_CONTACT_PATTERN.matcher(html);
//         while (contactMatcher.find()) {
//             extractContactInfoFromText(result, contactMatcher.group(1), "");
//         }
//     }

//     // =============================
//     // Text Extraction Helper
//     // =============================

//     private static void extractContactInfoFromText(ContactExtractionResult result, String text, String fieldName) {
//         if (text == null || text.isEmpty()) return;

//         // Emails
//         Matcher emailMatcher = EMAIL_PATTERN.matcher(text);
//         while (emailMatcher.find()) {
//             result.emails.add(emailMatcher.group().toLowerCase().trim());
//         }

//         // Phones
//         Matcher phoneMatcher = PHONE_PATTERN.matcher(text);
//         while (phoneMatcher.find()) {
//             String phone = phoneMatcher.group().replaceAll("[^\\d+]", "");
//             if (phone.length() >= 7) result.phoneNumbers.add(phone);
//         }

//         // Field-based hints
//         if (fieldName.contains("email")) {
//             result.emails.add(text.trim().toLowerCase());
//         } else if (fieldName.contains("phone") || fieldName.contains("tel")) {
//             result.phoneNumbers.add(text.replaceAll("[^\\d+]", ""));
//         }
//     }

//     // =============================
//     // Confidence Scoring
//     // =============================

//     private static double calculateConfidenceScore(ContactExtractionResult result) {
//         int score = 0;
//         score += result.emails.size() * 30;
//         score += result.phoneNumbers.size() * 25;
//         score += result.linkedInUrls.size() * 15;
//         score += result.twitterHandles.size() * 10;
//         score += result.githubProfiles.size() * 12;
//         score += result.names.size() * 5;

//         // Cap to 100
//         return Math.min(100.0, score);
//     }

//     // =============================
//     // Example Main
//     // =============================

//     public static void main(String[] args) {
//         try {
//             String html = """
//                 <div>
//                     <p>Contact us at: john.doe@example.com or +44 7700 900123</p>
//                     <p>LinkedIn: https://linkedin.com/in/johndoe</p>
//                     <p>Twitter: https://twitter.com/johndoe</p>
//                     <p>GitHub: https://github.com/johndoe</p>
//                 </div>
//             """;

//             JsonNode parsed = objectMapper.readTree("""
//                 {
//                     "author": "John Doe",
//                     "email": "john.doe@example.com",
//                     "phone": "+1 (555) 123-4567",
//                     "social": {
//                         "linkedin": "https://linkedin.com/in/johndoe",
//                         "twitter": "@johndoe"
//                     }
//                 }
//             """);

//             ContactExtractionResult result = parseContactInfoFromScrapeAttempt(
//                     "test-id",
//                     "https://example.com",
//                     parsed,
//                     html
//             );

//             System.out.println(result);
//             System.out.println("Has contact info: " + result.hasContactInfo());
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }
