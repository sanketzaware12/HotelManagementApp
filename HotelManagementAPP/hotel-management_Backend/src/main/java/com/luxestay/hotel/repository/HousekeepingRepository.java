package com.luxestay.hotel.repository;

import com.luxestay.hotel.entity.HousekeepingTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HousekeepingRepository extends JpaRepository<HousekeepingTask, Long> {

    List<HousekeepingTask> findByTaskStatus(HousekeepingTask.TaskStatus status);

    List<HousekeepingTask> findByScheduledDate(LocalDate date);

    List<HousekeepingTask> findByAssignedTo(String staffName);

    long countByTaskStatusAndScheduledDate(HousekeepingTask.TaskStatus status, LocalDate date);
}