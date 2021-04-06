package kr.co.okheeokey.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserRegisterDto {
    private String loginUserName;

    @Builder
    public UserRegisterDto(String loginUserName) {
        this.loginUserName = loginUserName;
    }
}
