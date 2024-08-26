package org.esfe.repository;

import org.esfe.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByGoogleId(String googleId);
    Optional<User> findByEmail(String email);
}
