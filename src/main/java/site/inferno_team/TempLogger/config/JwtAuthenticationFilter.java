package site.inferno_team.TempLogger.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String prefix = "Bearer ";
        String token = request.getHeader("Authorization");

        final String jwt;
        final String userEmail;
        if (token == null || !token.startsWith(prefix)) {
            boolean hasTokenInCookie = false;
            Cookie[] cookies = request.getCookies();
            if (cookies != null)
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("token")) {
                        token = cookie.getValue();
                        hasTokenInCookie = true;

                    }
                }
            if (!hasTokenInCookie) {
                filterChain.doFilter(request, response);
                return;
            }
            jwt = token;
        } else {
            jwt = token.substring(prefix.length());
        }
        try {
            if (jwtService.isTokenExpired(jwt)) {
                filterChain.doFilter(request, response);
                return;
            }
        } catch (ExpiredJwtException e) {
            response.resetBuffer();
            response.setStatus(403);
            response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("reason", e.getMessage());
            response.getOutputStream().print(new ObjectMapper().writeValueAsString(responseMap));
            response.flushBuffer(); // marks response as committed -- if we don't do this the request will go
            // through normally!
            return;
        }
        userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext()
                .getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));
                request.setAttribute("user", userDetails);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
