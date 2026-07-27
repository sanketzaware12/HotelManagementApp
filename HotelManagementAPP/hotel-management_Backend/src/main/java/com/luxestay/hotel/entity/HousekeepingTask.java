package com.luxestay.hotel.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "housekeeping_tasks")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class HousekeepingTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;       // CHECKOUT_CLEAN, TURNDOWN, DEEP_CLEAN, LINEN_CHANGE, MAINTENANCE

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;   // PENDING, IN_PROGRESS, COMPLETED

    private String assignedTo;       // staff name
    private String notes;
    private LocalDate scheduledDate;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    public enum TaskType   { CHECKOUT_CLEAN, TURNDOWN, DEEP_CLEAN, LINEN_CHANGE, INSPECTION }
    public enum TaskStatus { PENDING, IN_PROGRESS, COMPLETED }
}