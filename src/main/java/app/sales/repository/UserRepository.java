package app.sales.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.sales.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
