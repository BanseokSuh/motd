package com.banny.motd.domain.user.api.dto.response;

import com.banny.motd.domain.user.domain.Gender;
import com.banny.motd.domain.user.domain.User;
import com.banny.motd.domain.user.domain.UserRole;
import com.banny.motd.domain.user.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserMyResponse {

    private Long id;
    private String loginId;
    private String userName;
    private String email;
    private Gender gender;
    private UserRole userRole;
    private UserStatus userStatus;

    public static UserMyResponse from(User user) {
        return new UserMyResponse(
                user.getId(),
                user.getLoginId(),
                user.getUsername(),
                user.getEmail(),
                user.getGender(),
                user.getUserRole(),
                user.getUserStatus()
        );
    }

}
