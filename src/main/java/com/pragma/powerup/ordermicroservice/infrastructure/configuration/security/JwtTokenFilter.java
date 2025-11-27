package com.pragma.powerup.ordermicroservice.infrastructure.configuration.security;

import com.pragma.powerup.ordermicroservice.infrastructure.configuration.security.details.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getToken(request);

        if (token != null && validateToken(token)) {
            String username = getUsernameFromToken(token);
            List<String> roles = getRolesFromToken(token);
            Long userId = getIdFromToken(token);
            Long restaurantId = getRestaurantIdFromToken(token);

            List<GrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());

            CustomUserDetails userDetails = new CustomUserDetails(userId, restaurantId, username);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    authorities
            );
            
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.replace("Bearer ", "");
        }
        return null;
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKeys()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getKeys()).build().parseClaimsJws(token).getBody().getSubject();
    }
    
    private Long getIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getKeys()).build().parseClaimsJws(token).getBody();
        Object id = claims.get("id");
        if (id instanceof Integer) {
            return ((Integer) id).longValue();
        } else if (id instanceof Long) {
            return (Long) id;
        }
        return null;
    }

    private Long getRestaurantIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getKeys()).build().parseClaimsJws(token).getBody();
        Object restaurantId = claims.get("restaurantId");
        if (restaurantId instanceof Integer) {
            return ((Integer) restaurantId).longValue();
        } else if (restaurantId instanceof Long) {
            return (Long) restaurantId;
        }
        return null;
    }

    private List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getKeys()).build().parseClaimsJws(token).getBody();
        return (List<String>) claims.get("roles");
    }

    private java.security.Key getKeys() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
