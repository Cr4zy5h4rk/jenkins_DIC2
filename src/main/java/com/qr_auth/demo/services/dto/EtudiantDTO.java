package com.qr_auth.demo.services.dto;

import com.qr_auth.demo.Classes.Classe;
import com.qr_auth.demo.Classes.Departement;
import com.qr_auth.demo.Classes.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class EtudiantDTO {
        private String email;
        private String prenom;
        private String nom;
        private Profile profile;
        private String phoneNumber;
        private Integer nbrTckPetitDej;
        private Integer nbrTckDej;
        private Integer nbreAbsence;
        private Classe classe;
        private Departement departement;

}
