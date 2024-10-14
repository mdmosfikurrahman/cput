package za.ac.cput.ui.service;

import org.springframework.web.client.RestTemplate;
import za.ac.cput.ui.model.CustomerUIModel;

import java.util.Arrays;
import java.util.List;

public class UICustomerService {
    private final String BASE_URL = "http://localhost:8080/customers";
    private final RestTemplate restTemplate;

    public UICustomerService() {
        this.restTemplate = new RestTemplate();
    }

    public List<CustomerUIModel> getAllCustomers() {
        CustomerUIModel[] customers = restTemplate.getForObject(BASE_URL, CustomerUIModel[].class);
        return Arrays.asList(customers);
    }

    public CustomerUIModel addCustomer(CustomerUIModel customer) {
        return restTemplate.postForObject(BASE_URL, customer, CustomerUIModel.class);
    }

    public void updateCustomer(CustomerUIModel customer) {
        restTemplate.put(BASE_URL + "/" + customer.getId(), customer);
    }

    public void deleteCustomer(Long id) {
        restTemplate.delete(BASE_URL + "/" + id);
    }
}