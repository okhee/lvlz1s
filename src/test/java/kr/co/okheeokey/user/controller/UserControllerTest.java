package kr.co.okheeokey.user.controller;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class UserControllerTest {

    @Test
    public void passwordPatternTest() throws Exception {
        String PASSWORD_SPECIAL_CHARS = "@#$%^`<>&+=\"!ºª·#~%&'¿¡€,:;*/+-.=_\\[\\]\\(\\)\\|\\_\\?\\\\";
        int PASSWORD_MIN_SIZE = 8;
        int PASSWORD_MAX_SIZE = 20;
        String PASSWORD_REGEXP = "^(?=.*[0-9a-zA-Z" + PASSWORD_SPECIAL_CHARS + "])(?=\\S+$).{"+PASSWORD_MIN_SIZE+"," + PASSWORD_MAX_SIZE + "}$";

        Predicate<String> matchPredicate = Pattern.compile(PASSWORD_REGEXP).asPredicate();

        List<String> validPasswordList = Arrays.asList(
                "sadf4w3a",
                "FJ38F)J*3ji",
                "Fjf038#FJ1390",
                "12345678",
                "abcdefgh",
                "ABCDEFGH",
                "@$^#&=+!"
        );
        List<String> inValidPasswordList = Arrays.asList(
                "1234567", // size: 7
                "safcw3csd ", // including whitespace
                ""
        );

        assertTrue(validPasswordList.stream()
                .map(matchPredicate::test)
                .reduce(Boolean::logicalAnd).get());

        assertFalse(inValidPasswordList.stream()
                .map(matchPredicate::test)
                .reduce(Boolean::logicalOr).get());
    }
}
