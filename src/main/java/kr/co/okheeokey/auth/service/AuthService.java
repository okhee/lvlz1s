package kr.co.okheeokey.auth.service;

import kr.co.okheeokey.auth.domain.JwtAuthTokenProvider;
import kr.co.okheeokey.user.domain.User;
import kr.co.okheeokey.user.exception.WrongPasswordException;
import kr.co.okheeokey.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final UserService userService;

    // Create jwt token that can authenticate his/her identity using name and password and one's authority
    public String createJwtAuthToken(String name, String password) throws UsernameNotFoundException, WrongPasswordException {
        User user = userService.getUserIfRegistered(name, password);
        // check user's authorities

        return jwtAuthTokenProvider.createToken(user);
    }
}
