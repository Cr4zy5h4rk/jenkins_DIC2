package com.qr_auth.demo.Classes;

import com.qr_auth.demo.enums.EStatusCours;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cours")
public class Cours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nomCours;


    @ManyToOne
    @JoinColumn(nullable = false, name = "professeur_id")
    private Utilisateur professeur;
}
