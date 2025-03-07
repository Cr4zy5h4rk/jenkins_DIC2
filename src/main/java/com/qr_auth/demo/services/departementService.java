package com.qr_auth.demo.services;

import com.qr_auth.demo.Classes.Departement;
import com.qr_auth.demo.repository.departementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class departementService {

    @Autowired
    private departementRepository deprepo;

    public Departement saveDepartement(Departement departement) {
        return deprepo.save(departement);
    }

    public Optional<Departement> findDepartementById(final Long id) {
        return deprepo.findById(id);
    }
    public Optional<Departement> findDepartementByName(String name) {
        return deprepo.findByName(name);
    }

    public Iterable<Departement> findAllDepartements() {
        return deprepo.findAll();
    }

    public void daleteDepartementById(final Long id) {
        deprepo.deleteById(id);
    }
}
