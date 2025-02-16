package com.example.microservice.model;

public enum Role {
    ROLE_ADMIN, ROLE_USER;

    public static Role fromString(String role) {
        switch (role.toUpperCase()) {
            case "ROLE_ADMIN":
                return ROLE_ADMIN;
            case "ROLE_USER":
                return ROLE_USER;
            default:
                throw new IllegalArgumentException("Unexpected value: " + role);
        }
    }
}
