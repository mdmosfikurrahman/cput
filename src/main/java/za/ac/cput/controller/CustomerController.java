package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.dto.request.CustomerRequest;
import za.ac.cput.domain.Customer;
import za.ac.cput.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/customers")
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("")
    public List<Customer> list() {
        return customerService.getAll();
    }

    @GetMapping("/{id}")
    public Customer getById(@PathVariable Long id) {
        return customerService.getById(id);
    }

    @PostMapping("")
    public Customer create(@RequestBody CustomerRequest form) {
        return customerService.create(form);
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id, @RequestBody CustomerRequest form) {
        return customerService.update(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        customerService.delete(id);
    }
}
