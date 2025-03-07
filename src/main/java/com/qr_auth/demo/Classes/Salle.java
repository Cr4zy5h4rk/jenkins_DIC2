package com.qr_auth.demo.Classes;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "salle")
public class Salle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dep_id")
    private Departement dep;

    @OneToOne
    @JoinColumn(name = "scanner_id")
    private Scanner scanner;
}
