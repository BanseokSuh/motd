package com.banny.motd.domain.user;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserRole {

    ADMIN("관리자"),
    USER("사용자");

    private final String text;

}
