package ir.netpick.mailmine.scrape.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "contact_emails", joinColumns = @JoinColumn(name = "contact_id"))
    @Column(name = "email")
    private Set<String> emails = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "contact_phones", joinColumns = @JoinColumn(name = "contact_id"))
    @Column(name = "phone_number")
    private Set<String> phoneNumbers = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "contact_linkedin", joinColumns = @JoinColumn(name = "contact_id"))
    @Column(name = "linkedin_url")
    private Set<String> linkedInUrls = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "contact_twitter", joinColumns = @JoinColumn(name = "contact_id"))
    @Column(name = "twitter_handle")
    private Set<String> twitterHandles = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "contact_github", joinColumns = @JoinColumn(name = "contact_id"))
    @Column(name = "github_profile")
    private Set<String> githubProfiles = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "contact_names", joinColumns = @JoinColumn(name = "contact_id"))
    @Column(name = "name")
    private Set<String> names = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrape_data_id")
    private ScrapeData scrapeData;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Contact() {
    }

    public Contact(ScrapeData scrapeData) {
        this.scrapeData = scrapeData;
    }

    public UUID getId() {
        return id;
    }

    public Set<String> getEmails() {
        return emails;
    }

    public void setEmails(Set<String> emails) {
        this.emails = emails;
    }

    public Set<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Set<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public Set<String> getLinkedInUrls() {
        return linkedInUrls;
    }

    public void setLinkedInUrls(Set<String> linkedInUrls) {
        this.linkedInUrls = linkedInUrls;
    }

    public Set<String> getTwitterHandles() {
        return twitterHandles;
    }

    public void setTwitterHandles(Set<String> twitterHandles) {
        this.twitterHandles = twitterHandles;
    }

    public Set<String> getGithubProfiles() {
        return githubProfiles;
    }

    public void setGithubProfiles(Set<String> githubProfiles) {
        this.githubProfiles = githubProfiles;
    }

    public Set<String> getNames() {
        return names;
    }

    public void setNames(Set<String> names) {
        this.names = names;
    }

    public ScrapeData getScrapeData() {
        return scrapeData;
    }

    public void setScrapeData(ScrapeData scrapeData) {
        this.scrapeData = scrapeData;
    }

    public boolean hasContactInfo() {
        return !(emails.isEmpty() && phoneNumbers.isEmpty() &&
                linkedInUrls.isEmpty() && twitterHandles.isEmpty() &&
                githubProfiles.isEmpty() && names.isEmpty());
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", emails=" + emails +
                ", phones=" + phoneNumbers +
                ", linkedIn=" + linkedInUrls +
                ", twitter=" + twitterHandles +
                ", github=" + githubProfiles +
                ", names=" + names +
                '}';
    }
}
