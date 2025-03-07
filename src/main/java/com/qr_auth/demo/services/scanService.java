package com.qr_auth.demo.services;

import com.qr_auth.demo.Classes.*;
import com.qr_auth.demo.enums.EStatusCours;
import com.qr_auth.demo.enums.EStatutFinCours;
import com.qr_auth.demo.enums.EStatutJustification;
import com.qr_auth.demo.enums.EStatutPresence;
import com.qr_auth.demo.repository.UtilisateurRepository;
import com.qr_auth.demo.repository.planningRepository;
import com.qr_auth.demo.repository.scanUserRepository;
import com.qr_auth.demo.repository.sessionCoursRepository;
import com.qr_auth.demo.services.dto.JustificationRequest;
import com.qr_auth.demo.services.dto.RaisonRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class scanService {

    private final sessionCoursService sessionCoursService;

    private final planningRepository planningRepo;

    private final WebSocketNotificationService webSocketNotificationService;

    @Autowired
    private scanUserRepository scanUserRepository;


    @Autowired
    private sessionCoursRepository sessionCoursRepo;

    @Autowired
    private UtilisateurRepository userRepo;

    public scanService(
            WebSocketNotificationService webSocketNotificationService,
            sessionCoursService sessionCoursService,
            planningRepository planningRepo

    ) {
        this.webSocketNotificationService = webSocketNotificationService;
        this.sessionCoursService = sessionCoursService;
        this.planningRepo = planningRepo;
    }


    public scan_user saveScanUser(String UserId, LocalDateTime dateScan) {

        if(userRepo.findById(UserId).isPresent()) {
            Utilisateur user = userRepo.findById(UserId).get();
            if(user.getProfile().getLibelle().equals("Etudiant")){
                SessionCours sessionCours = sessionCoursService.getOrCreateTempSession(user.getEmail(),dateScan);
                if(!sessionCours.getSessionEnd()){ //si la session precedente n'est pas encore terminee alors l'etudiant ne pourra pas scanner pour une autre session
                    scan_user scanEleve = new scan_user();
                    scanEleve.setSessionCours(sessionCours);
                    scanEleve.setScan_date(dateScan);
                    scanEleve.setUser(user);


                    LocalDateTime timeMax=sessionCours.getPlanningAssocie().getHeureDebutPrevue().plusMinutes(15);
                    if (dateScan.isAfter(timeMax)) {
                        scanEleve.setPresence(EStatutPresence.RETARD);
                    }else{
                        scanEleve.setPresence(EStatutPresence.PRESENCE);
                    }
                    webSocketNotificationService.notifyScanChanges(scanEleve, "CREATE");
                    return scanUserRepository.save(scanEleve);
                }else{
                    throw new IllegalArgumentException("Cette session est terminee");
                }

            } else if (user.getProfile().getLibelle().equals("Professeur")) {
                Planning ProfPlanningPrevu=planningRepo.findCurrentPlanningByProf(user.getEmail(),dateScan);
                if (ProfPlanningPrevu==null) {
                    throw new IllegalArgumentException("Vous n.avez pas de cours dans votre Planning pour le moment");
                }

                SessionCours sessionCours = sessionCoursRepo.FindByDateAndClasse(dateScan, ProfPlanningPrevu.getCours());
                LocalDateTime timeMax=ProfPlanningPrevu.getHeureDebutPrevue().plusMinutes(15);
                LocalDateTime finCoursTime = ProfPlanningPrevu.getHeureFinPrevue();

                if (sessionCours == null) {
                    sessionCours = new SessionCours();
                    sessionCours.setDate(dateScan);
                    if (dateScan.isAfter(timeMax)) {
                        sessionCours.setStatut(EStatusCours.EN_RETARD);
                    } else {
                        sessionCours.setStatut(EStatusCours.A_LHEURE);
                    }
                    if(dateScan.isAfter(ProfPlanningPrevu.getHeureDebutPrevue())){
                        sessionCours.setHeureDebutEffective(dateScan);
                    }else{
                        sessionCours.setHeureDebutEffective(ProfPlanningPrevu.getHeureDebutPrevue());
                    }
                    sessionCours.setSessionStart(true);
                    sessionCours.setPlanningAssocie(ProfPlanningPrevu);
                    sessionCoursRepo.save(sessionCours);
                        webSocketNotificationService.notifySessionChanges(sessionCours, "CREATE");
                }else{
                    if (sessionCours.getSessionEnd()) {
                        throw new IllegalArgumentException("Le cours est termine");
                    }

                    if (sessionCours.getSessionStart()){ //si la session a deja demarree, le prochain scan du prof mettra fin a cette session
                        sessionCours.setHeureFinEffective(dateScan);
                        sessionCours.setSessionEnd(true);

                        LocalDateTime heureDebut = ProfPlanningPrevu.getHeureDebutPrevue();
                        LocalDateTime heureFin = ProfPlanningPrevu.getHeureFinPrevue();
                        long duree_cours = Duration.between(heureDebut, heureFin).toHours();
                        scanUserRepository.updateAbsenceCountForSession(sessionCours.getId(),duree_cours); //MARQUE LES ELEVES NON presents dans la session comme absent et incremente leur compteur


                        if (dateScan.isAfter(finCoursTime.plusMinutes(15))) {
                            sessionCours.setStatutFinCours(EStatutFinCours.PROLONGE);
                        } else if (dateScan.isBefore(finCoursTime.minusMinutes(30))) {
                            sessionCours.setStatutFinCours(EStatutFinCours.ANTICIPE);
                        }else{
                            sessionCours.setStatutFinCours(EStatutFinCours.NORMAL);
                        }

                        createAbsentScansForSession(sessionCours);

                    }else{
                        sessionCours.setSessionStart(true); //Demarrer la session des que le prof scanne
                        if (dateScan.isAfter(timeMax)) {
                            sessionCours.setStatut(EStatusCours.EN_RETARD);
                        } else {
                            sessionCours.setStatut(EStatusCours.A_LHEURE);
                        }
                        if(dateScan.isAfter(sessionCours.getPlanningAssocie().getHeureDebutPrevue())){
                            sessionCours.setHeureDebutEffective(dateScan);
                        }else{
                            sessionCours.setHeureDebutEffective(ProfPlanningPrevu.getHeureDebutPrevue());
                        }
                    }
                    webSocketNotificationService.notifySessionChanges(sessionCours,"UPDATE");
                    sessionCoursRepo.save(sessionCours);
                }
                scan_user scanProf = new scan_user();
                scanProf.setSessionCours(sessionCours);
                scanProf.setScan_date(dateScan);
                scanProf.setUser(user);
                if(scanUserRepository.existsscan_user(user.getEmail()).isPresent()){
                    scanProf.setPresence(EStatutPresence.FIN_COURS);
                }else{
                    if (dateScan.isAfter(timeMax)) {
                        scanProf.setPresence(EStatutPresence.RETARD);
                        sessionCours.setStatut(EStatusCours.EN_RETARD);
                    }else{
                        scanProf.setPresence(EStatutPresence.PRESENCE);
                        sessionCours.setStatut(EStatusCours.A_LHEURE);
                    }
                }

                webSocketNotificationService.notifyScanChanges(scanProf,"CREATE");
                return scanUserRepository.save(scanProf);
            }else{
                throw new IllegalArgumentException("User profile not found");
            }
        }
            else{
            throw new IllegalArgumentException("user not found");
        }
    }

    public void createAbsentScansForSession(SessionCours sessionCours) {
        List<Utilisateur> absentStudents = scanUserRepository.getAbsentStudentBySession(sessionCours.getId());

        LocalDateTime heureDebut = sessionCours.getPlanningAssocie().getHeureDebutPrevue();
        LocalDateTime heureFin = sessionCours.getPlanningAssocie().getHeureFinPrevue();
        Integer duree_cours = (int)Duration.between(heureDebut, heureFin).toHours();

        // Créer des entrées scan_user pour chaque étudiant absent
        List<scan_user> absentScans = absentStudents.stream()
                .map(student -> {
                    scan_user scanAbsent = new scan_user();
                    scanAbsent.setSessionCours(sessionCours);
                    scanAbsent.setUser(student);
                    scanAbsent.setScan_date(sessionCours.getHeureFinEffective());
                    scanAbsent.setPresence(EStatutPresence.ABSENCE);
                    scanAbsent.setJustification(EStatutJustification.NON_JUSTIFIEE);

                    student.setNbreAbsence(student.getNbreAbsence()+1);
                    student.setNbreHeureAbscence(student.getNbreHeureAbscence()+ duree_cours);
                    student.setNbreHeureAbsenceInjustifie(student.getNbreHeureAbsenceInjustifie()+ duree_cours);

                    return scanAbsent;
                })
                .collect(Collectors.toList());

        // Sauvegarder toutes les entrées en une seule opération
        if (!absentScans.isEmpty()) {
            webSocketNotificationService.notifyScanChanges(absentScans.get(0),"CREATE");
            scanUserRepository.saveAll(absentScans);
        }
    }

    public List<scan_user> getAllScan(String Email){
        Optional<Utilisateur> user = userRepo.findById(Email);
        if (user.isPresent()) {
            return scanUserRepository.findByUser(user.get());
        }else {
            throw new IllegalArgumentException("user not found");
        }
    }

    public void AcceptJustification(JustificationRequest justificationRequest){
        Long scanId = justificationRequest.getScanId();
        String email = justificationRequest.getEmail();
        Optional<scan_user> scan = scanUserRepository.findById(scanId);

        if (scan.isPresent()) {
                Utilisateur utilisateur = scan.get().getUser();
                scan_user scanUser = scan.get();
                if (scanUser.getPresence()==EStatutPresence.ABSENCE && utilisateur.getEmail().equals(email)) {

                    scanUser.setJustification(EStatutJustification.JUSTIFIEE);
                    LocalDateTime heureDebut = scanUser.getSessionCours().getPlanningAssocie().getHeureDebutPrevue();
                    LocalDateTime heureFin = scanUser.getSessionCours().getPlanningAssocie().getHeureFinPrevue();
                    Integer duree_cours = (int)Duration.between(heureDebut, heureFin).toHours();

                    if (utilisateur.getNbreHeureAbsenceJustifie() == null) {
                        utilisateur.setNbreHeureAbsenceJustifie(duree_cours);
                    }else{
                        utilisateur.setNbreHeureAbsenceJustifie(utilisateur.getNbreHeureAbsenceJustifie() + duree_cours);
                    }
                    utilisateur.setNbreHeureAbsenceInjustifie(utilisateur.getNbreHeureAbscence() - utilisateur.getNbreHeureAbsenceJustifie());
                    System.out.println("done !!!!!!!!!!");
                    webSocketNotificationService.notifyUserChanges(utilisateur,"UPDATE");
                    userRepo.save(utilisateur);
                    scanUserRepository.save(scanUser);
                }else{
                    throw new IllegalArgumentException("ERROR");
                }


        }else{
            throw new IllegalArgumentException("scan not found");
        }
    }

    public void CreateJustification(RaisonRequest raisonRequest){
        Long scanId = raisonRequest.getScanId();
        String raison = raisonRequest.getRaison();
        Optional<scan_user> scan = scanUserRepository.findById(scanId);

        if (scan.isPresent()) {
            scan_user scanUser = scan.get();
            scanUser.setJustification(EStatutJustification.EN_COURS_DE_JUSTIFICATION);
            if (raison != null) {
                scanUser.setRaison(raison);
            }
            scanUserRepository.save(scanUser);
        }else{
            throw new IllegalArgumentException("scan not found");
        }

    }

    public List<scan_user> getJustifiees(String email){
        Optional<Utilisateur> user = userRepo.findById(email);
        if (user.isPresent()) {
            return scanUserRepository.getJUSTIFIEE(email);
        }else {
            throw new IllegalArgumentException("user not found");
        }
    }

    public List<scan_user> getNonJustifiees(String email){
        Optional<Utilisateur> user = userRepo.findById(email);
        if (user.isPresent()) {
            return scanUserRepository.getNON_JUSTIFIEE(email);
        }else {
            throw new IllegalArgumentException("user not found");
        }
    }

    public List<scan_user> getEnCoursDeJustification(String email){
        Optional<Utilisateur> user = userRepo.findById(email);
        if (user.isPresent()) {
            return scanUserRepository.getEN_COURS_DE_JUSTIFICATION(email);
        }else {
            throw new IllegalArgumentException("user not found");
        }
    }

    public Double calculateAbsenceHours(String email, String dateDB, String dateFIN) {
        LocalDateTime dateDebut = LocalDateTime.parse(dateDB);
        LocalDateTime dateFin = LocalDateTime.parse(dateFIN);
        List<scan_user> scans = scanUserRepository.findAbsenceScansByStudent(email, dateDebut,dateFin);
        System.out.println("scans :" + scans);

        return scans.stream()
                .mapToDouble(scan -> {
                    LocalDateTime debut = scan.getSessionCours().getPlanningAssocie().getHeureDebutPrevue();
                    LocalDateTime fin = scan.getSessionCours().getPlanningAssocie().getHeureFinPrevue();
                    long minutesAbsence = Duration.between(debut, fin).toMinutes();
                    System.out.println("minutesAbsence :" + debut + "fin :" + fin);
                    return minutesAbsence / 60.0;
                })
                .sum();
    }

    public void deleteScanEleveById(final Long id) {
        scanUserRepository.deleteById(id);
    }
}
