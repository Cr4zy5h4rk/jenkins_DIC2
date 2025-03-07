package com.qr_auth.demo.controller;

import com.qr_auth.demo.Classes.Cours;
import com.qr_auth.demo.repository.coursRepository;
import com.qr_auth.demo.services.coursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:4200/")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/v1/qr_auth/cours")
public class CoursController {

    private final coursRepository coursrepository;

    @Autowired
    private coursService coursService;

    public CoursController(coursRepository coursrepository) {
        this.coursrepository = coursrepository;
    }

    @GetMapping
    private Iterable<Cours> getAllCours() {
        return coursService.getAllCours();
    }

    @GetMapping("{id}")
    private Optional<Cours> getCours(@PathVariable Long id) {
        return coursService.getCoursById(id);
    }

    @PostMapping("save")
    private Cours saveCours(@RequestBody Cours cours) {

            return coursService.saveCours(cours);
    }

    @DeleteMapping("delete/{id}")
    private void deleteCours(@PathVariable Long id) {
        coursService.deleteCours(id);
    }

    @PostMapping("update")
    private void updateCours(@RequestBody Cours cours) {
        Optional<Cours> coursOptional = coursService.getCoursById(cours.getId());
        if (coursOptional.isPresent()) {
            coursOptional.get().setNomCours(cours.getNomCours());
            coursOptional.get().setProfesseur(cours.getProfesseur());
            coursService.saveCours(coursOptional.get());
        }else {
            throw new RuntimeException("Cours not found");
        }
    }

    @GetMapping("professeur")
    private List<Cours> getByProfMail(String profMail) {
        return coursrepository.findByProfesseurEmail(profMail);
    }

}
