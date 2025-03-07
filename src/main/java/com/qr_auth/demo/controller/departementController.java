package com.qr_auth.demo.controller;


import com.qr_auth.demo.Classes.Departement;
import com.qr_auth.demo.services.departementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:4200/")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/v1/qr_auth/departement")
public class departementController {

    @Autowired
    private departementService departementService;

    @GetMapping
    private Iterable<Departement> getDepartements() {
        return departementService.findAllDepartements();
    }

    @GetMapping("getbyName/{name}")
    private Optional<Departement> getDepartementByName(@PathVariable String name) {
        return departementService.findDepartementByName(name);
    }

    @GetMapping("getbyId/{id}")
    private Optional<Departement> getDepartementById(@PathVariable Long id) {
        return departementService.findDepartementById(id);
    }

    @DeleteMapping("delete/{id}")
    private void deleteDepartementById(@PathVariable Long id) {
        departementService.daleteDepartementById(id);
    }

    @PostMapping("save")
    private Departement saveDepartement(@RequestBody Departement departement) {
        return departementService.saveDepartement(departement);
    }

    @PostMapping("update/{id}")
    private void updateDepartement(@PathVariable Long id, @RequestBody Departement departement) {
        Optional<Departement> departementOptional = departementService.findDepartementById(id);
        if (departementOptional.isPresent()) {
            departementService.saveDepartement(departement);
        }else{
            throw new RuntimeException("Departement not found");
        }
    }
}
