package com.pfe.backendpfe.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    RH_READ("management:read"),
    RH_UPDATE("management:update"),
    RH_CREATE("management:create"),
    RH_DELETE("management:delete"),
    COLLAB_READ("collab:read"),
    COLLAB_UPDATE("collab:update"),
    COLLAB_CREATE("collab:create"),
    COLLAB_DELETE("collab:delete")

    ;

    @Getter
    private final String permission;
}
