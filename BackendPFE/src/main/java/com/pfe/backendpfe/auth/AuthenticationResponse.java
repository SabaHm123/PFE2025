package com.pfe.backendpfe.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthenticationResponse {
        private String token;
        private Integer userID;
        private List<String> roles;


    public AuthenticationResponse(String token, Integer id, List<String> roles) {
        this.token = token;
        this.userID = id;
        this.roles = roles;

    }

    public AuthenticationResponse(String token) {
        this.token = token;
    }
}


