package com.luxestay.hotel.service;

import com.luxestay.hotel.dto.ReservationRequest;
import com.luxestay.hotel.dto.ReservationResponse;
import com.luxestay.hotel.entity.*;
import com.luxestay.hotel.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ReservationResponse getReservationById(Long id) {
        return toResponse(reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found: " + id)));
    }

    public ReservationResponse getByCode(String code) {
        return toResponse(reservationRepository.findByReservationCode(code)
                .orElseThrow(() -> new RuntimeException("Reservation not found: " + code)));
    }

    public List<ReservationResponse> getTodayCheckIns() {
        return reservationRepository
                .findByCheckInDateAndStatus(LocalDate.now(), Reservation.ReservationStatus.CONFIRMED)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ReservationResponse> getTodayCheckOuts() {
        return reservationRepository
                .findByCheckOutDateAndStatus(LocalDate.now(), Reservation.ReservationStatus.CHECKED_IN)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ReservationResponse> getByGuestId(Long guestId) {
        return reservationRepository.findByGuestId(guestId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public ReservationResponse createReservation(ReservationRequest req) {
        Guest guest = guestRepository.findById(req.getGuestId())
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        Room room = roomRepository.findByRoomNumber(req.getRoomNumber())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (room.getStatus() != Room.RoomStatus.AVAILABLE) {
            throw new RuntimeException("Room " + req.getRoomNumber() + " is not available");
        }

        // Check for overlapping reservations
        boolean hasOverlap = reservationRepository
                .existsOverlappingReservation(room.getId(), req.getCheckInDate(), req.getCheckOutDate());
        if (hasOverlap) {
            throw new RuntimeException("Room already booked for selected dates");
        }

        long nights = ChronoUnit.DAYS.between(req.getCheckInDate(), req.getCheckOutDate());
        double totalAmount = nights * room.getPricePerNight();

        Reservation reservation = Reservation.builder()
                .reservationCode("R" + (1000 + new Random().nextInt(9000)))
                .guest(guest)
                .room(room)
                .checkInDate(req.getCheckInDate())
                .checkOutDate(req.getCheckOutDate())
                .numberOfGuests(req.getNumberOfGuests())
                .specialRequests(req.getSpecialRequests())
                .status(Reservation.ReservationStatus.CONFIRMED)
                .totalAmount(totalAmount)
                .bookingSource(req.getBookingSource())
                .build();

        return toResponse(reservationRepository.save(reservation));
    }

    @Transactional
    public ReservationResponse checkIn(Long id) {
        Reservation res = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (res.getStatus() != Reservation.ReservationStatus.CONFIRMED) {
            throw new RuntimeException("Cannot check in: status is " + res.getStatus());
        }

        res.setStatus(Reservation.ReservationStatus.CHECKED_IN);
        res.getRoom().setStatus(Room.RoomStatus.OCCUPIED);
        roomRepository.save(res.getRoom());
        return toResponse(reservationRepository.save(res));
    }

    @Transactional
    public ReservationResponse checkOut(Long id) {
        Reservation res = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (res.getStatus() != Reservation.ReservationStatus.CHECKED_IN) {
            throw new RuntimeException("Cannot check out: status is " + res.getStatus());
        }

        res.setStatus(Reservation.ReservationStatus.CHECKED_OUT);
        res.getRoom().setStatus(Room.RoomStatus.CLEANING);
        roomRepository.save(res.getRoom());

        // Update guest stats
        Guest guest = res.getGuest();
        guest.setTotalStays(guest.getTotalStays() == null ? 1 : guest.getTotalStays() + 1);
        guest.setTotalSpent((guest.getTotalSpent() == null ? 0 : guest.getTotalSpent()) + res.getTotalAmount());
        updateLoyaltyTier(guest);
        guestRepository.save(guest);

        return toResponse(reservationRepository.save(res));
    }

    @Transactional
    public ReservationResponse cancelReservation(Long id) {
        Reservation res = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        res.setStatus(Reservation.ReservationStatus.CANCELLED);
        if (res.getRoom().getStatus() == Room.RoomStatus.OCCUPIED) {
            res.getRoom().setStatus(Room.RoomStatus.AVAILABLE);
            roomRepository.save(res.getRoom());
        }
        return toResponse(reservationRepository.save(res));
    }

    public List<String> getAvailableRooms(LocalDate checkIn, LocalDate checkOut, String roomType) {
        return roomRepository.findAvailableRooms(checkIn, checkOut, roomType);
    }

    private void updateLoyaltyTier(Guest guest) {
        double spent = guest.getTotalSpent() == null ? 0 : guest.getTotalSpent();
        if      (spent >= 500000) guest.setLoyaltyTier(Guest.LoyaltyTier.PLATINUM);
        else if (spent >= 200000) guest.setLoyaltyTier(Guest.LoyaltyTier.GOLD);
        else if (spent >= 50000)  guest.setLoyaltyTier(Guest.LoyaltyTier.SILVER);
        else                      guest.setLoyaltyTier(Guest.LoyaltyTier.STANDARD);
    }

    private ReservationResponse toResponse(Reservation r) {
        return ReservationResponse.builder()
                .id(r.getId())
                .reservationCode(r.getReservationCode())
                .guestName(r.getGuest().getFirstName() + " " + r.getGuest().getLastName())
                .guestEmail(r.getGuest().getEmail())
                .roomNumber(r.getRoom().getRoomNumber())
                .roomType(r.getRoom().getRoomType().name())
                .checkInDate(r.getCheckInDate())
                .checkOutDate(r.getCheckOutDate())
                .numberOfGuests(r.getNumberOfGuests())
                .specialRequests(r.getSpecialRequests())
                .status(r.getStatus().name())
                .totalAmount(r.getTotalAmount())
                .createdAt(r.getCreatedAt())
                .build();
    }
}