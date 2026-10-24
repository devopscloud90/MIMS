package com.mims.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Opportunity(
        Long id,
        Long domainId,
        @NotBlank @Size(max = 500) String offer,
        @Size(max = 4000) String description,
        @Size(max = 30) String status) {
}
