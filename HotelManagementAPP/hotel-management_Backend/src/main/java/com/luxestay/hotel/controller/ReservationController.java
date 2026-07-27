package com.luxestay.hotel.controller;

import com.luxestay.hotel.dto.ReservationRequest;
import com.luxestay.hotel.dto.ReservationResponse;
import com.luxestay.hotel.entity.Reservation;
import com.luxestay.hotel.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ReservationResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(reservationService.getByCode(code));
    }

    @GetMapping("/today/checkins")
    public ResponseEntity<List<ReservationResponse>> getTodayCheckIns() {
        return ResponseEntity.ok(reservationService.getTodayCheckIns());
    }

    @GetMapping("/today/checkouts")
    public ResponseEntity<List<ReservationResponse>> getTodayCheckOuts() {
        return ResponseEntity.ok(reservationService.getTodayCheckOuts());
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<List<ReservationResponse>> getByGuest(@PathVariable Long guestId) {
        return ResponseEntity.ok(reservationService.getByGuestId(guestId));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(reservationService.createReservation(request));
    }

    @PatchMapping("/{id}/checkin")
    public ResponseEntity<ReservationResponse> checkIn(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.checkIn(id));
    }

    @PatchMapping("/{id}/checkout")
    public ResponseEntity<ReservationResponse> checkOut(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.checkOut(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReservationResponse> cancelReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.cancelReservation(id));
    }

    @GetMapping("/availability")
    public ResponseEntity<List<String>> checkAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(required = false) String roomType) {
        return ResponseEntity.ok(reservationService.getAvailableRooms(checkIn, checkOut, roomType));
    }
}