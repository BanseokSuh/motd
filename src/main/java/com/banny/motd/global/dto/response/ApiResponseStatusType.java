package com.banny.motd.global.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiResponseStatusType {

    SUCCESS("0000", "success"),

    FAIL_INVALID_TOKEN("1001", "invalid token"),

    // User
    FAIL_USER_NOT_FOUND("2001", "user not found"),
    FAIL_USER_PASSWORD_MISMATCH("2002", "user password mismatch"),
    FAIL_USER_DUPLICATED("2003", "user duplicated"),
    FAIL_USER_NOT_ACTIVE("2004", "user not active"),

    // POST
    FAIL_POST_NOT_FOUND("3001", "post not found"),
    FAIL_INVALID_PERMISSION("3002", "invalid permission"),

    // ALARM
    FAIL_ALARM_CONNECT_ERROR("5001", "alarm connect error"),

    FAIL_NOT_EXIST_DEVICE("6000", "not exist device type"),

    FAIL_ALREADY_LOGGED_IN("6001", "already logged in"),

    // INVALID PARAMETER WHEN REQUEST
    FAIL_INVALID_PARAMETER("9997", "invalid parameter"),
    FAIL_VALIDATION_ERROR("9998", "validate error"),
    FAIL_SERVER_ERROR("9999", "server error");


    private final String code;
    private final String desc;

}
