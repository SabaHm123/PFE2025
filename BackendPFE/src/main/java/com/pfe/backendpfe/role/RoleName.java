package com.pfe.backendpfe.role;

import com.pfe.backendpfe.user.Permission;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.pfe.backendpfe.user.Permission.*;

@RequiredArgsConstructor
public enum RoleName {
    ROLE_ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    RH_READ,
                    RH_UPDATE,
                    RH_DELETE,
                    RH_CREATE,
                    COLLAB_READ,
                    COLLAB_UPDATE,
                    COLLAB_DELETE,
                    COLLAB_CREATE
            )
    ),
    //ROLE_DIRECTEUR,
    ROLE_RH(Set.of(
            RH_READ,
            RH_UPDATE,
            RH_DELETE,
            RH_CREATE,
            COLLAB_READ,
            COLLAB_UPDATE,
            COLLAB_DELETE,
            COLLAB_CREATE
    )),
    ROLE_COLLAB(Set.of(
            COLLAB_READ,
            COLLAB_UPDATE,
            COLLAB_DELETE,
            COLLAB_CREATE
    )),

    //ROLE_QALITE,
    // ROLE_CHEF,ROLE_USER,
    ROLE_PENDING(Collections.emptySet());


    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
