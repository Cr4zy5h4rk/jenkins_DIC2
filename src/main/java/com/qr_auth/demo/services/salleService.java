package com.qr_auth.demo.services;

import com.qr_auth.demo.Classes.Salle;
import com.qr_auth.demo.repository.salleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class salleService {

    @Autowired
    private salleRepository sallerepo;

    public void saveSalle(Salle salle) {
        sallerepo.save(salle);
    }

    public List<Salle> getAllSalles() {
        return sallerepo.findAll();
    }

    public Optional<Salle> getSalleById(final Long id) {
        return sallerepo.findById(id);
    }
    public void deleteSalleById(final Long id) {
        sallerepo.deleteById(id);
    }
}
