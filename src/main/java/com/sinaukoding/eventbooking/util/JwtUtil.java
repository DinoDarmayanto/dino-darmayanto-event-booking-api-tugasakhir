package com.sinaukoding.eventbooking.util;

import com.sinaukoding.eventbooking.entity.managementuser.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(Map<String, Object> claims, User user) {
        long expiredOneHour = 1000 * 60 * 60;

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail()) // atau user.getUsername()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiredOneHour))
                .signWith(key)
                .compact();
    }


    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
