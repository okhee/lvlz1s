package kr.co.okheeokey.user.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserCreateValues {
    private final String name;
    private final String password;
}
