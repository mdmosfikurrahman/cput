package za.ac.cput.service;

import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.dto.request.ScooterRequest;
import za.ac.cput.domain.Scooter;

import java.util.List;

public interface ScooterService extends BaseCrudService<Scooter, Long>{
    Scooter create(ScooterRequest form, MultipartFile file);
    Scooter update(Long id, ScooterRequest form, MultipartFile file);
    Scooter update(Long id, ScooterRequest form);
    List<Scooter> search(String q);

    List<Scooter> searchByModelOrYear(String model, Integer year);

    List<Scooter> getAvailableScooters();
}
