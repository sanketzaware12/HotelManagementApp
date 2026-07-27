package com.luxestay.hotel.service;

import com.luxestay.hotel.entity.Room;
import com.luxestay.hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found: " + id));
    }

    public Room getRoomByNumber(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomNumber));
    }

    public List<Room> getAvailableRooms() {
        return roomRepository.findByStatus(Room.RoomStatus.AVAILABLE);
    }

    public List<Room> getRoomsByStatus(Room.RoomStatus status) {
        return roomRepository.findByStatus(status);
    }

    public Room createRoom(Room room) {
        if (roomRepository.findByRoomNumber(room.getRoomNumber()).isPresent()) {
            throw new RuntimeException("Room number already exists: " + room.getRoomNumber());
        }
        if (room.getStatus() == null) room.setStatus(Room.RoomStatus.AVAILABLE);
        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, Room updated) {
        Room existing = getRoomById(id);
        existing.setRoomType(updated.getRoomType());
        existing.setFloor(updated.getFloor());
        existing.setPricePerNight(updated.getPricePerNight());
        existing.setCapacity(updated.getCapacity());
        existing.setDescription(updated.getDescription());
        existing.setAmenities(updated.getAmenities());
        return roomRepository.save(existing);
    }

    public Room updateRoomStatus(Long id, Room.RoomStatus status) {
        Room room = getRoomById(id);
        room.setStatus(status);
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}