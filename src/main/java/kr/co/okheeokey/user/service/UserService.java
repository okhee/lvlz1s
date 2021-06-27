package kr.co.okheeokey.user.service;

import kr.co.okheeokey.user.domain.User;
import kr.co.okheeokey.user.domain.UserRepository;
import kr.co.okheeokey.user.exception.WrongPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserIfRegistered(String name, String password) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("No such user name { " + name + " } exists"));

        return Optional.of(user)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElseThrow(() -> new WrongPasswordException("Wrong password input"));
    }
}
