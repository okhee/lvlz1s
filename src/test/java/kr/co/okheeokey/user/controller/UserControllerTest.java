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
    public void usernamePatternTest() throws Exception {
        String NAME_REGEXP = "^(?!.*\\.\\.)(?!.*\\.$)[^\\W][\\w.]{4,20}$";
        Predicate<String> matchPredicate = Pattern.compile(NAME_REGEXP).asPredicate();

        List<String> validUsernameList = Arrays.asList(
                "iloveryu._",
                "9.3.0521",
                "myungnee_",
                "babysoullvlz",
                "_flower_kei",
                "queen.chu_s",
                "happpy_yein",
                "jeezepizza",
                "abcdefgh1",
                "ABCDEFGH1",
                "candy"
        );
        List<String> invalidUsernameList = Arrays.asList(
                "c561",
                "fa3w51fa65w13f6a5w31f6a5wf1a65af3w",
                ""
        );

        System.out.println("Testing valid username list");
        validUsernameList.forEach(p -> System.out.println(matchPredicate.test(p) + ": " + p));

        System.out.println("Testing valid username list");
        invalidUsernameList.forEach(p -> System.out.println(matchPredicate.test(p) + ": " + p));

        assertTrue(validUsernameList.stream()
                .map(matchPredicate::test)
                .reduce(Boolean::logicalAnd).get());

        assertFalse(invalidUsernameList.stream()
                .map(matchPredicate::test)
                .reduce(Boolean::logicalOr).get());

    }

    @Test
    public void passwordPatternTest() throws Exception {
        String PASSWORD_REGEXP = "^(?=.*\\d)(?=.*[a-zA-Z]).{8,20}$";
        Predicate<String> matchPredicate = Pattern.compile(PASSWORD_REGEXP).asPredicate();

        List<String> validPasswordList = Arrays.asList(
                "password1",
                "sadf4w3a",
                "FJ38F)J*3ji",
                "Fjf038#FJ1390",
                "12345678 a",
                "abcdefgh1",
                "ABCDEFGH1",
                "@$^#&=+1!a"
        );
        List<String> invalidPasswordList = Arrays.asList(
                "12345678",
                "a1b2c3d",
                ""
        );

        System.out.println("Testing valid password list");
        validPasswordList.forEach(p -> System.out.println(matchPredicate.test(p) + ": " + p));

        System.out.println("Testing invalid password list");
        invalidPasswordList.forEach(p -> System.out.println(matchPredicate.test(p) + ": " + p));

        assertTrue(validPasswordList.stream()
                .map(matchPredicate::test)
                .reduce(Boolean::logicalAnd).get());

        assertFalse(invalidPasswordList.stream()
                .map(matchPredicate::test)
                .reduce(Boolean::logicalOr).get());
    }
}
