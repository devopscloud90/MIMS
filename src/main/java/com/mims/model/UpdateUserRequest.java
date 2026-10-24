package com.mims.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @NotBlank @Size(max = 200) String name,
        @Size(max = 30) String mobile,
        @Email @Size(max = 254) String email,
        @Size(max = 4000) String address,
        Long userTypeId) {
}
