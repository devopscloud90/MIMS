package com.mims.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InventionQuery(
        Long id,
        @NotNull Long inventionId,
        @Size(max = 4000) String query,
        Long replyId,
        @Size(max = 4000) String reply) {
}
