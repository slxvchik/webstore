package com.webstore.auth_service.config.jwt;

import com.webstore.auth_service.security.CsrfTokenGenerator;
import com.webstore.auth_service.user.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        var accessToken = getAccessToken(request);
        var requiredCookieTokens = extractRequiredCookies(request);

        var refreshToken = requiredCookieTokens.get("refresh-token");
        var csrfToken = requiredCookieTokens.get("X-CSRF-TOKEN");

        // if refresh token or csrf token is empty -> unauth
        if (refreshToken == null || csrfToken == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The csrf token or refresh token is missing");
            return;
        }

        var refreshTokenClaims = jwtUtils.extractAllClaims(refreshToken);

        // refresh token is invalid -> unauth
        if (refreshTokenClaims.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The refresh token is invalid");
            return;
        }

        ParsedToken refreshTokenParsed = new ParsedToken(refreshTokenClaims.get());

        // csrf not equals csrf in refresh jwt -> unauth
        if (!csrfToken.equals(refreshTokenParsed.getCsrfToken())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The csrf token in refresh token is not equal to the csrf token");
            return;
        }

        // access token isset -> try validate token
        if (accessToken != null) {

            var accessTokenClaims = jwtUtils.extractAllClaims(accessToken);

            // if access token is valid -> authenticate
            if (accessTokenClaims.isPresent()) {

                ParsedToken accessTokenParsed = new ParsedToken(accessTokenClaims.get());

                User user = accessTokenParsed.getUser();

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                filterChain.doFilter(request, response);
                return;
            }
        }

        // else if access token is invalid, but refresh token and csrf tokens is valid
        UserDetails userDetails = userDetailsService.loadUserByUsername(
                refreshTokenParsed.getUsername()
        );

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        var newAccessToken = jwtUtils.createAccessToken((User) authenticationToken.getPrincipal());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // Add new access token to response headers
        response.addHeader("Authorization", "Bearer " + newAccessToken);

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(HttpServletRequest request) {
        var accessToken = request.getHeader("Authorization");
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            return accessToken.substring(7);
        }
        return null;
    }

    private Map<String, String> extractRequiredCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return Map.of();

        Map<String, String> cookies = new HashMap<>();
        for (Cookie cookie : request.getCookies()) {
            String name = cookie.getName();
            if ("refresh-token".equals(name) || "X-CSRF-TOKEN".equals(name)) {
                cookies.put(name, cookie.getValue());
            }
        }
        return cookies;
    }

}
