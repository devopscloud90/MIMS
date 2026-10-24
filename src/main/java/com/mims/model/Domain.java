package com.mims.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Domain(
        Long id,
        @NotBlank @Size(max = 200) String name,
        @Size(max = 4000) String description,
        @Min(0) Long advisorsCount,
        @Min(0) Long expertsCount) {
}
