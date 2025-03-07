package com.qr_auth.demo.services;

import com.qr_auth.demo.Classes.Planning;
import com.qr_auth.demo.Classes.Utilisateur;
import com.qr_auth.demo.repository.UtilisateurRepository;
import com.qr_auth.demo.repository.planningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class planningService {

    private final UtilisateurRepository userRepo;

    private final planningRepository planningRepo;

    public planningService(UtilisateurRepository userRepo, planningRepository planningRepo) {
        this.userRepo = userRepo;
        this.planningRepo = planningRepo;
    }

    public Planning getCurrentPlanningEleve(String idStudent, LocalDateTime dateScan) {

        if(userRepo.findUserByEmail(idStudent).isPresent())
        {
            Utilisateur s=userRepo.findUserByEmail(idStudent).get();
            Long id=s.getClasse().getId();
            Planning planningPrevu=planningRepo.findCurrentPlanning(id,dateScan);
            if (planningPrevu == null) {
                throw new IllegalStateException("Aucun cours prévu dans cette salle à cette heure.");
            }
            return planningRepo.findCurrentPlanning(id,dateScan);
        }else{
            throw new IllegalArgumentException("no student found");
        }
    }

    public Planning getCurrentPlanningProf(String idProf, LocalDateTime dateScan) {
        return planningRepo.findCurrentPlanningByProf(idProf, dateScan);
    }

    public List<Planning> DailyPlanningProf(String idProf, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        return planningRepo.findPlanningOfTheDayByProf(idProf,startOfDay, endOfDay);
    }

    public List<Planning> getAllPlanningsByProf(String idProf) {
        return planningRepo.getPlanningsByProfesseur_Email(idProf);
    }

    public List<Planning> getAllPlanningsByClasse(Long idClasse) {
        return planningRepo.findByClasse_Id(idClasse);
    }

    public Planning save(Planning planning) {
        return planningRepo.save(planning);
    }

    public List<Planning> findAll() {
        return planningRepo.findAll();
    }

    public Optional<Planning> getById(final Long id) {
        return planningRepo.findById(id);
    }

    public void deleteById(final Long id) {
        planningRepo.deleteById(id);
    }
}
