package com.example.backendstage.config;

import com.example.backendstage.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "YQkMvcLeWwxKVxN6PZzuevIE6JNvejLQmVeXME9pJ5kGX2o4hhdRZgt+LC5qfNkVHXmZsE8GUkY1y3zL/jTYN2HxfGYfVaMzzOMJBT/OSQ2rp3Xxx71qgNwLxr332pSGXwU4pwSn+n4Q9Vr6f4SpDvMXk10k/ZGALoZaAqgTFm1c9lOS/Fw5JkjLDlqOa0W8mxJiuy0y07EJgfnP4V/b70gSJCGELn7n8WcfC4hbU79IHMU/1qXBtXCkHRoOwew92stePaNs6PlTVb+DYRWSGjDhZiAdsKHPzTmyHeNYzeJ8S8HxoOg6a0Ues3YZX3Za96JaDRAbH68z3XWlSPogRorZvzDJqSuflwLgqNraLdM=";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User user, String role) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", role);
        return generateToken(extraClaims, user.getEmail());
    }

    public String generateToken(Map<String, Object> extraClaims, String username) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))  // 24 hours expiration time
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        System.out.println("Token to be parsed: " + token);  // Log the token
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String generateToken(User user) {
        return generateToken(user, user.getRole().toString());
    }

}
