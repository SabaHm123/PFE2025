package com.pfe.backendpfe.domaines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/domaines")
public class DomaineController {

    @Autowired
    private DomaineService domaineService;
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public Domaine createDomaine(@RequestBody Domaine domaine) {
        return domaineService.createDomaine(domaine);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping
    public List<Domaine> getAllDomaines() {
        return domaineService.getAllDomaines();
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public Domaine getDomaineById(@PathVariable Long id) {
        return domaineService.getDomaineById(id);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteDomaine(@PathVariable Long id) {
        domaineService.deleteDomaine(id);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public Domaine updateDomaine(@PathVariable Long id, @RequestBody Domaine updatedDomaine) {
        return domaineService.updateDomaine(id, updatedDomaine);
    }
    @PostMapping("/{domaineId}/sous-domaines")
    public Domaine addSousDomaine(@PathVariable Long domaineId, @RequestBody SousDomaine sousDomaine) {
        return domaineService.addSousDomaineToDomaine(domaineId, sousDomaine);
    }
}
