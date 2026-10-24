package com.mims.auth;

public record LegacyLoginAccount(
        Long id,
        String username,
        String password,
        String keys,
        String userType,
        String status) {
}
