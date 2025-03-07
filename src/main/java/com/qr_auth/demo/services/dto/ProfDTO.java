package com.qr_auth.demo.services.dto;


import com.qr_auth.demo.Classes.Departement;
import com.qr_auth.demo.Classes.Profile;
import lombok.Data;

@Data
public class ProfDTO {
    private String email;
    private String prenom;
    private String nom;
    private String phoneNumber;
    private String matricule;
    private Profile profile;
    private Integer nbreAbsence;
    private Departement departement;

}
