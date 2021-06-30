package kr.co.okheeokey.user.controller;

import kr.co.okheeokey.user.domain.User;
import kr.co.okheeokey.user.dto.UserCreateDto;
import kr.co.okheeokey.user.exception.UsernameOccupiedException;
import kr.co.okheeokey.user.service.UserService;
import kr.co.okheeokey.user.vo.UserCreateValues;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok()
                .body(user);
    }

    @PostMapping
    public ResponseEntity<?> signUpNewUser(@RequestBody @Valid UserCreateDto dto) throws UsernameOccupiedException {
        User user = userService.createUser(new UserCreateValues(dto.getName(), dto.getPassword()));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(user);
    }
}
