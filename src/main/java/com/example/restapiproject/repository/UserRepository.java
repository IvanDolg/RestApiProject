package com.example.restapiproject.repository;

import com.example.restapiproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.lastVisitDate = :lastVisitDate WHERE u.username = :username")
    void updateLastVisitDate(@Param("username") String username, @Param("lastVisitDate") LocalDateTime lastVisitDate);
}
