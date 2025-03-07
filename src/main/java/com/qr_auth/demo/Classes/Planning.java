package com.qr_auth.demo.Classes;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "planning")
public class Planning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="cours_name")
    private Cours cours;

    @ManyToOne
    @JoinColumn(name = "prof_id")
    private Utilisateur professeur;

    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classe classe;

    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;

    @ManyToOne
    @JoinColumn(name = "dep_id")
    private Departement departement;

    private LocalDateTime heureDebutPrevue;

    private LocalDateTime heureFinPrevue;

    private LocalDateTime registeredDate;
}
