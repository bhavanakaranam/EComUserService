package com.scaler.ecomuserservice.security.repository;

import com.scaler.ecomuserservice.security.model.AuthorizationConsent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorizationConsentRepository extends JpaRepository<AuthorizationConsent, AuthorizationConsent.AuthorizationConsentId>
{
    Optional<AuthorizationConsent> findByRegisteredClientIdAndPrincipalName(String restrictedClientId, String principalName);
    void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);
}
