package com.qr_auth.demo.services;

import com.qr_auth.demo.Classes.Classe;
import com.qr_auth.demo.repository.classeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class classeService {

    @Autowired
    private classeRepository classeRepo;

    public Classe saveClasse(Classe classe) {
        return classeRepo.save(classe);
    }

    public Optional<Classe> findClasse(final Long id) {
        return classeRepo.findById(id);
    }

    public Iterable<Classe> findAllClasses() {
        return classeRepo.findAll();
    }
    public void deleteClass(final Long id) {
        classeRepo.deleteById(id);
    }
}
