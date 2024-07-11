package com.empik.empikinterviewtask.controller;

import com.empik.empikinterviewtask.model.UserRequestCounter;
import com.empik.empikinterviewtask.repository.UserRequestCounterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("testcontainers")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRequestCounterRepository userRequestCounterRepository;

    @Container
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:12-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        userRequestCounterRepository.deleteAll();
    }

    @Test
    void testGetUserByLogin_FirstRequest_UserFound() throws Exception {
        mockMvc.perform(get("/api/v1/users/octocat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("octocat"));

        UserRequestCounter counter = userRequestCounterRepository.findByLogin("octocat")
                .orElseThrow(() -> new AssertionError("User request counter should have been created"));

        assertEquals(1, counter.getRequestCount(), "Counter should be initialized with a count of 1");
    }

    @Test
    void testGetUserByLogin_NotFirstRequest_UserFound() throws Exception {
        UserRequestCounter existingCounter = new UserRequestCounter("octocat");
        existingCounter.setRequestCount(5L);
        userRequestCounterRepository.save(existingCounter);

        mockMvc.perform(get("/api/v1/users/octocat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("octocat"));

        UserRequestCounter updatedCounter = userRequestCounterRepository.findByLogin("octocat")
                .orElseThrow(() -> new AssertionError("User request counter should exist"));

        assertEquals(6, updatedCounter.getRequestCount(), "Request count should be incremented");
    }

    @Test
    void testGetUserByLogin_UserNotFound_NoCounterAdded() throws Exception {
        mockMvc.perform(get("/api/v1/users/nonexistentuser"))
                .andExpect(status().isNotFound());

        Optional<UserRequestCounter> counter = userRequestCounterRepository.findByLogin("nonexistentuser");
        assertTrue(counter.isEmpty(), "No counter should be created for a non-existent user");
    }

    @Test
    void testGetUserByLogin_UserFoundAndCountIncremented() throws Exception {
        UserRequestCounter counter = new UserRequestCounter("octocat");
        userRequestCounterRepository.save(counter);

        mockMvc.perform(get("/api/v1/users/octocat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("octocat"));

        UserRequestCounter updatedCounter = userRequestCounterRepository.findByLogin("octocat")
                .orElseThrow(() -> new AssertionError("User request counter should exist"));

        assertEquals(2, updatedCounter.getRequestCount(), "Request count should be incremented");
    }
}
