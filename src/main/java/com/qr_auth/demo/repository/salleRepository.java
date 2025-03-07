package com.qr_auth.demo.repository;

import com.qr_auth.demo.Classes.Salle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface salleRepository extends JpaRepository<Salle, Long> {
}
