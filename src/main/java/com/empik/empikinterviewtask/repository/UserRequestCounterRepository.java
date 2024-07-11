package com.empik.empikinterviewtask.repository;

import com.empik.empikinterviewtask.model.UserRequestCounter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRequestCounterRepository extends JpaRepository<UserRequestCounter, Integer> {
    Optional<UserRequestCounter> findByLogin(String login);
}
