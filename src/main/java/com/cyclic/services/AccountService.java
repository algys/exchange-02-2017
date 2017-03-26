package com.cyclic.services;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.cyclic.models.User;

import java.util.List;

/**
 * Created by algys on 18.02.17.
 */


@SuppressWarnings({"unused", "DefaultFileTemplate"})
@Service
public interface AccountService {
    int OK = 0;
    int ERROR_DUPLICATE = 1;
    int ERROR_UNDEFINED = 5;
    int addUser(User newUser);
    Boolean hasUser(String login);
    User getUserById(Long id);
    User getUserByLogin(String login);
    User getUserByEmail(String email);
    int setUser(User updatedUser);
    List<User> getUsers();
}