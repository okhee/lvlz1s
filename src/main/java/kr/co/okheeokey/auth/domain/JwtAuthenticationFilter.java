package kr.co.okheeokey.auth.domain;

import io.jsonwebtoken.JwtException;
import kr.co.okheeokey.auth.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("JwtAuthFilter : doFilter() called");

        resolveToken((HttpServletRequest) request)
                .map(jwtAuthTokenProvider::getSubject)
                .map(userDetailsService::loadUserByUsername)
                .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities()))
                .ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));

        chain.doFilter(request, response);
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
