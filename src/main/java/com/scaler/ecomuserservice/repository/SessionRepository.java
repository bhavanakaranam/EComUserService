package com.scaler.ecomuserservice.repository;

import com.scaler.ecomuserservice.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long>
{
    Optional<Session> findByUser_name(String userName);
}
