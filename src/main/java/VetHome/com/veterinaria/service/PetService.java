package VetHome.com.veterinaria.service;


import VetHome.com.veterinaria.convert.UtilityService;
import VetHome.com.veterinaria.entity.Pet;
import VetHome.com.veterinaria.exception.NotFoundException;
import VetHome.com.veterinaria.repository.PetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service

public class PetService {


    private final PetRepository petRepository;

    private final UtilityService utilityService;

    private final VetService vetService;




    private static final String NOT_FOUND_PET = "Pet not found";

    public void createPet(Pet pet) {
        petRepository.save(pet);
    }

    public void updatePet(Pet pet, Long id) {
        Pet existingPet = petRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_PET));
        utilityService.updatePetProperties(existingPet, pet);
        petRepository.save(existingPet);
    }



    public void deletePet(Long id) {
        petRepository.findById(id).ifPresentOrElse(pet -> petRepository.deleteById(id),
                () -> {
                    throw new NotFoundException(NOT_FOUND_PET);
                });

    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }


    public Pet getPetById(Long id){
        return petRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_PET));
    }

    public Optional<Pet> id(Long id){
        return petRepository.findById(id).or(() -> {
            throw new NotFoundException("nf");
        });
    }


    public List<Pet> findByName(String name) {
        List<Pet> pets = petRepository.findByName(name);
        if (pets.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_PET);
        }
        return pets;
    }

    public List<Pet> findByAge(Integer age) {
        List<Pet> pets = petRepository.findByAge(age);
        if (pets.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_PET);
        }
        return pets;
    }

    public List<Pet> findByGender(String gender) {
        List<Pet> pets = petRepository.findByGender(gender);
        if (pets.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_PET);
        }
        return pets;

    }

    public List<Pet> findBySpecies(String petSpecies) {
        List<Pet> pets = petRepository.findByPetSpecies(petSpecies);
        if (pets.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_PET);
        }
        return pets;
    }


}







