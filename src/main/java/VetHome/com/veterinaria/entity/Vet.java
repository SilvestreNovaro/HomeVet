package VetHome.com.veterinaria.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Vet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cant be null")
    private String name;
    @NotBlank(message = "surName cant be null")
    private String surname;
   @Email(message = "Email must be valid")
    @NotBlank(message = "Email cant be null")
    private String email;
    @NotBlank(message = "License cant be null")
    private String license;
    @NotBlank(message = "Image cant be null")
    private String image;
    @NotBlank(message = "Specialty cant be null")
    private String specialty;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "vet_availability_slot",
            joinColumns = @JoinColumn(name = "vet_id"),
            inverseJoinColumns = @JoinColumn(name = "availability_slot_id")
    )
    private List<AvailabilitySlot> availabilitySlots = new ArrayList<>();

}
