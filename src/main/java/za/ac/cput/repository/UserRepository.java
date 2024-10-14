package za.ac.cput.repository;

import org.springframework.stereotype.Repository;
import za.ac.cput.domain.User;
import za.ac.cput.enums.UserRole;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseCrudRepository<User, Long>{
    int countByUserRole(UserRole userRole);

    Optional<User> findByEmail(String email);
}
