package za.ac.cput.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import za.ac.cput.dto.request.UserRequest;
import za.ac.cput.domain.User;
import za.ac.cput.enums.UserRole;
import za.ac.cput.repository.BaseCrudRepository;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.service.UserService;
import za.ac.cput.util.ServiceUtil;

@Service
public class UserServiceImpl extends BaseCrudServiceImpl<User, Long> implements UserService {
    private final UserRepository userRepository;
    public UserServiceImpl(BaseCrudRepository<User, Long> repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    @Override
    public User create(UserRequest form) {
        User user = new User();
        ServiceUtil.copyProperties(form, user);
        user.setPassword(ServiceUtil.hashPassword(user.getPassword())); // encode password
        return userRepository.save(user);
    }

    @Override
    public User update(Long id, UserRequest form) {
        User user = getById(id);
        ServiceUtil.copyProperties(form, user);
        user.setPassword(ServiceUtil.hashPassword(user.getPassword())); // encode password
        return userRepository.save(user);
    }


    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }

    @PostConstruct
    public void initializeAdminAndEmployee() {
        if (userRepository.countByUserRole(UserRole.ADMIN) == 0) {
            User user = new User();
            user.setName("Admin");
            user.setEmail("admin@gmail.com");
            user.setPassword(ServiceUtil.hashPassword("1111"));
            user.setUserRole(UserRole.ADMIN);
            user.setPhone("0786598745");
            user.setNic("ADMIN123");

            userRepository.save(user);
            System.out.println("Admin has been initialized.");
        }

        if (userRepository.countByUserRole(UserRole.EMPLOYEE) == 0) {
            User employee = new User();
            employee.setName("Employee");
            employee.setEmail("employee@gmail.com");
            employee.setPassword(ServiceUtil.hashPassword("1111"));
            employee.setUserRole(UserRole.EMPLOYEE);
            employee.setPhone("0896532568");
            employee.setNic("EMPLOYEE123");

            userRepository.save(employee);
            System.out.println("Employee has been initialized.");
        }
    }

}
