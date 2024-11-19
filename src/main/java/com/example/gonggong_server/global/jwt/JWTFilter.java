package com.example.gonggong_server.global.jwt;

import com.example.gonggong_server.auth.exception.AuthException;
import com.example.gonggong_server.global.status.ErrorStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try{
            String authorization = request.getHeader("Authorization");

            validateAuthorizationHeader(authorization);

            String token = authorization.split(" ")[1];

            checkIsTokenValid(token);

            String userInputId = jwtUtil.getUserInputId(token);
            Authentication authToken = new UsernamePasswordAuthenticationToken(
                    userInputId, // principal로 userInputId 사용
                    null,  // credentials는 필요 없으므로 null
                    null   // authorities는 비워둠 (필요한 경우 권한 추가)
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        } catch (AuthException e) {
            log.warn("JWT Filter Error", e);
            handleAuthException(response, e);
        } catch (Exception e) {
            log.error("JWT Filter Error", e);
            handleException(response, e);
        }
    }

    private static void handleAuthException(HttpServletResponse response, AuthException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = String.format("{\"code\": \"400\", \"message\": \"%s\"}", e.getMessage());
        response.getWriter().write(jsonResponse);
    }

    private static void handleException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = String.format("{\"code\": \"500\", \"message\": \"%s\"}", e.getMessage());
        response.getWriter().write(jsonResponse);
    }

    private void checkIsTokenValid(String token) {
        if (jwtUtil.isExpired(token)) {
            throw new AuthException(ErrorStatus.TOKEN_EXPIRED);
        }
    }

    private static void validateAuthorizationHeader(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AuthException(ErrorStatus.INVALID_AUTHORIZATION_HEADER);
        }
    }
}

