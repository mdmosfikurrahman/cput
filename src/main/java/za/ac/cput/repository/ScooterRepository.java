package za.ac.cput.repository;

import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Scooter;

@Repository
public interface ScooterRepository extends BaseCrudRepository<Scooter, Long>{
}

