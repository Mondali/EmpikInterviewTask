package com.empik.empikinterviewtask.service.impl;

import com.empik.empikinterviewtask.model.User;
import com.empik.empikinterviewtask.model.UserRequestCounter;
import com.empik.empikinterviewtask.repository.UserRequestCounterRepository;
import com.empik.empikinterviewtask.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RestClient restClient;
    private final UserRequestCounterRepository userRequestCounterRepository;

    @Override
    public User getByLogin(String login) {
        return restClient.get()
                .uri("/users/{login}", login)
                .exchange((request, response) -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        userRequestCounterRepository.findByLogin(login).ifPresentOrElse(
                                userRequestCounter1 -> {
                                    log.info("Incrementing request count for login {}", login);
                                    userRequestCounter1.incrementRequestCount();
                                    userRequestCounterRepository.save(userRequestCounter1);
                                },
                                () -> {
                                    log.info("Creating new UserRequestCounter for login {}", login);
                                    userRequestCounterRepository.save(new UserRequestCounter(login));
                                }
                        );
                        return Objects.requireNonNull(response.bodyTo(User.class));
                    } else if (response.getStatusCode().is4xxClientError()) {
                        throw new EntityNotFoundException(String.format("User with login %s not found", login));
                    } else {
                        throw new RuntimeException("Error while fetching user data");
                    }
                });
    }
}
