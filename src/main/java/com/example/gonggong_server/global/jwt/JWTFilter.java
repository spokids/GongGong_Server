package com.example.gonggong_server.global.jwt;

import com.example.gonggong_server.auth.exception.AuthException;
import com.example.gonggong_server.global.status.ErrorStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AuthException(ErrorStatus.INVALID_AUTHORIZATION_HEADER);
        }

        String token = authorization.split(" ")[1];

        if (jwtUtil.isExpired(token)) {
            throw new AuthException(ErrorStatus.TOKEN_EXPIRED);
        }

        filterChain.doFilter(request, response);
    }
}

