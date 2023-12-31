package VetHome.com.veterinaria.service;


import VetHome.com.veterinaria.DTO.CustomerDTO;
import VetHome.com.veterinaria.convert.UtilityService;
import VetHome.com.veterinaria.entity.Customer;
import VetHome.com.veterinaria.entity.Pet;
import VetHome.com.veterinaria.entity.Role;
import VetHome.com.veterinaria.exception.BadRequestException;
import VetHome.com.veterinaria.exception.NotFoundException;
import VetHome.com.veterinaria.repository.CustomerRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service

public class CustomerService {

    @Autowired
    private final ModelMapper modelMapper;

    private final CustomerRepository customerRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final SpringTemplateEngine templateEngine;

    private JavaMailSender javaMailSender;

    private static final String NOT_FOUND_CUSTOMER = "Customer not found";

    private static final String NOT_FOUND_ROLE = "Role not found";

    @Autowired
    UtilityService utilityService;



    public void createCustomer(CustomerDTO customerDTO) throws MessagingException {
        customerRepository.findByEmail(customerDTO.getEmail()).ifPresent( c -> {
            throw new BadRequestException("Email already in use");
        });
        Customer customer = utilityService.convertCustomerDTOtoCustomerCreate(customerDTO);
        String encodedPassword = this.passwordEncoder.encode(customerDTO.getPassword());
        customer.setPassword(encodedPassword);
        Role role = roleService.findById(1L).orElseThrow(() -> new NotFoundException(NOT_FOUND_ROLE));
        customer.setRole(role);
        sendRegistrationEmail(customer);
        customerRepository.save(customer);
    }
    public void sendRegistrationEmail(Customer customer) throws MessagingException {
        String recipient = customer.getEmail();
        String subject = "Registry exitoso en VETHOME";

        Context context = new Context();
        context.setVariable("name", customer.getName());

        // Procesar la plantilla Thymeleaf con el Context
        String content = templateEngine.process("email-template", context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(recipient);
        helper.setSubject(subject);


        // Establece el contenido como HTML
        helper.setText(content, true);
        //ESTA LINEA ENVIA EL MAIL DE RECORDATORIO DE FORMA CORRECTA!!
        javaMailSender.send(mimeMessage);
    }


    public void updateCustomerDTO(CustomerDTO customerDTO, Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CUSTOMER));
        customerRepository.findByEmail(customerDTO.getEmail()).ifPresent( c -> {
            throw new BadRequestException("Email already in use");
        });
        customer =  utilityService.convertCustomerDTOtoCustomerUpdate(customerDTO, customer);
        List<Pet> existingPets = customer.getPets();
        if(!customerDTO.getPets().isEmpty()) {
            for (Pet pet : customerDTO.getPets()) {
                if (pet.getId() != null) {
                    Pet existingPet = existingPets.stream()
                            .filter(pet1 -> pet1.getId().equals(pet.getId()))
                            .findFirst()
                            .orElseThrow(() -> new NotFoundException("Pet not found"));
                    modelMapper.map(pet, existingPet);
                } else {
                    utilityService.createPet(customer, pet);
                }
            }
        }
        String encodedPassword = this.passwordEncoder.encode(customerDTO.getPassword());
        customer.setPassword(encodedPassword);
        Role role = roleService.findById(customerDTO.getRoleId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ROLE));
        customer.setRole(role);
        customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CUSTOMER));
    }
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CUSTOMER));
        customerRepository.delete(customer);
    }

    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(NOT_FOUND_CUSTOMER));
    }

    public List<Customer> findAllAsc(){
        return customerRepository.findAllAsc();
    }

    public Long countCustomers(){
        return customerRepository.countCustomers();
    }

    public Long countCustomersByPetSpecies(String petSpecies) {
        return customerRepository.countCustomersByPetSpecies(petSpecies);
    }

    public List<Customer> listOfCustomersByPetSpecies(String petSpecies){
        return customerRepository.findTheCustomersByPetSpecies(petSpecies);
    }

    public List<Customer> listOfCustomersRole(){
        return customerRepository.findAllCustomerByRole();
    }

    public List<Customer> listCustomersWithSeniorPets(){
        return customerRepository.findOldPets();
    }

    public List<Pet> findPetsByCustomerLastName(String lastName){
        List<Pet> pets = customerRepository.findPetsByCustomerLastName(lastName);
        if (pets.isEmpty()) {
            throw new NotFoundException("No pets found for customer with last name: " + lastName);
        }
        return pets;
    }

    public List<Customer> findByLastName(String lastName){
        List<Customer> customers = customerRepository.findByLastName(lastName);
        if(customers.isEmpty()) {
            throw new NotFoundException("No pets found for customer with last name: " + lastName);
        }
        return customers;
    }

    public Customer findByLastNameAndAddress(String lastName, String address){
        return customerRepository.findByLastNameAndAddress(lastName, address).orElseThrow(() -> new NotFoundException(NOT_FOUND_CUSTOMER));
    }

    public List<Customer> findCustomersByPetName(String petName) {
        List<Customer> customers = customerRepository.findCustomersByPetName(petName);
        if(customers.isEmpty()){
            throw new NotFoundException("No pets found for customer with last name");
        }
        return customers;
    }

    public void addAnimalToCustomer(Long customerId, Pet pet){
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException(NOT_FOUND_CUSTOMER));
        customer.getPets().add(pet);
        customerRepository.save(customer);
    }

    public void addMultiplePetsToCustomer(Long customerId, List<Pet> pets){
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException(NOT_FOUND_CUSTOMER));
        List<Pet> petList = customer.getPets();
        for(Pet pet: pets){
            petList.add(pet);
            customer.setPets(petList);
        }
        customerRepository.save(customer);
    }

    public void addRoleToCustomer(Long customerId, Long roleId){
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException(NOT_FOUND_CUSTOMER));
        roleService.findById(roleId).ifPresentOrElse(
                foundRole -> {
                    customer.setRole(foundRole);
                    customerRepository.save(customer);
                },
                () -> {
                    throw new NotFoundException(NOT_FOUND_ROLE);
                }
        );
        customerRepository.save(customer);
    }


    public void deletePetsById(Long customerId, List<Long> petIds){
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException(NOT_FOUND_CUSTOMER));
        List<Pet> pets = customer.getPets();
            List<Pet> petsToRemove = new ArrayList<>();
            for (Long petId : petIds) {
                // La variable pet se sobreescribe en cada vuelta, guardando el objeto Pet que contenga el id proporcionado en petIds.
                Pet pet = pets.stream().filter(p -> p.getId().equals(petId)).findFirst().orElseThrow(() -> new NotFoundException("Pet with id: " + petId + " does not belong to the customer"));
                if (pet != null) {
                    petsToRemove.add(pet);
                }
                pets.removeAll(petsToRemove);
                customer.setPets(pets);
            }
        customerRepository.save(customer);
    }

    public void deletePetById(Long customerId, Long petId){
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException(NOT_FOUND_CUSTOMER));
        List<Pet> pets = customer.getPets();
        Pet pet = pets.stream().filter(p -> p.getId().equals(petId)).findFirst().orElseThrow(() -> new NotFoundException("Pet not found"));
        pets.remove(pet);
        customer.setPets(pets);
        customerRepository.save(customer);
    }

    public List<Customer> findCustomerByRoleId(Long id){
        return customerRepository.findCustomerByRoleId(id);
    }


    public void deleteRoleById(Long customerId, Long roleId){
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if(optionalCustomer.isPresent()){
            Customer customer = optionalCustomer.get();
            if(customer.getRole() !=null && customer.getRole().getId().equals(roleId)){
                customer.setRole(null);
                customerRepository.save(customer);
                //roleService.delete(roleId); Si agrego esta linea, borra el rol del customer y el rol de la base de datos.
            }else {
                throw new NotFoundException("Role not found with the id " + roleId);
            }
            }else {
                throw new NotFoundException(NOT_FOUND_CUSTOMER);
            }


        }


    }







