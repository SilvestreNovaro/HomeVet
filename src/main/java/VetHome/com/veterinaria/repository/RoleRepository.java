package VetHome.com.veterinaria.repository;


import VetHome.com.veterinaria.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    public Optional<Role> findByRoleName(String roleName);

}
