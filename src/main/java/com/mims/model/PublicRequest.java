package com.mims.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PublicRequest(
        @NotBlank @Size(max = 200) String name,
        @NotBlank @Size(min = 10, max = 30) String phoneNumber,
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Size(max = 4000) String address,
        @NotNull Long domainId) {
}
