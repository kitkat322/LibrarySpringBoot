package org.example.libraryspringboot.service;

import org.example.libraryspringboot.entity.User;

import java.util.List;

public interface UserService {

    void saveUserWithPassword(User user);
    void updateUser(User user);
    void deleteUserById(int id);
    List<User> getAllUsers();
    User findByUsername(String username);
    void changeUserRoleToModerator(int id);
    void changeUserRoleToUser(int id);
    User findUserById(int id);
    List<User> findUsersByUsernameContaining(String username);
}
