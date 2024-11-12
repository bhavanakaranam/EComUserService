package com.scaler.ecomuserservice.repository;

import com.scaler.ecomuserservice.models.ECom_User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ECom_User, Long>
{
    Optional<ECom_User> findByEmail(String email);
}
