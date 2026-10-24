package com.mims.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record Review(
        Long id,
        @NotNull Long inventionId,
        @Size(max = 20) String reviewDate,
        Long reviewerId,
        @Size(max = 4000) String comments) {
}
