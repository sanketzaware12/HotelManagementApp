package com.luxestay.hotel.config;

import com.luxestay.hotel.entity.*;
import com.luxestay.hotel.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public void run(String... args) {
        if (roomRepository.count() > 0) return; // already seeded

        // ── Seed Rooms ──────────────────────────────────────────────────────
        List<Room> rooms = List.of(
            room("101", Room.RoomType.STANDARD,     1, 5000.0,  2),
            room("102", Room.RoomType.STANDARD,     1, 5000.0,  2),
            room("103", Room.RoomType.DELUXE,       1, 8000.0,  2),
            room("201", Room.RoomType.DELUXE,       2, 8000.0,  2),
            room("202", Room.RoomType.SUITE,        2, 15000.0, 3),
            room("203", Room.RoomType.STANDARD,     2, 5000.0,  2),
            room("301", Room.RoomType.DELUXE,       3, 8000.0,  2),
            room("302", Room.RoomType.SUITE,        3, 15000.0, 4),
            room("401", Room.RoomType.SUITE,        4, 15000.0, 4),
            room("501", Room.RoomType.PRESIDENTIAL, 5, 25000.0, 6),
            room("502", Room.RoomType.PRESIDENTIAL, 5, 25000.0, 6),
            room("601", Room.RoomType.SUITE,        6, 15000.0, 4)
        );
        roomRepository.saveAll(rooms);

        // ── Seed Guests ──────────────────────────────────────────────────────
        Guest g1 = guestRepository.save(guest("Priya",  "Mehta",  "priya@email.com",  "9876543210", Guest.LoyaltyTier.GOLD));
        Guest g2 = guestRepository.save(guest("James",  "Wilson", "james@email.com",  "9812345678", Guest.LoyaltyTier.PLATINUM));
        Guest g3 = guestRepository.save(guest("Ananya", "Rao",    "ananya@email.com", "9823456789", Guest.LoyaltyTier.SILVER));
        Guest g4 = guestRepository.save(guest("Rahul",  "Singh",  "rahul@email.com",  "9834567890", Guest.LoyaltyTier.STANDARD));

        // ── Seed Reservations ────────────────────────────────────────────────
        Room r301 = roomRepository.findByRoomNumber("301").orElseThrow();
        Room r501 = roomRepository.findByRoomNumber("501").orElseThrow();
        Room r202 = roomRepository.findByRoomNumber("202").orElseThrow();

        reservationRepository.save(reservation("R1001", g1, r301,
                LocalDate.now(), LocalDate.now().plusDays(3),
                Reservation.ReservationStatus.CONFIRMED, 24000.0));

        reservationRepository.save(reservation("R1002", g2, r501,
                LocalDate.now().minusDays(2), LocalDate.now().plusDays(2),
                Reservation.ReservationStatus.CHECKED_IN, 100000.0));

        reservationRepository.save(reservation("R1003", g3, r202,
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(4),
                Reservation.ReservationStatus.CONFIRMED, 45000.0));

        // Mark r501 occupied since g2 is checked in
        r501.setStatus(Room.RoomStatus.OCCUPIED);
        roomRepository.save(r501);

        System.out.println("✦ LuxeStay: Demo data seeded successfully");
    }

    private Room room(String num, Room.RoomType type, int floor, double price, int cap) {
        return Room.builder()
                .roomNumber(num).roomType(type).floor(floor)
                .pricePerNight(price).capacity(cap)
                .status(Room.RoomStatus.AVAILABLE)
                .build();
    }

    private Guest guest(String first, String last, String email, String phone, Guest.LoyaltyTier tier) {
        return Guest.builder()
                .firstName(first).lastName(last)
                .email(email).phone(phone)
                .loyaltyTier(tier).totalStays(0).totalSpent(0.0)
                .build();
    }

    private Reservation reservation(String code, Guest guest, Room room,
                                     LocalDate in, LocalDate out,
                                     Reservation.ReservationStatus status, double amount) {
        return Reservation.builder()
                .reservationCode(code).guest(guest).room(room)
                .checkInDate(in).checkOutDate(out)
                .status(status).totalAmount(amount)
                .numberOfGuests(2)
                .bookingSource(Reservation.BookingSource.DIRECT)
                .build();
    }
}