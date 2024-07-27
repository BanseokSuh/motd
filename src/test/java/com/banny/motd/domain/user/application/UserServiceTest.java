package com.banny.motd.domain.user.application;


import com.banny.motd.domain.user.application.repository.UserRepository;
import com.banny.motd.domain.user.fixture.UserEntityFixture;
import com.banny.motd.domain.user.infrastructure.entity.UserEntity;
import com.banny.motd.global.exception.ApplicationException;
import com.banny.motd.global.exception.ResultType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    @DisplayName("회원가입")
    void test1() {
        // given
        String loginId = "loginId";
        String userName = "userName";
        String password = "password";
        String email = "test@gmail.com";
        String gender = "M";
        String role = "USER";

        // when
        when(userRepository.findByLoginId(loginId)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userRepository.save(any())).thenReturn(UserEntityFixture.get(loginId, userName, password, gender, role));

        // then
        assertDoesNotThrow(() -> userService.join(loginId, userName, password, email, gender));
    }

    @Test
    @DisplayName("회원가입시_loginId로_회원가입한_유저가_이미_있다")
    void test2() {
        // given
        String loginId = "loginId123";
        String userName = "userName123";
        String password = "password123";
        String email = "test@gmail.com";
        String gender = "M";
        String role = "USER";

        UserEntity fixture = UserEntityFixture.get(loginId, userName, password, gender, role);

        // when
        when(userRepository.findByLoginId(loginId)).thenReturn(Optional.of(fixture));
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userRepository.save(any())).thenReturn(UserEntityFixture.get(loginId, userName, password, gender, role));

        // then
        ApplicationException e = assertThrows(ApplicationException.class, () -> userService.join(loginId, userName, email, password, gender));
        assertEquals(ResultType.USER_DUPLICATED.getCode(), e.getResult().getCode());
    }

    @Test
    @DisplayName("로그인")
    void test3() {
        // given
        String loginId = "loginId";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(loginId, "userName", password, "M", "USER");

        // when
        when(userRepository.findByLoginId(loginId)).thenReturn(Optional.of(fixture));
        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);

        // then
        assertDoesNotThrow(() -> userService.login(loginId, password));
    }
}