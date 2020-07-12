package com.example.demo.api.dtos;

import lombok.Data;

@Data
public class LoginUserResponse {
    private String email;
    private String firstName;
    private String lastName;
    private String token;

    public LoginUserResponse(String token) {
        this.token = token;
    }
}
