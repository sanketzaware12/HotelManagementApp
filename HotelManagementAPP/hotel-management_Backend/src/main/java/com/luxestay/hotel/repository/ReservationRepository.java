package com.luxestay.hotel.repository;

import com.luxestay.hotel.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByReservationCode(String code);

    List<Reservation> findByGuestId(Long guestId);

    List<Reservation> findByCheckInDateAndStatus(LocalDate date, Reservation.ReservationStatus status);

    List<Reservation> findByCheckOutDateAndStatus(LocalDate date, Reservation.ReservationStatus status);

    @Query("""
        SELECT COUNT(r) > 0 FROM Reservation r
        WHERE r.room.id = :roomId
          AND r.status NOT IN ('CANCELLED', 'CHECKED_OUT', 'NO_SHOW')
          AND r.checkInDate  < :checkOut
          AND r.checkOutDate > :checkIn
    """)
    boolean existsOverlappingReservation(
            @Param("roomId")   Long roomId,
            @Param("checkIn")  LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut);

    @Query("""
        SELECT r FROM Reservation r
        WHERE r.checkInDate BETWEEN :start AND :end
        ORDER BY r.checkInDate ASC
    """)
    List<Reservation> findReservationsInDateRange(
            @Param("start") LocalDate start,
            @Param("end")   LocalDate end);

    @Query("""
        SELECT SUM(r.totalAmount) FROM Reservation r
        WHERE r.status = 'CHECKED_OUT'
          AND r.checkOutDate BETWEEN :start AND :end
    """)
    Double getTotalRevenueInRange(
            @Param("start") LocalDate start,
            @Param("end")   LocalDate end);

    long countByStatus(Reservation.ReservationStatus status);
}