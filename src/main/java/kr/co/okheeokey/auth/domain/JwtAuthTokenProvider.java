package kr.co.okheeokey.auth.domain;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import kr.co.okheeokey.auth.exception.CustomJwtRuntimeException;
import kr.co.okheeokey.user.domain.User;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

@Slf4j
public class JwtAuthTokenProvider {
    private final Long expirationTimeInMS;
    private final Key key;

    public JwtAuthTokenProvider(String base64Secret, Long expirationTimeInMS) {
        byte[] bytes = Decoders.BASE64.decode(base64Secret);
        this.key = Keys.hmacShaKeyFor(bytes);
        this.expirationTimeInMS = expirationTimeInMS;
    }

    public String createToken(User user) {
        Date now = new Date();

        return Jwts.builder()
                .setIssuedAt(now)
                .setIssuer("okhee/lvlz1s")
                .setSubject(user.getUsername())
                .setExpiration(new Date(now.getTime() + expirationTimeInMS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getSubject(String token) {
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
