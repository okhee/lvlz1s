package kr.co.okheeokey.auth.domain;

import io.jsonwebtoken.JwtException;
import kr.co.okheeokey.auth.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final CustomUserDetailsService userDetailsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Optional<String> token = resolveToken((HttpServletRequest) request);
        if (token.isPresent()){
            JwtAuthToken jwtAuthToken = convertAuthToken(token.get());
            if(jwtAuthToken.validate()) {
                Optional.of(jwtAuthToken)
                        .map(JwtAuthToken::getSubject)
                        .map(userDetailsService::loadUserByUsername)
                        .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities()))
                        .ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));
            }
        }
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

    private JwtAuthToken convertAuthToken(String token) {
        return new JwtAuthToken(token);
    }
}
