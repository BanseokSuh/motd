package com.banny.motd.api.controller.user.response;

import com.banny.motd.domain.user.Tokens;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {

    private String accessToken;
    private String refreshToken;

    public static UserLoginResponse from(Tokens token) {
        return new UserLoginResponse(token.getAccessToken(), token.getRefreshToken());
    }

}
