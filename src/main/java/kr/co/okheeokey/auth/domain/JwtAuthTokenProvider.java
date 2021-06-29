package kr.co.okheeokey.auth.domain;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.co.okheeokey.auth.exception.JwtRuntimeException;
import kr.co.okheeokey.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtAuthTokenProvider {
    private final Long expirationTimeInMS;
    private final Key key;

    public JwtAuthTokenProvider(@Value("${security.jwt.token.base64-secret}") String base64Secret,
                                @Value("${security.jwt.token.expire-length}") Long expirationTimeInMS) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
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

    // Validate if given token is signed properly to ensure security and prohibit unauthorized access.
    public String getSubject(String token) throws JwtRuntimeException{
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtRuntimeException(e.getMessage());
        }
    }
}
