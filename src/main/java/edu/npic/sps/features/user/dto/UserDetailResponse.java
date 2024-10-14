package edu.npic.sps.features.user.dto;

import edu.npic.sps.domain.Role;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record UserDetailResponse(

        String uuid,
        String fullName,
        String email,
        String phoneNumber,
        LocalDateTime createdAt,
        Boolean isVerified,
        List<String> roleNames

) {
}
