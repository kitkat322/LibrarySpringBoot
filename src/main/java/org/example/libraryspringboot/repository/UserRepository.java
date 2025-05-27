package org.example.libraryspringboot.repository;

import org.example.libraryspringboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUsername(String username);
    User findUserByRole(User.Role role);
    List<User> findByUsernameContainingIgnoreCase(String username);
}
