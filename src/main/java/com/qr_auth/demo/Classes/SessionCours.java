package com.qr_auth.demo.Classes;

import com.qr_auth.demo.enums.EStatusCours;
import com.qr_auth.demo.enums.EStatutFinCours;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "session_cours")
public class SessionCours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;
    private LocalDateTime heureDebutEffective;
    private LocalDateTime heureFinEffective;

    @ManyToOne
    @JoinColumn(name = "planning_id")
    private Planning planningAssocie;

    private Boolean sessionStart = false;

    private Boolean sessionEnd = false;

    private EStatusCours statut;
    private EStatutFinCours statutFinCours;
}
