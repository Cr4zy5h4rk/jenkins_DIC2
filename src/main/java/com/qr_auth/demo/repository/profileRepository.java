package com.qr_auth.demo.repository;

import com.qr_auth.demo.Classes.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface profileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findProfileByLibelle(String libelle);
}
