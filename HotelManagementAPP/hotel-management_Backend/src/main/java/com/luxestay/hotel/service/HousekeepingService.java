package com.luxestay.hotel.service;

import com.luxestay.hotel.entity.HousekeepingTask;
import com.luxestay.hotel.entity.Room;
import com.luxestay.hotel.repository.HousekeepingRepository;
import com.luxestay.hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HousekeepingService {

    private final HousekeepingRepository housekeepingRepository;
    private final RoomRepository roomRepository;

    public List<HousekeepingTask> getAllTasks() {
        return housekeepingRepository.findAll();
    }

    public List<HousekeepingTask> getByStatus(HousekeepingTask.TaskStatus status) {
        return housekeepingRepository.findByTaskStatus(status);
    }

    public List<HousekeepingTask> getTodayTasks() {
        return housekeepingRepository.findByScheduledDate(LocalDate.now());
    }

    public HousekeepingTask createTask(HousekeepingTask task) {
        if (task.getTaskStatus() == null) task.setTaskStatus(HousekeepingTask.TaskStatus.PENDING);
        if (task.getScheduledDate() == null) task.setScheduledDate(LocalDate.now());
        return housekeepingRepository.save(task);
    }

    @Transactional
    public HousekeepingTask startTask(Long id) {
        HousekeepingTask task = housekeepingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found: " + id));
        task.setTaskStatus(HousekeepingTask.TaskStatus.IN_PROGRESS);
        task.setStartedAt(LocalDateTime.now());
        // Mark room as being cleaned
        if (task.getRoom() != null) {
            task.getRoom().setStatus(Room.RoomStatus.CLEANING);
            roomRepository.save(task.getRoom());
        }
        return housekeepingRepository.save(task);
    }

    @Transactional
    public HousekeepingTask completeTask(Long id) {
        HousekeepingTask task = housekeepingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found: " + id));
        task.setTaskStatus(HousekeepingTask.TaskStatus.COMPLETED);
        task.setCompletedAt(LocalDateTime.now());
        // Set room back to available after cleaning
        if (task.getRoom() != null &&
            task.getRoom().getStatus() == Room.RoomStatus.CLEANING) {
            task.getRoom().setStatus(Room.RoomStatus.AVAILABLE);
            roomRepository.save(task.getRoom());
        }
        return housekeepingRepository.save(task);
    }

    public void deleteTask(Long id) {
        housekeepingRepository.deleteById(id);
    }
}