package com.qr_auth.demo.services;

import com.qr_auth.demo.Classes.Planning;
import com.qr_auth.demo.Classes.SessionCours;
import com.qr_auth.demo.Classes.Utilisateur;
import com.qr_auth.demo.repository.UtilisateurRepository;
import com.qr_auth.demo.repository.planningRepository;
import com.qr_auth.demo.repository.sessionCoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class sessionCoursService {

    private final sessionCoursRepository sessionrepo;


    private final planningRepository planningRepo;

    private final WebSocketNotificationService notificationService;

    private final UtilisateurRepository utilisateurRepository;

    public sessionCoursService(
            WebSocketNotificationService notificationService,
            sessionCoursRepository sessionrepo,
            planningRepository planningRepo,
            UtilisateurRepository utilisateurRepository
    ) {
        this.sessionrepo = sessionrepo;
        this.planningRepo = planningRepo;
        this.utilisateurRepository = utilisateurRepository;
        this.notificationService = notificationService;
    }

    public SessionCours getOrCreateTempSession(String idUser, LocalDateTime dateScan) {

        if (utilisateurRepository.findById(idUser).isPresent()) {
            Utilisateur u = utilisateurRepository.findById(idUser).get();
            Long IdClass=u.getClasse().getId();
//            Planning planningPrevu=planningService.getCurrentPlanningEleve(idUser,dateScan);
            Planning planningPrevu=planningRepo.findCurrentPlanning(IdClass,dateScan);
            if(planningPrevu==null){
                throw new IllegalArgumentException("t'as pas de cours mec");
            }
            SessionCours session=sessionrepo.FindByDateAndClasse(dateScan, planningPrevu.getCours());;
            if (session==null){
                session = new SessionCours();
                session.setDate(dateScan);
                session.setPlanningAssocie(planningPrevu);
                session = sessionrepo.save(session);
                notificationService.notifySessionChanges(session, "CREATE");
            }

            return session;
        }else{
            throw new IllegalArgumentException("user not found");
        }
    }

    public void setEndSession() {
    }


    public SessionCours saveSessionCours(SessionCours sessionCours) {
        return sessionrepo.save(sessionCours);
    }

    public Optional<SessionCours> getSessionCoursById(final Long id) {
        return sessionrepo.findById(id);
    }



    public List<SessionCours> getAllSessionCours() {
        return sessionrepo.findAll();
    }

    public void deleteSessionCours(final Long id) {
        sessionrepo.deleteById(id);
    }

    public List<SessionCours> getLatestSessionsByClasse() {
        return sessionrepo.findLatestSessionsByClasse();
    }

}
