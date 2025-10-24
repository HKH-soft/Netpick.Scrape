package ir.netpick.scrape.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "search_query", indexes = {
        @Index(name = "idx_searchquery_sentence", columnList = "sentence")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uniq_searchquery_sentence", columnNames = { "sentence" })
})
public class SearchQuery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "sentence", nullable = false)
    private String sentence;

    @Column(name = "link_count", nullable = false)
    private Integer link_count;

    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public SearchQuery() {
    }

    public SearchQuery(String sentence, Integer link_count) {
        this.sentence = sentence;
        this.link_count = link_count;
    }

    public SearchQuery(String sentence, Integer link_count, String description) {
        this.sentence = sentence;
        this.link_count = link_count;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public Integer getLink_count() {
        return link_count;
    }

    public void setLink_count(Integer link_count) {
        this.link_count = link_count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
