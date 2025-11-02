package ir.netpick.mailmine.scrape.model;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import ir.netpick.mailmine.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "search_query", indexes = {
        @Index(name = "idx_searchquery_sentence", columnList = "sentence")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uniq_searchquery_sentence", columnNames = { "sentence" })
})
public class SearchQuery extends BaseEntity {

    @Column(name = "sentence", nullable = false)
    private String sentence;

    @Column(name = "link_count", nullable = false)
    private Integer link_count;

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

}
