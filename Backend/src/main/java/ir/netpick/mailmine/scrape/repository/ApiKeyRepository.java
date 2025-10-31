package ir.netpick.mailmine.scrape.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ir.netpick.mailmine.scrape.model.ApiKey;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {

}
