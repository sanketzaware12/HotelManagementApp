package com.luxestay.hotel.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;  // STANDARD, DELUXE, SUITE, PRESIDENTIAL

    @Enumerated(EnumType.STRING)
    private RoomStatus status;  // AVAILABLE, OCCUPIED, CLEANING, MAINTENANCE

    private Integer floor;
    private Double pricePerNight;
    private Integer capacity;
    private String description;
    private String amenities;   // comma-separated list

    public enum RoomType   { STANDARD, DELUXE, SUITE, PRESIDENTIAL }
    public enum RoomStatus { AVAILABLE, OCCUPIED, CLEANING, MAINTENANCE }
}
