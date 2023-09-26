package VetHome.com.veterinaria.convert;


import VetHome.com.veterinaria.DTO.CustomerDTO;
import VetHome.com.veterinaria.entity.Customer;
import VetHome.com.veterinaria.entity.Pet;
import VetHome.com.veterinaria.service.VetService;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
public class UtilityService {

    @Autowired
    private ModelMapper modelMapper;

    private final VetService vetService;


    public CustomerDTO convertCustomerToCustomerDTO(Customer customer){

        return modelMapper.map(customer, CustomerDTO.class);
    }

    public Customer convertCustomerDTOtoCustomerCreate(CustomerDTO customerDTO){
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setLastName(customerDTO.getLastName());
        customer.setPets(customerDTO.getPets());
        customer.setPassword(customerDTO.getPassword());
        customer.setAddress(customerDTO.getAddress());
        customer.setEmail(customerDTO.getEmail());
        customer.setContactNumber(customerDTO.getContactNumber());
        return customer;
    }



    public Customer convertCustomerDTOtoCustomerUpdate(CustomerDTO customerDTO, Customer existingCustomer) {
        if (StringUtils.isNotBlank(customerDTO.getName())) {
            existingCustomer.setName(customerDTO.getName());
        }
        if (StringUtils.isNotBlank(customerDTO.getLastName())) {
            existingCustomer.setLastName(customerDTO.getLastName());
        }
        if (StringUtils.isNotBlank(customerDTO.getAddress())) {
            existingCustomer.setAddress(customerDTO.getAddress());
        }
        if (StringUtils.isNotBlank(customerDTO.getEmail())) {
            existingCustomer.setEmail(customerDTO.getEmail());
        }
        if (StringUtils.isNotBlank(customerDTO.getContactNumber())) {
            existingCustomer.setContactNumber(customerDTO.getContactNumber());
        }
        if (StringUtils.isNotBlank(customerDTO.getPassword())) {
            existingCustomer.setPassword(customerDTO.getPassword());
        }
        return existingCustomer;
    }

    public Customer convertCustomerDTOtoCustomerUpdate1(CustomerDTO customerDTO, Customer existingCustomer) {
        existingCustomer.setName(customerDTO.getName());
        existingCustomer.setLastName(customerDTO.getLastName());
        existingCustomer.setAddress(customerDTO.getAddress());
        existingCustomer.setEmail(customerDTO.getEmail());
        existingCustomer.setContactNumber(customerDTO.getContactNumber());
        existingCustomer.setPassword(customerDTO.getPassword());
        return existingCustomer;
    }


    public void createPet(Customer customer, Pet pet){
        Pet newPet = new Pet();
        newPet.setPetName(pet.getPetName());
        newPet.setAge(pet.getAge());
        newPet.setGender(pet.getGender());
        newPet.setPetSpecies(pet.getPetSpecies());
        customer.getPets().add(newPet);
    }

    public void updatePetProperties(Pet existingPet, Pet pet) {
        modelMapper.map(pet, existingPet);
    }

   /* public String buildAppointmentConfirmationEmail(AppointmentDTO appointmentDTO) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        Customer customer = customerService.getCustomerById(appointmentDTO.getCustomerId());
        Optional<Vet> optionalVet = vetService.getVetById(appointmentDTO.getVetId());
        Vet vet = optionalVet.get();
        String htmlMsg =
                "<html>" +
                        "<head>" +
                        "<style>" +
                        "table {" +
                        "  border-collapse: collapse;" +
                        "  width: 100%;" +
                        "}" +
                        "th, td {" +
                        "  text-align: left;" +
                        "  padding: 8px;" +
                        "}" +
                        "th {" +
                        "  background-color: #dddddd;" +
                        "  color: #333333;" +
                        "}" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<h1 style='color: #007bff;'>Confirmación de reserva</h1>" +
                        "<p>Estimado/a " + customer.getName() + ",</p>" +
                        "<p>Please, review the details of your reservation in the following table:</p>" +
                        "<table>" +
                        "<tr>" +
                        "<th>Customer</th>" +
                        "<th>appointmentReason</th>" +
                        "<th>appointmentNotes</th>" +
                        "<th>Vet</th>" +
                        "<th>Pet</th>" +
                        "</tr>" +
                        "<tr>" +
                        "<td>" + customer.getName() + "</td>" +
                        "<td>" + appointmentDTO.getAppointmentReason() + "</td>" +
                        "<td>" + vet.getName() + "</td>" +
                        "<td>" + customer.getPets().toString() + "</td>" +
                        "<td>" + formattedDateTime + "</td>" +
                        "</tr>" +
                        "</table>" +
                        "<p>Hope to see you soon!.</p>" +
                        "<p>Sincirely,</p>" +
                        "<p>The vet</p>" +
                        "</body>" +
                        "</html>";

        return htmlMsg;
    }

    */



}
