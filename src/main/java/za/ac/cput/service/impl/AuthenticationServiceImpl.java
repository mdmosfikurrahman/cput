package za.ac.cput.service.impl;


import org.springframework.stereotype.Service;
import za.ac.cput.domain.Customer;
import za.ac.cput.domain.User;
import za.ac.cput.service.AuthenticationService;
import za.ac.cput.service.CustomerService;
import za.ac.cput.service.UserService;
import za.ac.cput.util.ServiceUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final CustomerService customerService;
    private final UserService userService;

    public AuthenticationServiceImpl(CustomerService customerService, UserService userService) {
        this.customerService = customerService;
        this.userService = userService;
    }

    @Override
    public Object authenticate(String email, String password, String role) {
        if ("CUSTOMER".equalsIgnoreCase(role)) {
            Customer customer = customerService.findByEmail(email);
            if (customer != null && ServiceUtil.checkPassword(password, customer.getPassword())) {
                return ServiceUtil.copyProperties(customer, new Customer());
            }
        } else {
            User user = userService.findByEmail(email);
            if (user != null && ServiceUtil.checkPassword(password, user.getPassword()) &&
                    user.getUserRole().name().equalsIgnoreCase(role)) {
                return user;
            }
        }
        return null;
    }


}
