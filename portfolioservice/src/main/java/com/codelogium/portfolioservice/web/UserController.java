package com.codelogium.portfolioservice.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelogium.portfolioservice.entity.User;
import com.codelogium.portfolioservice.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;



@RestController
@AllArgsConstructor
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.valueOf(201));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.valueOf(200));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User newUser) {
        return new ResponseEntity<>(userService.updateUser(id, newUser), HttpStatus.valueOf(200));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> removeUser(@PathVariable Long id) {
        userService.removeUser(id);
        return new ResponseEntity<>(HttpStatus.valueOf(204));
    }
}
