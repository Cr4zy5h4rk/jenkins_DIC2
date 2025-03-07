package com.qr_auth.demo.controller;

import com.qr_auth.demo.Classes.Planning;
import com.qr_auth.demo.services.planningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:4200/")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/v1/qr_auth/users/planning")
public class planningController {

    private final planningService planningService;

    public planningController(planningService planningService) {
        this.planningService = planningService;
    }

    @GetMapping
    private List<Planning> getPlannings() {
        return planningService.findAll();
    }

    @PostMapping("save")
    private Planning savePlanning(@RequestBody Planning planning) {
        return planningService.save(planning);
    }

    @PostMapping("{id}")
    private Optional<Planning> getPlanning(@RequestBody Long id) {
        return planningService.getById(id);
    }

    @DeleteMapping("delete/{id}")
    private void deletePlanning(@PathVariable Long id) {
        planningService.deleteById(id);
    }

    @GetMapping("classe/{id}")
    private List<Planning> getPlannings(@PathVariable Long id) {
        return planningService.getAllPlanningsByClasse(id);
    }

    @GetMapping("currentPlanningEleve")
    private Planning findCurrentPlanning(@RequestParam String id, @RequestParam String dateScan) {
        LocalDateTime date = LocalDateTime.parse(dateScan);
        return  planningService.getCurrentPlanningEleve(id,date);
    }

    @GetMapping("currentPlanningProf")
    private Planning findCurrentPlanningProf(@RequestParam String id, @RequestParam String dateScan) {
        LocalDateTime date = LocalDateTime.parse(dateScan);
        return  planningService.getCurrentPlanningProf(id,date);
    }

    @GetMapping("DailyPlanningProf")
    private List<Planning> findDailyPlanning(@RequestParam String id, @RequestParam String dateScan) {
        LocalDate date = LocalDate.parse(dateScan);
        return planningService.DailyPlanningProf(id,date);
    }
    @GetMapping("prof/{id}")
    private List<Planning> findPlannings(@PathVariable String id) {
        return planningService.getAllPlanningsByProf(id);
    }

}
