package kr.co.okheeokey.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class UserCreateDto {
    private static final String NAME_REGEXP = "^(?!.*\\.\\.)(?!.*\\.$)[^\\W][\\w.]{5,20}$";
    private static final String PASSWORD_REGEXP = "^(?=.*\\d)(?=.*[a-zA-Z]).{8,20}$";

    @Pattern(regexp = NAME_REGEXP)
    private final String name;

    @Pattern(regexp = PASSWORD_REGEXP)
    private final String password;

    @JsonCreator
    public UserCreateDto(@JsonProperty("name") String name,
                         @JsonProperty("password") String password) {
        this.name = name;
        this.password = password;
    }
}
