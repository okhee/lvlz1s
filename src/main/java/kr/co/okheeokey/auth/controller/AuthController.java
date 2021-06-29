package kr.co.okheeokey.auth.controller;

import kr.co.okheeokey.auth.dto.LoginDto;
import kr.co.okheeokey.auth.service.AuthService;
import kr.co.okheeokey.user.exception.WrongPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/login")
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDto dto) throws UsernameNotFoundException, WrongPasswordException {
        String jwtAuthToken = authService.createJwtAuthToken(dto.getName(), dto.getPassword());

        return ResponseEntity.ok(jwtAuthToken);
    }
}
