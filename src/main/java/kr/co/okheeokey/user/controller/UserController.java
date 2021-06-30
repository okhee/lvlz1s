package kr.co.okheeokey.user.controller;

import kr.co.okheeokey.user.domain.User;
import kr.co.okheeokey.user.dto.UserCreateDto;
import kr.co.okheeokey.user.exception.UsernameOccupiedException;
import kr.co.okheeokey.user.service.UserService;
import kr.co.okheeokey.user.vo.UserCreateValues;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> signUpNewUser(@RequestBody @Valid UserCreateDto dto) throws UsernameOccupiedException {
        User user = userService.createUser(new UserCreateValues(dto.getName(), dto.getPassword()));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(user);
    }
}
