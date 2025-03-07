package com.qr_auth.demo.Classes;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "departement")
public class Departement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nomDepartement;
}
