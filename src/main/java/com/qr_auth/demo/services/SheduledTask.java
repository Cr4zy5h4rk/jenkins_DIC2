package com.qr_auth.demo.services;


import com.qr_auth.demo.Classes.Planning;
import com.qr_auth.demo.Classes.SessionCours;
import com.qr_auth.demo.Classes.Utilisateur;
import com.qr_auth.demo.Classes.scan_user;
import com.qr_auth.demo.enums.EStatusCours;
import com.qr_auth.demo.enums.EStatutFinCours;
import com.qr_auth.demo.enums.EStatutJustification;
import com.qr_auth.demo.enums.EStatutPresence;
import com.qr_auth.demo.repository.scanUserRepository;
import com.qr_auth.demo.repository.sessionCoursRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SheduledTask {

    private final sessionCoursRepository sessionCoursRepository;

    private final scanUserRepository scanUserRepository;

    public SheduledTask(
            sessionCoursRepository sessionCoursRepository,
            scanUserRepository scanUserRepository
    ) {
        this.sessionCoursRepository = sessionCoursRepository;
        this.scanUserRepository = scanUserRepository;
    }

    @Scheduled(cron = "0 */46 * * * MON-SAT")
    void closeOpenSession() {
        LocalDateTime now = LocalDateTime.now();

        // Récupérer toutes les sessions non terminées
        List<SessionCours> openSessions = sessionCoursRepository.findSessionNotClosed();

        for (SessionCours session : openSessions) {
            Planning planning = session.getPlanningAssocie();
            LocalDateTime finPrevue = planning.getHeureFinPrevue();

            // Si l'heure de fin prévue est dépassée de plus de 30 minutes
            if (now.isAfter(finPrevue.plusMinutes(45))) {
                // Clôturer automatiquement la session
                session.setSessionEnd(true);
                session.setHeureFinEffective(finPrevue);
                session.setStatutFinCours(EStatutFinCours.AUTO_CLOSED);

                // Calculer la durée prévue du cours
                LocalDateTime heureDebut = planning.getHeureDebutPrevue();
                long duree_cours = Duration.between(heureDebut, finPrevue).toHours();

                // Mettre à jour les absences
                scanUserRepository.updateAbsenceCountForSession(session.getId(), duree_cours);

                // Sauvegarder les modifications
                sessionCoursRepository.save(session);
            }
        }
    }

    @Scheduled(cron = "0 */5 * * * *")
    void closeSessionsWithNoProfs(){
        LocalDateTime now = LocalDateTime.now();

        List<SessionCours> sessionNoProfs = sessionCoursRepository.findSessionWithNoProf(now);
        System.out.println("list on date " + now +" : " + sessionNoProfs.size());
        for (SessionCours session : sessionNoProfs) {

            Utilisateur prof = session.getPlanningAssocie().getProfesseur();
            scan_user scanAbsent = new scan_user();
            scanAbsent.setSessionCours(session);
            scanAbsent.setUser(prof);
            scanAbsent.setScan_date(session.getHeureFinEffective());
            scanAbsent.setPresence(EStatutPresence.ABSENCE);
            scanAbsent.setJustification(EStatutJustification.NON_JUSTIFIEE);
            scanUserRepository.save(scanAbsent);

            session.setHeureFinEffective(now);
            session.setSessionEnd(true);
            session.setSessionStart(false);
            session.setStatutFinCours(EStatutFinCours.AUTO_CLOSED);
            session.setStatut(EStatusCours.ANNULE);
            // Sauvegarder les modifications
            sessionCoursRepository.save(session);
        }


    }
}
