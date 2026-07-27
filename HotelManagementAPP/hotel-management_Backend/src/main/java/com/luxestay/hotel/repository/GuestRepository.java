package com.luxestay.hotel.repository;

import com.luxestay.hotel.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    Optional<Guest> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
        SELECT g FROM Guest g
        WHERE LOWER(g.firstName) LIKE %:q%
           OR LOWER(g.lastName)  LIKE %:q%
           OR LOWER(g.email)     LIKE %:q%
           OR g.phone            LIKE %:q%
    """)
    List<Guest> searchGuests(@Param("q") String query);

    List<Guest> findByLoyaltyTier(Guest.LoyaltyTier tier);
}