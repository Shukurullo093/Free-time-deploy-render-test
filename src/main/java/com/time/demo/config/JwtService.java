package com.time.demo.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${spring.jwt.secret.key}")
    private String SECRET_KEY;

    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
        return decodeEmail(extractClaim(token, Claims::getSubject));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
//                .setSubject(userDetails.getUsername())
                .setSubject(encodeEmail(userDetails.getUsername()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
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

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //  email -> caesar(email, 1) -> base64(email)
    //  base64(email)x2
    public String encodeEmail(String email) {
        StringBuilder stringBuilder = new StringBuilder(email);
        int i = 0;
        while (i < stringBuilder.length()) {
            stringBuilder.setCharAt(i, (char) ((int) email.charAt(i) + 1));

            i++;
        }
        //        String code = UUID.randomUUID().toString().substring(0, 4);
//        return code+encodedString;
//        return String.join("", code, encodedString);
        return Base64.getEncoder().encodeToString(stringBuilder.toString().getBytes());
    }

    public String decodeEmail(String email) {
        byte[] decodedBytes = Base64.getDecoder().decode(email);
        String decodedString = new String(decodedBytes);
        StringBuilder stringBuilder = new StringBuilder(decodedString);
        int i = 0;
        while (i < stringBuilder.length()) {
            stringBuilder.setCharAt(i, (char) ((int) stringBuilder.charAt(i) - 1));
            i++;
        }
        return stringBuilder.toString();
    }
}