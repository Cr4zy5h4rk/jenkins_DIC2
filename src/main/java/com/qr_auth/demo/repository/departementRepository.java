package com.qr_auth.demo.repository;

import com.qr_auth.demo.Classes.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface departementRepository extends JpaRepository<Departement, Long> {

    @Query("SELECT d from Departement d where d.nomDepartement=?1")
    Optional<Departement> findByName(String name);
}
