package com.luxestay.hotel.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "guests")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    private String phone;
    private String address;
    private String nationality;
    private String idProofType;   // PASSPORT, AADHAAR, PAN, DRIVING_LICENSE
    private String idProofNumber;
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private LoyaltyTier loyaltyTier;

    private Integer totalStays;
    private Double totalSpent;

    public enum LoyaltyTier { STANDARD, SILVER, GOLD, PLATINUM }
}