package za.ac.cput.service;

import za.ac.cput.dto.request.CustomerRequest;
import za.ac.cput.domain.Customer;

public interface CustomerService extends BaseCrudService<Customer, Long>{
    Customer create(CustomerRequest form);

    Customer update(Long id, CustomerRequest form);

    Customer findByEmail(String email);
}
