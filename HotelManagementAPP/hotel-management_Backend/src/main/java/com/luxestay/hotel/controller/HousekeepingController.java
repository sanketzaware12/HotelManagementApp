package com.luxestay.hotel.controller;

import com.luxestay.hotel.entity.HousekeepingTask;
import com.luxestay.hotel.service.HousekeepingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/housekeeping")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HousekeepingController {

    private final HousekeepingService housekeepingService;

    @GetMapping
    public ResponseEntity<List<HousekeepingTask>> getAllTasks() {
        return ResponseEntity.ok(housekeepingService.getAllTasks());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<HousekeepingTask>> getPendingTasks() {
        return ResponseEntity.ok(housekeepingService.getByStatus(HousekeepingTask.TaskStatus.PENDING));
    }

    @GetMapping("/today")
    public ResponseEntity<List<HousekeepingTask>> getTodayTasks() {
        return ResponseEntity.ok(housekeepingService.getTodayTasks());
    }

    @PostMapping
    public ResponseEntity<HousekeepingTask> createTask(@RequestBody HousekeepingTask task) {
        return ResponseEntity.ok(housekeepingService.createTask(task));
    }

    @PatchMapping("/{id}/start")
    public ResponseEntity<HousekeepingTask> startTask(@PathVariable Long id) {
        return ResponseEntity.ok(housekeepingService.startTask(id));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<HousekeepingTask> completeTask(@PathVariable Long id) {
        return ResponseEntity.ok(housekeepingService.completeTask(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        housekeepingService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}