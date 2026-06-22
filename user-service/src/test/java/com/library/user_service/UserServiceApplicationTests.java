package com.library.user_service;

import com.library.user_service.entity.User;
import com.library.user_service.repository.UserRepository;
import com.library.user_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // This automatically uses H2 database
class UserServiceApplicationTests {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void cleanDatabase() {
		userRepository.deleteAll(); // Ensures a clean slate before each test
	}

	@Test
	void integrationScenario1_CreateAndRetrieveUser() {
		User newUser = User.builder().username("integration_user").password("pass").build();
		User savedUser = userService.createUser(newUser);

		User retrievedUser = userService.getUserById(savedUser.getId());

		assertEquals("integration_user", retrievedUser.getUsername());
		assertEquals("ROLE_USER", retrievedUser.getRole()); // Tests business logic default role
	}

	@Test
	void integrationScenario2_UpdateExistingUser() {
		User user = userService.createUser(User.builder().username("old_name").password("pass").build());

		User updates = User.builder().username("new_name").password("pass").build();
		userService.updateUser(user.getId(), updates);

		User verifiedUser = userService.getUserById(user.getId());
		assertEquals("new_name", verifiedUser.getUsername());
	}

	@Test
	void integrationScenario3_DeleteUser() {
		User user = userService.createUser(User.builder().username("to_be_deleted").password("pass").build());
		assertTrue(userRepository.findById(user.getId()).isPresent());

		userService.deleteUser(user.getId());

		assertTrue(userRepository.findById(user.getId()).isEmpty());
	}
}