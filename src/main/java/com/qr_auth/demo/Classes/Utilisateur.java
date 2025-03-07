package com.qr_auth.demo.Classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Utilisateur {
    @Id
    @Column(unique = true)
    private String email;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String prenom;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String phoneNumber;

    private Integer nbreAbsence;

    private Integer nbreHeureAbscence;

    private Integer nbreHeureAbsenceJustifie;

    private Integer nbreHeureAbsenceInjustifie;

    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classe classe;


    @ManyToOne
    @JoinColumn(name = "dep_id")
    private Departement departement;

    @Column(columnDefinition = "INTEGER")
    private Integer nbrTckPetitDej=0;

    @Column(columnDefinition = "INTEGER")
    private Integer nbrTckDej=0;

    @Column(columnDefinition = "TEXT")
    private String matricule;


    @ManyToMany
    @JoinTable(
            name = "scan_line",
            joinColumns = @JoinColumn(name = "prof_id"),
            inverseJoinColumns = @JoinColumn(name = "scanner_id")
    )
    private List<Scanner> scan_line_prof;

    @ManyToMany
    @JoinTable(
            name = "scan_line",
            joinColumns = @JoinColumn(name = "etudiant_id"),
            inverseJoinColumns = @JoinColumn(name = "scanner_id")
    )
    private List<Scanner> scan_line_eleve;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    public Utilisateur(String prenom, String nom, Departement departement, String email, String phoneNumber, Profile profile, String matricule) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profile = profile;
        this.matricule = matricule;
        this.departement = departement;
    }

    public Utilisateur(String prenom, String nom, Departement departement, String email, String phoneNumber, Profile profile, Classe classe, Integer nbrTckPetitDej, Integer nbrTckDej, Integer nbreAbsence) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profile = profile;
        this.classe = classe;
        this.departement = departement;
        this.nbrTckPetitDej = nbrTckPetitDej;
        this.nbrTckDej = nbrTckDej;
        this.nbreAbsence = nbreAbsence;
    }

}
