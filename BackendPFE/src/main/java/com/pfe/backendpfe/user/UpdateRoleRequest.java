package com.pfe.backendpfe.user;

import com.pfe.backendpfe.role.RoleName;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRoleRequest {
    private RoleName roleName;

    @NotNull(message = "L'ID de l'activité ne peut pas être nul.")
    private Long activiteId; // Correction de la convention de nommage

    public RoleName getRoleName() {
        return roleName;
    }

    public Long getActiviteId() {
        return activiteId;
    }

    public void setActiviteId(Long id) {
        this.activiteId = id;  // Correction ici
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }
}
