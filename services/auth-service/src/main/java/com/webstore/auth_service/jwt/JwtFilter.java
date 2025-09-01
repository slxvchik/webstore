package com.webstore.auth_service.jwt;

import com.webstore.auth_service.jwt.blacklist.JwtBlacklistService;
import com.webstore.auth_service.user.User;
import com.webstore.auth_service.user.UserDetailService;
import com.webstore.auth_service.user.deleted.DeletedUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    private UserDetailService userDetailService;
    private JwtBlacklistService jwtBlacklistService;
    private final DeletedUserService deletedUserService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        var accessTokenStr = getAccessToken(request);
        var requiredCookie = extractRequiredCookies(request);

        var refreshTokenStr = requiredCookie.get("refresh-token");

        // if refresh token is missing -> unauth request
        if (refreshTokenStr == null) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtBlacklistService.isTokenBlacklisted(refreshTokenStr) || accessTokenStr != null && jwtBlacklistService.isTokenBlacklisted(accessTokenStr)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The access or refresh token in blacklist");
            return;
        }

        Optional<ParsedToken> parsedRefreshToken = jwtUtils.extractAllClaims(refreshTokenStr);

        // refresh token is invalid -> error
        if (parsedRefreshToken.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The refresh token is invalid");
            return;
        }

        ParsedToken refreshToken = parsedRefreshToken.get();

        // access token isset -> try validate token
        if (accessTokenStr != null) {

            Optional<ParsedToken> parsedAccessToken = jwtUtils.extractAllClaims(accessTokenStr);

            // if access token is valid -> authenticate
            if (parsedAccessToken.isPresent()) {

                ParsedToken accessToken = parsedAccessToken.get();

                User user = accessToken.getUser();

                if (deletedUserService.isUserDeleted(user.getId())) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User account deleted");
                    return;
                }

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

        if (deletedUserService.isUserDeleted(refreshToken.getUserId())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User account deleted");
            return;
        }

        // else if access token is invalid, but refresh token is valid
        UserDetails userDetails = userDetailService.loadUserById(
                refreshToken.getUserId()
        );

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        var newAccessToken = jwtUtils.createAccessToken((User) authenticationToken.getPrincipal());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // Add new access token to response headers
        response.setHeader("Authorization", "Bearer " + newAccessToken);

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
            if ("refresh-token".equals(name)) {
                cookies.put(name, cookie.getValue());
            }
        }
        return cookies;
    }

}