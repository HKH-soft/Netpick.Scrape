package ir.netpick.mailmine.scrape.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ir.netpick.mailmine.scrape.model.ScrapeData;


@Repository
public interface ScrapeDataRepository extends JpaRepository<ScrapeData, UUID> {
    List<ScrapeData> findByParsedFalse();
}
