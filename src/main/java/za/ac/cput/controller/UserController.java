package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.dto.request.UserRequest;
import za.ac.cput.domain.User;
import za.ac.cput.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public List<User> list() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping("")
    public User create(@RequestBody UserRequest form) {
        return userService.create(form);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody UserRequest form) {
        return userService.update(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        userService.delete(id);
    }
}
