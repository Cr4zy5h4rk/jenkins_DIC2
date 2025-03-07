package com.qr_auth.demo.repository;

import com.qr_auth.demo.Classes.Scanner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface scannerRepository extends JpaRepository<Scanner, Long> {
}
