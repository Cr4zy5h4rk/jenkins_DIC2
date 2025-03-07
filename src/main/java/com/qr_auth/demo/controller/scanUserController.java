package com.qr_auth.demo.controller;


import com.qr_auth.demo.Classes.scan_user;
import com.qr_auth.demo.services.dto.JustificationRequest;
import com.qr_auth.demo.services.dto.RaisonRequest;
import com.qr_auth.demo.services.scanService;
import com.qr_auth.demo.repository.scanUserRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:4200/")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/v1/qr_auth/scan")
public class scanUserController {

    private final scanService scanService;

    private final scanUserRepository scanUserRepository;

    public scanUserController(
            scanService scanService,
            scanUserRepository scanUserRepository
    ) {

        this.scanService = scanService;
        this.scanUserRepository = scanUserRepository;
    }


    @GetMapping("newScanTest")
    private scan_user getOrCreateTempSessionEleve(@RequestParam String id, @RequestParam String Date) {
        LocalDateTime date = LocalDateTime.parse(Date);
        return scanService.saveScanUser(id,date);
    }

    @GetMapping("session/{id}")
    private List<scan_user> getScanUsers(@PathVariable Long id) {
        return scanUserRepository.findBySessionCours_Id(id);
    }

    @GetMapping("Historique/{email}")
    private List<scan_user> getScanUsersByEmail(@PathVariable String email) {
        return scanService.getAllScan(email);
    }

    @PostMapping("Justification")
    private void Justification(@RequestBody JustificationRequest justificationRequest) {
        scanService.AcceptJustification(justificationRequest);
    }

    @PostMapping("CreateJustification")
    private void CreateJustification(@RequestBody RaisonRequest raisonRequest) {
        scanService.CreateJustification(raisonRequest);
    }

    @GetMapping("AbsencesJustifiees")
    private List<scan_user> AbsencesJustifiee(String mail) {
        return scanService.getJustifiees(mail);
    }

    @GetMapping("AbsencesNonJustifiees")
    private List<scan_user> AbsencesNonJustifiee(String mail) {
        return scanService.getNonJustifiees(mail);
    }

    @GetMapping("AbsencesEnCoursDeJustification")
    private List<scan_user> AbsencesEnCoursDeJustification(String mail) {
        return scanService.getEnCoursDeJustification(mail);
    }

    @GetMapping("AbsencesByJUSTIFIEEBetweenDate")
    private List<scan_user> AbsencesByJustificationBetweenDate(String mail, String start, String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return scanUserRepository.findAbsenceScansByStudentJustificationJUSTIFIEEBetweenDate(mail, startDate, endDate);
    }

    @GetMapping("AbsencesByEN_COURSBetweenDate")
    private List<scan_user> AbsencesByJustificationEN_COURSBetweenDate(String mail, String start, String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return scanUserRepository.findAbsenceScansByStudentJustificationEN_COURSBetweenDate(mail, startDate, endDate);
    }

    @GetMapping("AbsencesByNON_JUSTIFIEEBetweenDate")
    private List<scan_user> NON_JUSTIFIEEBetweenDate(String mail, String start, String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return scanUserRepository.findAbsenceScansByStudentJustificationNON_JUSTIFIEEBetweenDate(mail, startDate, endDate);
    }

    @GetMapping("nbreRetard/{email}")
    private Long getNbreRetard(@PathVariable String email) {
        return scanUserRepository.getRetardCount(email);
    }

    @GetMapping("nbrePresence/{email}")
    private Long getNbrePresence(@PathVariable String email) {
        return scanUserRepository.getPresenceCount(email);
    }

    @GetMapping("nbreAbsence/{email}")
    private Long getNbreAbsence(@PathVariable String email, @RequestParam String start, @RequestParam String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return scanUserRepository.getAbsenceCountBetweenDate(email, startDate, endDate);
    }

    @GetMapping("nombreHeure")
    private Double getNombreHeure(@RequestParam String email, @RequestParam String dateDebut, @RequestParam String dateFin) {
        return scanService.calculateAbsenceHours(email, dateDebut, dateFin);
    }

    @GetMapping("scanPresentsBySession/{sessionId}")
    private List<scan_user> getPresentsBySession(@PathVariable Long sessionId) {
        return scanUserRepository.findBySessionCours_IdAndPresence(sessionId);
    }

    @GetMapping("scanAbsentsBySession/{sessionId}")
    private List<scan_user> getAbsentsBySession(@PathVariable Long sessionId) {
        return scanUserRepository.findBySessionCours_IdAndPresence(sessionId);
    }
}
