package ir.netpick.mailmine.scrape.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ir.netpick.mailmine.scrape.model.ScrapeJob;

@Repository
public interface ScrapeJobRepository extends JpaRepository<ScrapeJob, UUID> {
    boolean existsByLink(String link);

    Set<ScrapeJob> findAllByLinkIn(List<String> links);

    Optional<ScrapeJob> findByLink(String link);

    List<ScrapeJob> findByAttemptLessThanEqual(int attempt);
}
