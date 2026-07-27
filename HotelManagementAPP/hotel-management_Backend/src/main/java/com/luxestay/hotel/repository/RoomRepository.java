package com.luxestay.hotel.repository;

import com.luxestay.hotel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomNumber(String roomNumber);

    List<Room> findByStatus(Room.RoomStatus status);

    List<Room> findByRoomType(Room.RoomType roomType);

    long countByStatus(Room.RoomStatus status);

    @Query("""
        SELECT r.roomNumber FROM Room r
        WHERE r.status = 'AVAILABLE'
          AND (:roomType IS NULL OR r.roomType = :roomType)
          AND r.id NOT IN (
            SELECT res.room.id FROM Reservation res
            WHERE res.status NOT IN ('CANCELLED', 'CHECKED_OUT', 'NO_SHOW')
              AND res.checkInDate  < :checkOut
              AND res.checkOutDate > :checkIn
          )
    """)
    List<String> findAvailableRooms(
            @Param("checkIn")   LocalDate checkIn,
            @Param("checkOut")  LocalDate checkOut,
            @Param("roomType")  String roomType);
}