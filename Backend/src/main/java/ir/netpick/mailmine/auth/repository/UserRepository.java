package ir.netpick.mailmine.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ir.netpick.mailmine.auth.model.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);

    @Modifying()
    @Query("UPDATE User u SET u.lastLoginAt = :now WHERE u.email = :email")
    void updateLastLogin(@Param("now") LocalDateTime now, @Param("email") String email);

}
