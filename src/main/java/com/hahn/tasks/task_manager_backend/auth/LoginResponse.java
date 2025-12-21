package com.hahn.tasks.task_manager_backend.auth;

public class LoginResponse {

    private String token;
    private String email;
    private String fullName;

    public LoginResponse(String token, String email, String fullName) {
        this.token = token;
        this.email = email;
        this.fullName = fullName;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }
}
