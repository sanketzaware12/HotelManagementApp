package com.luxestay.hotel.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    private Double roomCharges;
    private Double foodCharges;
    private Double extraCharges;
    private Double subtotal;
    private Double taxRate;        // e.g. 0.18 for 18% GST
    private Double taxAmount;
    private Double discountAmount;
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private LocalDateTime generatedAt;
    private LocalDateTime paidAt;

    public enum PaymentStatus { PENDING, PARTIAL, PAID, REFUNDED }
    public enum PaymentMethod { CASH, CREDIT_CARD, DEBIT_CARD, UPI, NET_BANKING }
}