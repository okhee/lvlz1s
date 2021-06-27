package kr.co.okheeokey.auth.service;

import kr.co.okheeokey.auth.domain.JwtAuthTokenProvider;
import kr.co.okheeokey.user.domain.User;
import kr.co.okheeokey.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final UserService userService;

    public String createJwtAuthToken(String name, String password) {
        User user = userService.getUserIfRegistered(name, password);
        // check user's authorities

        return jwtAuthTokenProvider.createToken(user);
    }
}
