package ir.netpick.scrape.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ir.netpick.scrape.models.ScrapeJob;

@Repository
public interface ScrapeJobRepository extends JpaRepository<ScrapeJob, UUID> {
    boolean existsByLink(String link);
}
