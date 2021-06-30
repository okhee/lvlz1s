package kr.co.okheeokey.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class UserCreateDto {
    private static final int NAME_MIN_SIZE = 5;
    private static final int NAME_MAX_SIZE = 20;
    private static final String NAME_REGEXP = "^(?=.*[a-z0-9-_]).{" + NAME_MIN_SIZE + "," + NAME_MAX_SIZE + "}$";

    private static final String PASSWORD_SPECIAL_CHARS = "@#$%^`<>&+=\"!ºª·#~%&'¿¡€,:;*/+-.=_\\[]\\(\\)\\|_\\?\\\\";
    private static final int PASSWORD_MIN_SIZE = 8;
    private static final int PASSWORD_MAX_SIZE = 20;
    private static final String PASSWORD_REGEXP = "^(?=.*[0-9a-zA-Z" + PASSWORD_SPECIAL_CHARS + "])(?=\\S+$).{"+
            PASSWORD_MIN_SIZE+"," + PASSWORD_MAX_SIZE + "}$";

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
