package com.qr_auth.demo.Classes;

import jakarta.persistence.*;
import lombok.Data;

import javax.lang.model.element.Name;

@Data
@Entity
@Table(name = "classe")
public class Classe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomClasse;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Departement dep;
}
