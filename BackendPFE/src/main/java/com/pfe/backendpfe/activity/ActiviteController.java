package com.pfe.backendpfe.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activites")public class ActiviteController {
    private final ActiviteService activiteService;

    // Constructor injection for ActiviteService
    @Autowired
    public ActiviteController(ActiviteService activiteService) {
        this.activiteService = activiteService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping
    public List<Activite> getAllActivities() {
        return activiteService.getAllActivity();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public Activite getActivityById(@PathVariable Long id) {
        return activiteService.getActivityById(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public Activite createActivity(@RequestBody Activite activite) {
        return activiteService.saveActivity(activite);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteActivity(@PathVariable Long id) {
        activiteService.deleteActivite(id);
    }
    @PutMapping("/{id}")
    public Activite updateActivity(@PathVariable Long id, @RequestBody Activite activite) {
        return activiteService.updateActivity(id, activite);
    }

}
