package com.luxestay.hotel.repository;

import com.luxestay.hotel.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByReservationId(Long reservationId);

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    List<Invoice> findByPaymentStatus(Invoice.PaymentStatus status);

    @Query("""
        SELECT SUM(i.totalAmount) FROM Invoice i
        WHERE i.paymentStatus = 'PAID'
          AND i.paidAt BETWEEN :from AND :to
    """)
    Double sumPaidInRange(
            @Param("from") LocalDateTime from,
            @Param("to")   LocalDateTime to);
}