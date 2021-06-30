package kr.co.okheeokey.user.service;

import kr.co.okheeokey.user.domain.User;
import kr.co.okheeokey.user.domain.UserRepository;
import kr.co.okheeokey.user.domain.UserRole;
import kr.co.okheeokey.user.domain.UserRoles;
import kr.co.okheeokey.user.exception.UsernameOccupiedException;
import kr.co.okheeokey.user.exception.WrongPasswordException;
import kr.co.okheeokey.user.vo.UserCreateValues;
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

    public User getUserIfRegistered(String name, String password) throws UsernameNotFoundException, WrongPasswordException {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("No such user name { " + name + " } exists"));

        return Optional.of(user)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElseThrow(() -> new WrongPasswordException("Invalid password input for user { " + user.getName() + " }"));
    }

    // values is considered validated.
    public User createUser(UserCreateValues values) throws UsernameOccupiedException {
        // Check if user name is occupied
        userRepository.findByNameAndEnabled(values.getName(), true)
            .ifPresent(user -> {
                throw new UsernameOccupiedException("User name { " + values.getName() + " } is occupied.");
            });

        return userRepository.save(new User(values.getName(),
                passwordEncoder.encode(values.getPassword()),
                new UserRoles(UserRole.USER)));
    }
}
