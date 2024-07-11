package com.empik.empikinterviewtask.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserRequestCounter {

    @Id
    private String login;
    private Long requestCount;

    public void incrementRequestCount() {
        this.requestCount++;
    }

    public UserRequestCounter(String login) {
        this.login = login;
        this.requestCount = 1L;
    }
}
