package ir.netpick.scrape.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "scrape_data", indexes = {
        @Index(name = "idx_scrapedata_dataLocation", columnList = "data_location")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uniq_scrapedata_dataLocation", columnNames = { "data_location" })
})
public class ScrapeData {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "data_location", nullable = false)
    private String location;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "job_id", referencedColumnName = "id", nullable = false)
    private ScrapeJob scrapeJob;

    @Column(name = "Parsed")
    private boolean Parsed;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public ScrapeData(String location, ScrapeJob scrapeJob) {
        this.location = location;
        this.scrapeJob = scrapeJob;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ScrapeJob getScrapeJob() {
        return scrapeJob;
    }

    public void setScrapeJob(ScrapeJob scrapeJob) {
        this.scrapeJob = scrapeJob;
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

    public boolean isParsed() {
        return Parsed;
    }

    public void setParsed(boolean parsed) {
        Parsed = parsed;
    }

}
