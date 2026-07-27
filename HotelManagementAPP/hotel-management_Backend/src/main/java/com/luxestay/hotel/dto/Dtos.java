package com.luxestay.hotel.dto;

import com.luxestay.hotel.entity.Reservation;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

// ─── Reservation Request DTO ───────────────────────────────────────────────
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ReservationRequest {
    private Long guestId;
    private String roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer numberOfGuests;
    private String specialRequests;
    private Reservation.BookingSource bookingSource;
}

// ─── Reservation Response DTO ──────────────────────────────────────────────
@Data @NoArgsConstructor @AllArgsConstructor @Builder

public class ReservationResponse {
    private Long id;
    private String reservationCode;
    private String guestName;
    private String guestEmail;
    private String roomNumber;
    private String roomType;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer numberOfGuests;
    private String specialRequests;
    private String status;
    private Double totalAmount;
    private LocalDateTime createdAt;
}

// ─── Dashboard Stats DTO ───────────────────────────────────────────────────
@Data @NoArgsConstructor @AllArgsConstructor @Builder
 public class DashboardStats {
    private long totalRooms;
    private long occupiedRooms;
    private long availableRooms;
    private long cleaningRooms;
    private long maintenanceRooms;
    private double occupancyRate;
    private long todayCheckIns;
    private long todayCheckOuts;
    private Double todayRevenue;
    private Double monthRevenue;
}