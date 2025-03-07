package com.qr_auth.demo.repository;

import com.qr_auth.demo.Classes.Utilisateur;
import com.qr_auth.demo.Classes.scan_user;
import com.qr_auth.demo.enums.EStatutJustification;
import com.qr_auth.demo.enums.EStatutPresence;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface scanUserRepository extends JpaRepository<scan_user, Long> {


    @Query("SELECT DISTINCT u FROM Utilisateur u " +
            "JOIN u.classe c " +
            "JOIN SessionCours sc ON sc.id = :idSession " +
            "JOIN sc.planningAssocie p " +
            "WHERE u.classe = p.classe " +
            "AND u.email NOT IN (" +
            "    SELECT se.user.email " +
            "    FROM scan_user se " +
            "    WHERE se.sessionCours.id = :idSession" +
            ")")
    List<Utilisateur> getAbsentStudentBySession(@Param("idSession") Long idSession);

    @Modifying
    @Transactional
    @Query("UPDATE Utilisateur u " +
            "SET u.nbreAbsence = COALESCE(u.nbreAbsence, 0) + 1, u.nbreHeureAbscence = COALESCE(u.nbreHeureAbscence, 0) + :dureeCours, u.nbreHeureAbsenceInjustifie = COALESCE(u.nbreHeureAbsenceInjustifie, 0) + :dureeCours " +
            "WHERE u.classe IN (SELECT p.classe FROM Planning p JOIN SessionCours sc ON sc.planningAssocie = p WHERE sc.id = :idSession) " +
            "AND u.email NOT IN (" +
            "    SELECT se.user.email " +
            "    FROM scan_user se " +
            "    WHERE se.sessionCours.id = :idSession" +
            ")")
    void updateAbsenceCountForSession(@Param("idSession") Long idSession, @Param("dureeCours") long dureeCours);

    @Query(
            "SELECT s.user from scan_user s where s.user.email = :email"
    )
    Optional<Utilisateur> existsscan_user(@Param("email") String email);

    List<scan_user> findByUser(Utilisateur user);


    List<scan_user> findBySessionCours_Id(Long idSession);

    @Query(
            "SELECT s FROM scan_user s " +
                    "WHERE s.user.email = :email AND s.presence = 2 AND s.justification = 0  AND s.scan_date BETWEEN :dateDB AND :dateFIN"
    )
    List<scan_user> findAbsenceScansByStudent(
            @Param("email") String email,
            @Param("dateDB") LocalDateTime dateDB,
            @Param("dateFIN") LocalDateTime dateFIN
    );

    @Query("SELECT s FROM scan_user s WHERE s.scan_date BETWEEN :start AND :end")
    List<scan_user> findBetweenDates(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(
            "SELECT COUNT(s) from scan_user s where s.user.email=:mail and s.presence = 0"
    )
    Long getPresenceCount(@Param("mail") String mail);

    @Query(
            "SELECT COUNT(s) from scan_user s where s.user.email=:mail and s.presence = 1"
    )
    Long getRetardCount(@Param("mail") String mail);

    @Query(
            "SELECT COUNT(s) from scan_user s where s.user.email=:mail and s.presence = 2 and s.scan_date between :start and :end"
    )
    Long getAbsenceCountBetweenDate(
            @Param("mail") String mail,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query(
            "SELECT s from scan_user s where s.user.email=:mail and s.justification=0 AND s.presence = 2"
    )
    List<scan_user> getNON_JUSTIFIEE(@Param("mail") String mail);

    @Query(
            "SELECT s from scan_user s where s.user.email=:mail and s.justification=1 AND s.presence = 2"
    )
    List<scan_user> getEN_COURS_DE_JUSTIFICATION(@Param("mail") String mail);

    @Query(
            "SELECT s from scan_user s where s.user.email=:mail and s.justification=2 AND s.presence = 2"
    )
    List<scan_user> getJUSTIFIEE(@Param("mail") String mail);

    @Query(
            "SELECT s FROM scan_user s " +
                    "WHERE s.user.email = :email AND s.presence = 2 AND s.justification=0 AND s.scan_date BETWEEN :dateDB AND :dateFIN"
    )
    List<scan_user> findAbsenceScansByStudentJustificationNON_JUSTIFIEEBetweenDate(
            @Param("email") String email,
            @Param("dateDB") LocalDateTime dateDB,
            @Param("dateFIN") LocalDateTime dateFIN
    );

    @Query(
            "SELECT s FROM scan_user s " +
                    "WHERE s.user.email = :email AND s.presence = 2 AND s.justification=1 AND s.scan_date BETWEEN :dateDB AND :dateFIN"
    )
    List<scan_user> findAbsenceScansByStudentJustificationEN_COURSBetweenDate(
            @Param("email") String email,
            @Param("dateDB") LocalDateTime dateDB,
            @Param("dateFIN") LocalDateTime dateFIN
    );

    @Query(
            "SELECT s FROM scan_user s " +
                    "WHERE s.user.email = :email AND s.presence = 2 AND s.justification=2 AND s.scan_date BETWEEN :dateDB AND :dateFIN"
    )
    List<scan_user> findAbsenceScansByStudentJustificationJUSTIFIEEBetweenDate(
            @Param("email") String email,
            @Param("dateDB") LocalDateTime dateDB,
            @Param("dateFIN") LocalDateTime dateFIN
    );

    @Query(
            "select s from scan_user s where s.sessionCours.id = :sessionId and s.presence = 2"
    )
    List<scan_user> findBySessionCours_IdAndAbsence(@Param("sessionId") Long sessionId);

    @Query(
            "select s from scan_user s where s.sessionCours.id = :sessionId and s.presence = 0"
    )
    List<scan_user> findBySessionCours_IdAndPresence(@Param("sessionId") Long sessionId);

}
