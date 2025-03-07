package com.qr_auth.demo.Classes;

import com.qr_auth.demo.enums.EStatutJustification;
import com.qr_auth.demo.enums.EStatutPresence;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "scan_user")
public class scan_user {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime scan_date;

    @ManyToOne
    @JoinColumn(name = "user_scan_id")
    private Utilisateur user;

    @ManyToOne
    @JoinColumn(name = "scanner_id")
    private Scanner scanner;

    private EStatutPresence presence;

    private EStatutJustification justification;

    private String raison;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private SessionCours sessionCours;
}
