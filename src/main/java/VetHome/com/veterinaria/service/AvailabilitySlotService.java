package VetHome.com.veterinaria.service;

import VetHome.com.veterinaria.entity.AvailabilitySlot;
import VetHome.com.veterinaria.repository.AvailabilitySlotRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AvailabilitySlotService {

    private final AvailabilitySlotRepository availableSlotRepository;


    public AvailabilitySlot add(AvailabilitySlot availabilitySlot){
        return availableSlotRepository.save(availabilitySlot);
    }
}
