package com.luxestay.hotel.service;

import com.luxestay.hotel.entity.Guest;
import com.luxestay.hotel.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;

    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    public Guest getGuestById(Long id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guest not found: " + id));
    }

    public List<Guest> searchGuests(String query) {
        String q = query.toLowerCase();
        return guestRepository.searchGuests(q);
    }

    public Guest createGuest(Guest guest) {
        if (guestRepository.existsByEmail(guest.getEmail())) {
            throw new RuntimeException("Guest with email already exists: " + guest.getEmail());
        }
        if (guest.getLoyaltyTier() == null) {
            guest.setLoyaltyTier(Guest.LoyaltyTier.STANDARD);
        }
        if (guest.getTotalStays() == null) guest.setTotalStays(0);
        if (guest.getTotalSpent() == null) guest.setTotalSpent(0.0);
        return guestRepository.save(guest);
    }

    public Guest updateGuest(Long id, Guest updated) {
        Guest existing = getGuestById(id);
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setPhone(updated.getPhone());
        existing.setAddress(updated.getAddress());
        existing.setNationality(updated.getNationality());
        existing.setIdProofType(updated.getIdProofType());
        existing.setIdProofNumber(updated.getIdProofNumber());
        return guestRepository.save(existing);
    }

    public void deleteGuest(Long id) {
        guestRepository.deleteById(id);
    }
}
