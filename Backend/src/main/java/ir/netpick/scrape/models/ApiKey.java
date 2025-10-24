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
@Table(name = "api_key", indexes = {
        @Index(name = "idx_apikey_key", columnList = "key")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uniq_apikey_key", columnNames = { "key" })
})
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "point_left")
    private Integer point;

    @Column(name = "link_id", nullable = false)
    private String apiLink;

    @Column(name = "search_engine_id", nullable = false)
    private String searchEngineId;

    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public ApiKey(String key, Integer point, String apiLink, String searchEngineId) {
        this.key = key;
        this.point = point;
        this.apiLink = apiLink;
        this.searchEngineId = searchEngineId;
    }

    public ApiKey(String key, Integer point, String apiLink, String searchEngineId, String description) {
        this.key = key;
        this.point = point;
        this.apiLink = apiLink;
        this.searchEngineId = searchEngineId;
        this.description = description;
    }

    public ApiKey() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getString() {
        return apiLink;
    }

    public void setString(String apiLink) {
        this.apiLink = apiLink;
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

    public String getApiLink() {
        return apiLink;
    }

    public void setApiLink(String apiLink) {
        this.apiLink = apiLink;
    }

    public String getSearchEngineId() {
        return searchEngineId;
    }

    public void setSearchEngineId(String searchEngineId) {
        this.searchEngineId = searchEngineId;
    }

}
