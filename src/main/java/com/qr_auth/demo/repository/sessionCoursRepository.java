package com.qr_auth.demo.repository;

import com.qr_auth.demo.Classes.Cours;
import com.qr_auth.demo.Classes.SessionCours;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface sessionCoursRepository extends JpaRepository<SessionCours, Long> {

    @Query(
            "SELECT s FROM SessionCours s WHERE :dateScan BETWEEN s.planningAssocie.heureDebutPrevue - 30 minute AND s.planningAssocie.heureFinPrevue"
    )
    SessionCours FindByDate(@Param("dateScan") LocalDateTime dateScan);

    @Query(
            "SELECT s FROM SessionCours s " +
                    "WHERE s.planningAssocie.cours= :Cours "+
                    "AND :dateScan BETWEEN s.planningAssocie.heureDebutPrevue - 30 minute "+
                    "AND s.planningAssocie.heureFinPrevue " +
                    "AND s.sessionEnd = false "
    )
    SessionCours FindByDateAndClasse(@Param("dateScan") LocalDateTime dateScan, @Param("Cours")Cours Cours);

    @Query(
            "SELECT S FROM SessionCours S WHERE S.planningAssocie.classe.nomClasse = :nomClasse"
    )
    List<SessionCours> findByClasse(@Param("nomClasse") String nomClasse);

    @Query("SELECT s FROM SessionCours s " +
            "WHERE s.date >= :startOfDay AND s.date < :endOfDay")
    List<SessionCours> findSessionCoursByDay(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );



    @Query(
            "SELECT S FROM SessionCours S where S.sessionStart = true and   S.sessionEnd = false"
    )
    List<SessionCours> findSessionNotClosed();

    @Query(
            "SELECT S FROM SessionCours S WHERE S.heureDebutEffective IS NULL AND S.sessionStart = false AND :date> S.planningAssocie.heureDebutPrevue + 20 minute "
    )
    List<SessionCours> findSessionWithNoProf(LocalDateTime date);


    @Query("SELECT s FROM SessionCours s WHERE s.date = (SELECT MAX(s2.date) FROM SessionCours s2 WHERE s2.planningAssocie.classe = s.planningAssocie.classe) AND s.sessionEnd = true ")
    List<SessionCours> findLatestSessionsByClasse();



}
