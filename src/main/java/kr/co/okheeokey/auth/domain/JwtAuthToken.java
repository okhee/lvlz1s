package kr.co.okheeokey.auth.domain;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import kr.co.okheeokey.auth.exception.CustomJwtRuntimeException;
import kr.co.okheeokey.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;

@Slf4j
public class JwtAuthToken {
    @Value("testKey")
    private Key key;
    @Value("10000000")
    private Long expirationTimeInMS;

    private final String token;

    public JwtAuthToken(String token) {
        this.token = token;
    }

    public JwtAuthToken(User user) {
        this.token = createJwtAuthToken(user);
    }

    public String createJwtAuthToken(User user) {
        Date now = new Date();

        return Jwts.builder()
                .setIssuedAt(now)
                .setIssuer("okhee/lvlz1s")
                .setSubject(user.getUsername())
                .setExpiration(new Date(now.getTime() + expirationTimeInMS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validate() {
        return getSubject() != null;
    }

    public String getSubject() {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            throw new CustomJwtRuntimeException();
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            throw new CustomJwtRuntimeException();
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            throw new CustomJwtRuntimeException();
        } catch (SignatureException e) {
            log.info("Validation of JWT token fails");
            throw new CustomJwtRuntimeException();
        } catch (IllegalArgumentException e) {
            log.info("Null of empty JWT token.");
            throw new CustomJwtRuntimeException();
        }
    }



}
