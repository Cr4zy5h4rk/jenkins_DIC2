package com.qr_auth.demo.controller;


import com.qr_auth.demo.Classes.Utilisateur;
import com.qr_auth.demo.repository.UtilisateurRepository;
import com.qr_auth.demo.services.UserService;
import com.qr_auth.demo.services.dto.EtudiantDTO;
import com.qr_auth.demo.services.dto.ProfDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:4200/")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/v1/qr_auth/users")
public class UserContoller {



    @Autowired
    private UserService userService;

    private final UtilisateurRepository utilisateurRepository;

    public UserContoller(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping("All")
    private Iterable<Utilisateur> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("profs")
    private Iterable<Utilisateur> getProfs() {
        return userService.findAllProf();
    }

    @GetMapping("prof/{email}")
    private Optional<Utilisateur> getProfByEmail(@PathVariable String email) {
        return userService.findProfByEmail(email);
    }

    @GetMapping("prof/departement/{dep}")
    private List<Utilisateur> getProfByDep(@PathVariable String dep) {
        return userService.findProfByDep(dep);
    }


    @GetMapping("etudiants")
    private Iterable<Utilisateur> getAllEtudiants() {
        return userService.findAllStudent();
    }

    @GetMapping("etudiants/{email}")
    private Optional<Utilisateur> getStudentByEmail(@PathVariable String email) {
        return userService.findStudentByEmail(email);
    }

    @GetMapping("etudiants/classe/{classe}")
    private Iterable<Utilisateur> getAllStudentByClasse(@PathVariable String classe) {
        return userService.findStudentByClasse(classe);
    }

    @PostMapping("prof/save")
    private Utilisateur saveProf(@RequestBody ProfDTO prof) {
        return userService.saveProf(prof);
    }

    @PostMapping("etudiants/save")
    private Utilisateur saveStudent(@RequestBody EtudiantDTO utilisateur) {
        return userService.saveEtudiant(utilisateur);
    }

    @DeleteMapping("prof/delete/{email}")
    private void deleteProfByEmail(@PathVariable String email) {
        userService.deleteUtilisateurByEmail(email);
    }

    @GetMapping("{email}")
    private Optional<Utilisateur> getUserByEmail(@PathVariable String email) {
        return utilisateurRepository.findUserByEmail(email);
    }
}
