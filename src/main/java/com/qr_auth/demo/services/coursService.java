package com.qr_auth.demo.services;

import com.qr_auth.demo.Classes.Cours;
import com.qr_auth.demo.repository.coursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class coursService {

    @Autowired
    private coursRepository coursRepo;

    public Cours saveCours(Cours cours) {

        return coursRepo.save(cours);
    }

    public List<Cours> getAllCours(){
        return coursRepo.findAll();
    }

    public Optional<Cours> getCoursById(final Long id) {
        return coursRepo.findById(id);
    }
    public void deleteCours(final Long id) {
        coursRepo.deleteById(id);
    }
}
