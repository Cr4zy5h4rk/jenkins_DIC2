package com.qr_auth.demo.repository;

import com.qr_auth.demo.Classes.Classe;
import com.qr_auth.demo.Classes.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {

    Optional<Utilisateur> findUserByEmail(String email);

    @Modifying
    @Transactional
    @Query("delete FROM Utilisateur u where u.email=?1")
    void deleteUserByEmail(String email);

    @Query("SELECT u FROM Utilisateur u WHERE u.profile.libelle='Etudiant'")
    List<Utilisateur> findAllStudent();

    @Query("SELECT u from Utilisateur u where u.profile.libelle='Professeur'")
    List<Utilisateur> findAllProfessor();

    @Query("SELECT p from Utilisateur p where p.profile.libelle='Professeur' and p.email=?1")
    Optional<Utilisateur> findProfByEmail(String email);

    @Query("select p from Utilisateur p where p.departement.nomDepartement = :dep")
    List<Utilisateur> findProfByDep(@Param("dep") String dep);

    @Query("SELECT s from Utilisateur s where s.profile.libelle='Etudiant' and s.email=?1")
    Optional<Utilisateur> findStudentByEmail(String email);

    @Modifying
    @Query("select s from Utilisateur s where s.classe.nomClasse = :classe")
    List<Utilisateur> findStudentsByClasse(@Param("classe") String classe);
}
