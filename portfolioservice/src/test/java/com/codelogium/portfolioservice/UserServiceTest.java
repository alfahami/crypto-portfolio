package com.codelogium.portfolioservice;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.codelogium.portfolioservice.entity.User;
import com.codelogium.portfolioservice.respository.UserRepository;
import com.codelogium.portfolioservice.service.UserService;
import com.codelogium.portfolioservice.service.UserServiceImp;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    List<User> users;

    @BeforeEach
    void setUp() {

        userService = new UserServiceImp(userRepository);

        users = Arrays.asList(
                new User(1L, "John", "Doe", LocalDate.parse("1999-09-24"), "Developer", null),
                new User(2L, "Doe", "Jane", LocalDate.parse("1991-09-24"), "Technical Architect", null),
                new User(3L, "Ludacris", "Lamar", LocalDate.parse("1989-09-24"), "Rap Singer", null));
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        // Mock
        when(userRepository.findById(2L)).thenReturn(Optional.of(users.get(1)));

        User retrievedUser = userRepository.findById(2L).get();

        retrievedUser.setFirstName("Tupac");
        retrievedUser.setLastName("Shakur");
        // intentionally tampering the id in order to test the ignorance of the ID in the service
        retrievedUser.setId(5L); 

        when(userRepository.save(any(User.class))).thenReturn(retrievedUser);

        // Act
        User result = userService.updateUser(2L, retrievedUser);

        // Assert
        // extra message is an optional failure message to be displayed in case the test fails
        assertEquals(2L, result.getId(), "User ID shouldn't be tampered"); 
        assertEquals("Tupac", result.getFirstName());
        assertEquals("Shakur", result.getLastName());
        assertEquals(LocalDate.parse("1991-09-24"), result.getBirthDate());
    }
}
