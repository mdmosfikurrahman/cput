package za.ac.cput.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import za.ac.cput.dto.request.RentalRequest;
import za.ac.cput.domain.RentalDetail;
import za.ac.cput.domain.Scooter;
import za.ac.cput.enums.RentalStatus;
import za.ac.cput.enums.ScooterStatus;
import za.ac.cput.repository.BaseCrudRepository;
import za.ac.cput.repository.CustomerRepository;
import za.ac.cput.repository.RentalRepository;
import za.ac.cput.repository.ScooterRepository;
import za.ac.cput.service.RentalService;

import java.util.List;

@Service
public class RentalServiceImpl extends BaseCrudServiceImpl<RentalDetail, Long> implements RentalService {
    private final RentalRepository rentalRepository;
    private final CustomerRepository customerRepository;
    private final ScooterRepository scooterRepository;

    public RentalServiceImpl(BaseCrudRepository<RentalDetail, Long> repository, RentalRepository rentalRepository, CustomerRepository customerRepository, ScooterRepository scooterRepository) {
        super(repository);
        this.rentalRepository = rentalRepository;
        this.customerRepository = customerRepository;
        this.scooterRepository = scooterRepository;
    }

    @Override
    public RentalDetail create(RentalRequest form) {
        RentalDetail rentalDetail = new RentalDetail();
        BeanUtils.copyProperties(form, rentalDetail);

        customerRepository.findById(form.getCustomerId()).ifPresent(rentalDetail::setCustomer);
        rentalDetail.setStatus(RentalStatus.PENDING);

        Scooter scooter = scooterRepository.findById(form.getScooterId()).orElseThrow(() -> new RuntimeException("Scooter not found"));
        scooter.setStatus(ScooterStatus.NOT_AVAILABLE);
        scooterRepository.save(scooter);

        rentalDetail.setScooter(scooter);
        rentalDetail.setTotalPrice(rentalDetail.getScooter().getRentalPerDay() * form.getNoOfDates());

        return rentalRepository.save(rentalDetail);
    }

    @Override
    public RentalDetail update(Long id, RentalRequest form) {
        return null;
    }

    @Override
    public RentalDetail acceptRental(Long id) {
        RentalDetail rentalDetail = getById(id);
        rentalDetail.setStatus(RentalStatus.ACCEPTED);

        Scooter scooter = scooterRepository.findById(rentalDetail.getScooter().getId()).orElseThrow(() -> new RuntimeException("Scooter not found"));
        scooter.setStatus(ScooterStatus.NOT_AVAILABLE);
        scooterRepository.save(scooter);
        return rentalRepository.save(rentalDetail);
    }

    @Override
    public RentalDetail rejectRental(Long id) {
        RentalDetail rentalDetail = getById(id);
        rentalDetail.setStatus(RentalStatus.REJECTED);

        Scooter scooter = scooterRepository.findById(rentalDetail.getScooter().getId()).orElseThrow(() -> new RuntimeException("Scooter not found"));
        scooter.setStatus(ScooterStatus.AVAILABLE);
        scooterRepository.save(scooter);
        return rentalRepository.save(rentalDetail);
    }

    @Override
    public RentalDetail completeRental(Long id) {
        RentalDetail rentalDetail = getById(id);
        rentalDetail.setStatus(RentalStatus.COMPLETED);

        Scooter scooter = scooterRepository.findById(rentalDetail.getScooter().getId()).orElseThrow(() -> new RuntimeException("Scooter not found"));
        scooter.setStatus(ScooterStatus.AVAILABLE);
        scooterRepository.save(scooter);
        return rentalRepository.save(rentalDetail);
    }

    @Override
    public List<RentalDetail> getRentalsByCustomerId(Long customerId) {
        return rentalRepository.findByCustomerId(customerId);
    }

}
