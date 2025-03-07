package com.qr_auth.demo.services;

import com.qr_auth.demo.Classes.Profile;
import com.qr_auth.demo.Classes.Utilisateur;
import com.qr_auth.demo.repository.UtilisateurRepository;
import com.qr_auth.demo.repository.profileRepository;
import com.qr_auth.demo.services.dto.EtudiantDTO;
import com.qr_auth.demo.services.dto.ProfDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private WebSocketNotificationService webSocketService;

    private final UtilisateurRepository utilisateurRepository;
    private final profileRepository profileRepository;

    private static final String ALLOWED_DOMAINS =
            "ept.sn";

    public UserService(UtilisateurRepository utilisateurRepository,
                       profileRepository profileRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.profileRepository = profileRepository;
    }


    public Optional<Utilisateur> findProfByEmail(String email) {
        return utilisateurRepository.findProfByEmail(email);

    }

    public Optional<Utilisateur> findStudentByEmail(String email) {
        return utilisateurRepository.findStudentByEmail(email);
    }

    public List<Utilisateur> findAllProf() {
        return utilisateurRepository.findAllProfessor();
    }

    public List<Utilisateur> findAllStudent() {
        return utilisateurRepository.findAllStudent();
    }

    public List<Utilisateur> findStudentByClasse(String classe) {
        return utilisateurRepository.findStudentsByClasse(classe);
    }

    public List<Utilisateur> findProfByDep(String dep) {
        return utilisateurRepository.findProfByDep(dep);
    }



    public Optional<Utilisateur> loadUserByEmail(String email) {
        Optional<Utilisateur> user  = utilisateurRepository.findUserByEmail(email);
        if (user.isPresent()) {
            return user;
        }
        throw new IllegalArgumentException("User not FOUND");
    }

    public Utilisateur saveProf(ProfDTO user) {
        if (!isEmailAllowed(user.getEmail())) {
            throw new IllegalArgumentException("Email not authorized");
        }
        Utilisateur utilisateur = new Utilisateur(
                user.getPrenom(),
                user.getNom(),
                user.getDepartement(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getProfile(),
                user.getMatricule()
        );
        webSocketService.notifyUserChanges(utilisateur, "CREATE");
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur saveEtudiant(EtudiantDTO user){
        if (!isEmailAllowed(user.getEmail())) {
            throw new IllegalArgumentException("Email not authorized");
        }
        Profile studentProfile = profileRepository.findProfileByLibelle("Etudiant")
                .orElseThrow(() -> new RuntimeException("Profil étudiant non trouvé"));
        Utilisateur utilisateur = new Utilisateur(
                user.getPrenom(),
                user.getNom(),
                user.getDepartement(),
                user.getEmail(),
                user.getPhoneNumber(),
                studentProfile,
                user.getClasse(),
                user.getNbrTckPetitDej(),
                user.getNbrTckDej(),
                user.getNbreAbsence()
        );
        return utilisateurRepository.save(utilisateur);
    }

    private boolean isEmailAllowed(String email) {
        String domain = email.split("@")[1];
        return ALLOWED_DOMAINS.contains(domain);
    }



    public void deleteUtilisateurByEmail(String email) {
        Optional<Utilisateur> utilisateur = findStudentByEmail(email);
        if (utilisateur.isPresent()){
           Utilisateur user=utilisateur.get();
            utilisateurRepository.deleteUserByEmail(email);
            webSocketService.notifyUserChanges(user, "DELETE");
        }

    }

    public List<Utilisateur> findAllUsers(){
        return utilisateurRepository.findAll();
    }
}
