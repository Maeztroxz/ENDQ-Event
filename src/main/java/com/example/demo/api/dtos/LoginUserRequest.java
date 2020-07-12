package com.example.demo.api.dtos;

import lombok.Data;

@Data
public class LoginUserRequest {
    private String email;
    private String password;
}
