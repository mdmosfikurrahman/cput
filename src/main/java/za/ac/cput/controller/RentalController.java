package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.dto.request.RentalRequest;
import za.ac.cput.domain.RentalDetail;
import za.ac.cput.service.RentalService;

import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @GetMapping("")
    public List<RentalDetail> list() {
        return rentalService.getAll();
    }

    @GetMapping("/{id}")
    public RentalDetail getById(@PathVariable Long id) {
        return rentalService.getById(id);
    }

    @PostMapping("")
    public RentalDetail create(@RequestBody RentalRequest form) {
        return rentalService.create(form);
    }

    @PutMapping("/{id}")
    public RentalDetail update(@PathVariable Long id, @RequestBody RentalRequest form) {
        return rentalService.update(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        rentalService.delete(id);
    }

    @PutMapping("/{id}/accept")
    public RentalDetail acceptRental(@PathVariable Long id) {
        return rentalService.acceptRental(id);
    }

    @PutMapping("/{id}/reject")
    public RentalDetail rejectRental(@PathVariable Long id) {
        return rentalService.rejectRental(id);
    }

    @PutMapping("/{id}/complete")
    public RentalDetail completeRental(@PathVariable Long id) {
        return rentalService.completeRental(id);
    }
}
