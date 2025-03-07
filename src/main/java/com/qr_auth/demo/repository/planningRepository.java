package com.qr_auth.demo.repository;

import com.qr_auth.demo.Classes.Planning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface planningRepository extends JpaRepository<Planning, Long> {


    @Query("SELECT p " +
            "FROM Planning p " +
            "WHERE p.classe.id = :classe_id " +
            "AND :dateDuCours BETWEEN p.heureDebutPrevue - 30 minute AND p.heureFinPrevue")
    Planning findCurrentPlanning(@Param("classe_id") Long classe_id,
                                 @Param("dateDuCours") LocalDateTime dateDuCours);

    List<Planning> findByClasse_Id(Long classe_id);

    List<Planning> getPlanningsByProfesseur_Email(String professeur_id);

    @Query(
            "select p FROM Planning p "+
            "WHERE p.professeur.email= :id AND :dateScan "+
            "BETWEEN p.heureDebutPrevue-30 minute AND p.heureFinPrevue"
    )
    Planning findCurrentPlanningByProf(@Param("id") String id,@Param("dateScan") LocalDateTime dateScan);

    @Query(
            "select p FROM Planning p "+
            "WHERE p.professeur.email= :id "+
            "AND p.heureDebutPrevue >= :startOfDay AND p.heureDebutPrevue < :endOfDay"
    )
    List<Planning> findPlanningOfTheDayByProf(@Param("id") String id,
                                              @Param("startOfDay") LocalDateTime startOfDay,
                                              @Param("endOfDay") LocalDateTime endOfDay);



}
