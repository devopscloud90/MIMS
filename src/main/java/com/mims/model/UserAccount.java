package com.mims.model;

public record UserAccount(
        Long id,
        String name,
        String mobile,
        String email,
        String address,
        String username,
        String userType,
        Long userTypeId,
        String startDate,
        String endDate,
        String status) {
}
