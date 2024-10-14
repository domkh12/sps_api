package edu.npic.sps.features.user.dto;

import edu.npic.sps.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreateUser(
        @NotBlank(message = "fullName is required!")
        String fullName,
        @NotBlank(message = "email is required")
        @Email
        String email,
        @NotBlank(message = "password is required")
        String password,
        @NotBlank(message = "phoneNumber is required!")
        String phoneNumber,
        List<String> roleName
) {
}
