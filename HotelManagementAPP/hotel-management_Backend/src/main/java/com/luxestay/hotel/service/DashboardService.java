package com.luxestay.hotel.service;

import com.luxestay.hotel.dto.DashboardStats;
import com.luxestay.hotel.entity.Reservation;
import com.luxestay.hotel.entity.Room;
import com.luxestay.hotel.repository.ReservationRepository;
import com.luxestay.hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public DashboardStats getDashboardStats() {
        long total       = roomRepository.count();
        long occupied    = roomRepository.countByStatus(Room.RoomStatus.OCCUPIED);
        long available   = roomRepository.countByStatus(Room.RoomStatus.AVAILABLE);
        long cleaning    = roomRepository.countByStatus(Room.RoomStatus.CLEANING);
        long maintenance = roomRepository.countByStatus(Room.RoomStatus.MAINTENANCE);

        double occupancyRate = total > 0 ? (double) occupied / total * 100 : 0;

        long todayCheckIns  = reservationRepository
                .findByCheckInDateAndStatus(LocalDate.now(), Reservation.ReservationStatus.CONFIRMED).size();
        long todayCheckOuts = reservationRepository
                .findByCheckOutDateAndStatus(LocalDate.now(), Reservation.ReservationStatus.CHECKED_IN).size();

        Double todayRevenue = reservationRepository
                .getTotalRevenueInRange(LocalDate.now(), LocalDate.now());
        Double monthRevenue = reservationRepository
                .getTotalRevenueInRange(LocalDate.now().withDayOfMonth(1), LocalDate.now());

        return DashboardStats.builder()
                .totalRooms(total)
                .occupiedRooms(occupied)
                .availableRooms(available)
                .cleaningRooms(cleaning)
                .maintenanceRooms(maintenance)
                .occupancyRate(Math.round(occupancyRate * 10.0) / 10.0)
                .todayCheckIns(todayCheckIns)
                .todayCheckOuts(todayCheckOuts)
                .todayRevenue(todayRevenue != null ? todayRevenue : 0.0)
                .monthRevenue(monthRevenue != null ? monthRevenue : 0.0)
                .build();
    }
}
