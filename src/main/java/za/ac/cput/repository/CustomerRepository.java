package za.ac.cput.repository;


import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends BaseCrudRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
}
