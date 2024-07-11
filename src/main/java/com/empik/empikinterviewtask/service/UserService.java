package com.empik.empikinterviewtask.service;

import com.empik.empikinterviewtask.model.User;

public interface UserService {

    User getByLogin(String login);

}
