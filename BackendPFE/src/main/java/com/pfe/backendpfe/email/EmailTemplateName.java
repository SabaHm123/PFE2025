package com.pfe.backendpfe.email;

import lombok.Getter;

@Getter

public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("activate_account"),
    RESET_PASSWORD("reset_password"),
    CONFIRM_EMAIL("confirmation_account");

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
