package com.bookstore.usersservice.controller;

import com.bookstore.usersservice.model.UserDTO;
import com.bookstore.usersservice.model.UserRequestModel;
import com.bookstore.usersservice.repository.User;
import com.bookstore.usersservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public UserDTO login(@RequestBody UserDTO dto) {
        return userService.login(dto.getUsername(), dto.getPassword());
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id,
                              @RequestBody UserRequestModel user ) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "Delete";
    }
}
