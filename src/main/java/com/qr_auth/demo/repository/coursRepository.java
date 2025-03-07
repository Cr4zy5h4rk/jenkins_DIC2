package com.qr_auth.demo.repository;

import com.qr_auth.demo.Classes.Cours;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface coursRepository extends JpaRepository<Cours,Long> {

    List<Cours> findByProfesseurEmail(String professeurEmail);
}
