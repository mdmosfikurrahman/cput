package za.ac.cput.service;


import za.ac.cput.dto.request.UserRequest;
import za.ac.cput.domain.User;

public interface UserService extends BaseCrudService<User, Long>{
    User create(UserRequest form);

    User update(Long id, UserRequest form);

    User findByEmail(String email);
}
