package com.mims.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Invention(
        Long id,
        Long domainId,
        @NotBlank @Size(max = 500) String title,
        @Size(max = 4000) String thesis,
        @Size(max = 4000) String finalDocument,
        @Size(max = 20) String registeredDate,
        @Size(max = 20) String patentDate,
        @Size(max = 30) String status,
        Long userId) {
}
