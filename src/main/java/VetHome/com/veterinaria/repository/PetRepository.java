package VetHome.com.veterinaria.repository;



import VetHome.com.veterinaria.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("select p from Pet p where p.petName = ?1")
    public List<Pet> findByName(String name);

    List<Pet> findByAge(Integer age);

    List<Pet> findByGender(String gender);

    List<Pet> findByPetSpecies(String petSpecies);









}
