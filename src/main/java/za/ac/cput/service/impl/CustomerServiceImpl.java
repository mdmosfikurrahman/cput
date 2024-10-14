package za.ac.cput.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import za.ac.cput.dto.request.CustomerRequest;
import za.ac.cput.domain.Customer;
import za.ac.cput.repository.BaseCrudRepository;
import za.ac.cput.repository.CustomerRepository;
import za.ac.cput.service.CustomerService;
import za.ac.cput.util.ServiceUtil;

@Service
public class CustomerServiceImpl extends BaseCrudServiceImpl<Customer, Long> implements CustomerService {
    private final CustomerRepository customerRepository;
    public CustomerServiceImpl(BaseCrudRepository<Customer, Long> repository, CustomerRepository customerRepository) {
        super(repository);
        this.customerRepository = customerRepository;
    }


    @Override
    public Customer create(CustomerRequest form) {
        Customer customer = new Customer();
        ServiceUtil.copyProperties(form, customer);
        customer.setPassword(ServiceUtil.hashPassword(customer.getPassword())); // encode password
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Long id, CustomerRequest form) {
        Customer customer = getById(id);
        ServiceUtil.copyProperties(form, customer);
        customer.setPassword(ServiceUtil.hashPassword(customer.getPassword())); // encode password
        return customerRepository.save(customer);
    }


    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElse(null);
    }

    @PostConstruct
    private void initializeCustomer() {
        if (customerRepository.count() == 0) {
            Customer customer = new Customer();
            customer.setName("Siya Tshitshi");
            customer.setEmail("tshitshi@gmail.com");
            customer.setPassword(ServiceUtil.hashPassword("1111"));
            customer.setPhone("0765984321");
            customer.setNic("CUST123");

            customerRepository.save(customer);
            System.out.println("Initial customer has been created.");
        }
    }

}
