package kr.co.okheeokey.auth.domain;

import io.jsonwebtoken.JwtException;
import kr.co.okheeokey.auth.exception.JwtRuntimeException;
import kr.co.okheeokey.auth.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, JwtRuntimeException {
        log.info("JwtAuthFilter : doFilterInternal() called");

        // Check whether user has JwtToken that can prove his/her identity
        // This is the only way to set Authentication on SecurityContext
        resolveToken(request)

            // Check validity of token and return subject
            .map(jwtAuthTokenProvider::getSubject)

            // User is now authenticated, setting Authentication object
            .map(userDetailsService::loadUserByUsername)
            .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities()))
            .ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));

        filterChain.doFilter(request, response);
    }

    private Optional<String> resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if(!StringUtils.hasText(token) || token.equals("Bearer"))
            return Optional.empty();

        String[] typeAndCredential = token.split(" ");
        if (typeAndCredential.length != 2)
            throw new IllegalArgumentException("Invalid authorization token");
        if (!typeAndCredential[0].equals("Bearer"))
            throw new JwtException("Invalid Authorization header type");

        return Optional.of(typeAndCredential[1]);
    }
}
