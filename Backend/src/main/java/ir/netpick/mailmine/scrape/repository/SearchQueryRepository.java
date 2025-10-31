package ir.netpick.mailmine.scrape.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ir.netpick.mailmine.scrape.model.SearchQuery;

@Repository
public interface SearchQueryRepository extends JpaRepository<SearchQuery, UUID> {
    @Query(value = "Select * FROM search_query q WHERE q.link_count <= 10 ORDER BY link_count", nativeQuery = true)
    List<SearchQuery> findAllBelowTenOrderByLinkCount();
}
