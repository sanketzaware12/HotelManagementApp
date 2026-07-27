package com.luxestay.hotel.service;

import com.luxestay.hotel.entity.Invoice;
import com.luxestay.hotel.entity.Reservation;
import com.luxestay.hotel.entity.Room;
import com.luxestay.hotel.repository.InvoiceRepository;
import com.luxestay.hotel.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private static final double GST_RATE = 0.18;

    private final InvoiceRepository invoiceRepository;
    private final ReservationRepository reservationRepository;

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found: " + id));
    }

    public Invoice getByReservationId(Long reservationId) {
        return invoiceRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new RuntimeException("Invoice not found for reservation: " + reservationId));
    }

    @Transactional
    public Invoice generateInvoice(Long reservationId, Double foodCharges,
                                   Double extraCharges, Double discountAmount) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found: " + reservationId));

        // Check if invoice already exists
        invoiceRepository.findByReservationId(reservationId).ifPresent(existing -> {
            throw new RuntimeException("Invoice already exists for this reservation");
        });

        long nights = ChronoUnit.DAYS.between(
                reservation.getCheckInDate(), reservation.getCheckOutDate());
        double roomCharges = nights * reservation.getRoom().getPricePerNight();

        double subtotal = roomCharges
                + (foodCharges   != null ? foodCharges   : 0)
                + (extraCharges  != null ? extraCharges  : 0)
                - (discountAmount != null ? discountAmount : 0);

        double taxAmount = Math.round(subtotal * GST_RATE * 100.0) / 100.0;
        double total     = Math.round((subtotal + taxAmount) * 100.0) / 100.0;

        Invoice invoice = Invoice.builder()
                .invoiceNumber("INV-" + (10000 + new Random().nextInt(90000)))
                .reservation(reservation)
                .roomCharges(roomCharges)
                .foodCharges(foodCharges != null ? foodCharges : 0.0)
                .extraCharges(extraCharges != null ? extraCharges : 0.0)
                .subtotal(subtotal)
                .taxRate(GST_RATE)
                .taxAmount(taxAmount)
                .discountAmount(discountAmount != null ? discountAmount : 0.0)
                .totalAmount(total)
                .paymentStatus(Invoice.PaymentStatus.PENDING)
                .generatedAt(LocalDateTime.now())
                .build();

        return invoiceRepository.save(invoice);
    }

    @Transactional
    public Invoice markAsPaid(Long id, Invoice.PaymentMethod paymentMethod) {
        Invoice invoice = getById(id);
        invoice.setPaymentStatus(Invoice.PaymentStatus.PAID);
        invoice.setPaymentMethod(paymentMethod);
        invoice.setPaidAt(LocalDateTime.now());
        return invoiceRepository.save(invoice);
    }
}