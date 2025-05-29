package org.example.libraryspringboot.service;

import org.example.libraryspringboot.entity.User;

import java.util.List;

public interface UserService {

    void saveUserWithPassword(User user);

    //main operations with a User
    void updateUser(User user);
    void deleteUserById(int id);

    //method to get one user
    User findByUsername(String username);
    User findUserById(int id);

    //methods to get lists of users
    List<User> findUsersByUsernameContaining(String username);
    List<User> getAllUsers();

    //methods for admin to change roles of other users
    void changeUserRoleToModerator(int id);
    void changeUserRoleToUser(int id);


}
