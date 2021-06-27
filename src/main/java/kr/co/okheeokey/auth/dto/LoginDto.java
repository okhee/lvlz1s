package kr.co.okheeokey.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class LoginDto {
    private final String name;
    private final String password;

    @JsonCreator
    public LoginDto(@JsonProperty("name") String name,
                    @JsonProperty("password") String password) {
        this.name = name;
        this.password = password;
    }
}
