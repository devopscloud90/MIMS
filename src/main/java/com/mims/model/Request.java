package com.mims.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Request(
        Long id,
        @NotBlank @Size(max = 200) String name,
        @Size(max = 30) String phoneNumber,
        @Email @Size(max = 254) String email,
        @Size(max = 4000) String address,
        Long domainId,
        @Size(max = 20) String reviewDate,
        Long reviewerId,
        @Size(max = 4000) String comments,
        @Size(max = 30) String status,
        Long userId) {
}
