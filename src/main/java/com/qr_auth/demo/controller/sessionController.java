package com.qr_auth.demo.controller;

import com.qr_auth.demo.Classes.Classe;
import com.qr_auth.demo.Classes.SessionCours;
import com.qr_auth.demo.Classes.Utilisateur;
import com.qr_auth.demo.repository.scanUserRepository;
import com.qr_auth.demo.repository.sessionCoursRepository;
import com.qr_auth.demo.services.sessionCoursService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:4200/")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/v1/qr_auth/sessions")
public class sessionController {

    private final sessionCoursRepository sessionCoursRepo;
    private final scanUserRepository scanUserRepository;
    private final sessionCoursService sessionCoursService;

    public sessionController(
            sessionCoursRepository sessionCoursRepo,
            scanUserRepository scanUserRepository, com.qr_auth.demo.services.sessionCoursService sessionCoursService
    ) {
        this.sessionCoursRepo = sessionCoursRepo;
        this.scanUserRepository = scanUserRepository;
        this.sessionCoursService = sessionCoursService;
    }

    @GetMapping("{classe}")
    private List<SessionCours> getSessionsByClasse(@PathVariable String classe) {
        return sessionCoursRepo.findByClasse(classe);
    }

    @GetMapping("absents/{idSession}")
    private List<Utilisateur> getAbsentsBySession(@PathVariable Long idSession) {
        return scanUserRepository.getAbsentStudentBySession(idSession);
    }

    @GetMapping("sessionsDuJour")
    private List<SessionCours> getSessionsDuJour(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.plusDays(1).atStartOfDay();
        return sessionCoursRepo.findSessionCoursByDay(startOfDay, endOfDay);
    }


    @GetMapping("/last-by-classe")
    public ResponseEntity<List<SessionCours>> getLatestSessionsByClasse() {
        List<SessionCours> sessions = sessionCoursService.getLatestSessionsByClasse();
        return ResponseEntity.ok(sessions);
    }
}
