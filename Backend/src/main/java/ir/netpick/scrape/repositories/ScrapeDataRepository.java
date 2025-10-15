package ir.netpick.scrape.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ir.netpick.scrape.models.ScrapeData;

@Repository
public interface ScrapeDataRepository extends JpaRepository<ScrapeData, UUID> {

}
