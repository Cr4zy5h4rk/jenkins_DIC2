package com.qr_auth.demo.services;


import com.qr_auth.demo.Classes.Scanner;
import com.qr_auth.demo.repository.scannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class scannerService {

    @Autowired
    private scannerRepository scannerRepository;

    public Scanner save(Scanner scanner) {
        return scannerRepository.save(scanner);
    }

    public Optional<Scanner> findScannerById(final Long id) {
        return scannerRepository.findById(id);
    }
    public List<Scanner> findAllScanners() {
        return scannerRepository.findAll();
    }

    public void deleteScannerById(final Long id) {
        scannerRepository.deleteById(id);
    }
}
