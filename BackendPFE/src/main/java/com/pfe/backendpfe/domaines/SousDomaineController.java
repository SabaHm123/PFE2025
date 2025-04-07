package com.pfe.backendpfe.domaines;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/sous-domaines")
public class SousDomaineController {

    @Autowired
    private SousDomaineService sousDomaineService;
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/{domaineId}")
    public ResponseEntity<?> ajouterSousDomaine(
            @PathVariable Long domaineId,
            @RequestBody SousDomaine sousDomaine) {
        System.out.println("Domaine ID reçu : " + domaineId);

        return ResponseEntity.ok(sousDomaineService.createSousDomaine(sousDomaine, domaineId));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/domaine/{domaineId}")
    public List<SousDomaine> getSousDomainesByDomaine(@PathVariable Long domaineId) {
        return sousDomaineService.getSousDomainesByDomaine(domaineId);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping
    public List<SousDomaine> getAllSousDomaines() {
        return sousDomaineService.getAllSousDomaines();
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public SousDomaine updateSousDomaine(@PathVariable Long id, @RequestBody SousDomaine sousDomaine) {
        return sousDomaineService.updateSousDomaine(id, sousDomaine);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}/domaine/{domaineId}")
    public SousDomaine updateSousDomaineByDomaine(
            @PathVariable Long id,
            @PathVariable Long domaineId,
            @RequestBody SousDomaine sousDomaine) {

        return sousDomaineService.updateSousDomaineByDomaine(id, domaineId, sousDomaine);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/{categoryId}")
    public List<SousDomaine> getSubDomainsByCategory(@PathVariable Long categoryId) {
        return sousDomaineService.getSubDomainsByCategory(categoryId);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping(value = "/addSubdomain")
    public SousDomaine addSubDomain(@RequestBody SousDomaine subDomain) {
        return sousDomaineService.saveSubDomain(subDomain);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/{subDomainId}")
    public ResponseEntity<?> deleteSubDomain(@PathVariable Long subDomainId) {
        sousDomaineService.deleteSubDomain(subDomainId);
        return ResponseEntity.ok().body("Sous-domaine supprimé avec succès");
    }

}
