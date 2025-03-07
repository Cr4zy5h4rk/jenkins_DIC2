package com.qr_auth.demo.controller;

import com.qr_auth.demo.Classes.Classe;
import com.qr_auth.demo.services.classeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:4200/")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/v1/qr_auth/classe")
public class classeController {

    @Autowired
    private classeService classeService;

    @GetMapping
    private Iterable<Classe> getClasses() {
        return classeService.findAllClasses();
    }

    @GetMapping("{id}")
    private Optional<Classe> getClasse(@PathVariable Long id) {
        return classeService.findClasse(id);
    }

    @DeleteMapping("delete/{id}")
    private void deleteClasse(@PathVariable Long id) {
        classeService.deleteClass(id);
    }

    @PostMapping("save")
    private void saveClasse(@RequestBody Classe classe) {
        classeService.saveClasse(classe);
    }

    @PostMapping("update/{id}")
    private void updateClasse(@PathVariable Long id, @RequestBody Classe classe) {
        Optional<Classe> classeOptional = classeService.findClasse(id);
        if(classeOptional.isPresent()) {
            classeOptional.get().setNomClasse(classe.getNomClasse());
            classeOptional.get().setDep(classe.getDep());
            classeService.saveClasse(classeOptional.get());
        }else {
            throw new RuntimeException("Classe not found");
        }
    }

}
