package com.bytescolab.featureflag.security.jwt;

import com.bytescolab.featureflag.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private long jwtExpirationMs;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw new ApiException(ErrorCodes.TOKEN_EXPIRADO, ErrorCodes.TOKEN_EXPIRADO_MSG);
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            throw new ApiException(ErrorCodes.TOKEN_MALFORMADO, ErrorCodes.TOKEN_MALFORMADO_MSG);
        } catch (JwtException | IllegalArgumentException e) {
            throw new ApiException(ErrorCodes.TOKEN_INVALIDO, ErrorCodes.TOKEN_INVALIDO_MSG);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

    }

    public long extractExpirationMillis(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .getTime();
        } catch (ExpiredJwtException e) {
            throw new ApiException(ErrorCodes.TOKEN_EXPIRADO, ErrorCodes.TOKEN_EXPIRADO_MSG);
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            throw new ApiException(ErrorCodes.TOKEN_MALFORMADO, ErrorCodes.TOKEN_MALFORMADO_MSG);
        } catch (JwtException | IllegalArgumentException e) {
            throw new ApiException(ErrorCodes.TOKEN_INVALIDO, ErrorCodes.TOKEN_INVALIDO_MSG);
        }
    }
}