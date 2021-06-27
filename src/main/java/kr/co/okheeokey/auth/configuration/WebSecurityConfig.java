package kr.co.okheeokey.auth.configuration;

import kr.co.okheeokey.auth.domain.JwtAuthTokenProvider;
import kr.co.okheeokey.auth.domain.JwtAuthenticationFilter;
import kr.co.okheeokey.auth.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailsService userDetailsService;

    @Value("${security.jwt.token.base64-secret}")
    private String base64Secret;

    @Value("${security.jwt.token.expire-length}")
    private Long expirationTimeInMS;

    @Bean
    public JwtAuthTokenProvider jwtAuthTokenProvider() {
        return new JwtAuthTokenProvider(base64Secret, expirationTimeInMS);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(userDetailsService, jwtAuthTokenProvider()),
                        UsernamePasswordAuthenticationFilter.class);
    }
}
