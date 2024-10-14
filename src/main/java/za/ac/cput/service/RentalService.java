package za.ac.cput.service;


import za.ac.cput.dto.request.RentalRequest;
import za.ac.cput.domain.RentalDetail;

import java.util.List;

public interface RentalService extends BaseCrudService<RentalDetail, Long>{
    RentalDetail create(RentalRequest form);

    RentalDetail update(Long id, RentalRequest form);

    RentalDetail acceptRental(Long id);

    RentalDetail rejectRental(Long id);

    RentalDetail completeRental(Long id);

    List<RentalDetail> getRentalsByCustomerId(Long id);
}
