package edu.npic.sps.features.user.dto;

import jakarta.validation.constraints.Email;

import java.util.List;

public record UpdateUserRequest(
        String fullName,
        @Email
        String email,
        String password,
        String phoneNumber,
        List<String> roleName
) {
}
