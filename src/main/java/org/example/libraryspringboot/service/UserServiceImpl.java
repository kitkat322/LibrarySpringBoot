package org.example.libraryspringboot.service;

import lombok.RequiredArgsConstructor;
import org.example.libraryspringboot.entity.User;
import org.example.libraryspringboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUserWithPassword(User user) {

        if(user.getRole()==null){
            user.setRole(User.Role.USER);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.USER); // по умолчанию
        userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> findUsersByUsernameContaining(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

    @Override
    public void changeUserRoleToModerator(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && user.getRole() == User.Role.USER) {
            user.setRole(User.Role.MODERATOR);
            userRepository.save(user);
        }
    }

    @Override
    public void changeUserRoleToUser(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && user.getRole() == User.Role.MODERATOR) {
            user.setRole(User.Role.USER);
            userRepository.save(user);
        }
    }
}
