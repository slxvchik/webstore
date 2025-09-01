package com.webstore.auth_service.jwt.blacklist;

import com.webstore.auth_service.jwt.JwtUtils;
import com.webstore.auth_service.jwt.ParsedToken;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {
    private final JwtBlacklistRepository blacklistRepository;
    private final JwtUtils jwtUtils;

    public boolean isTokenBlacklisted(String token) {
        Optional<ParsedToken> parsedToken = jwtUtils.extractAllClaims(token);

        if (parsedToken.isEmpty()) {
            return false;
        }

        ParsedToken claims = parsedToken.get();
        String jwtId = claims.getTokenId();
        String userId = claims.getUserId();

        return blacklistRepository.existsByJwtIdAndUserId(jwtId, userId);
    }

    public void addTokenToBlacklist(String token) {
        Optional<ParsedToken> parsedToken = jwtUtils.extractAllClaims(token);

        if (parsedToken.isEmpty() || isTokenBlacklisted(token)) {
            return;
        }

        ParsedToken claims = parsedToken.get();

        JwtBlacklist blacklistedToken = new JwtBlacklist();
        blacklistedToken.setJwtId(claims.getTokenId());
        blacklistedToken.setUserId(claims.getUserId());
        blacklistedToken.setExpiresAt(claims.getExpiration().toInstant());

        blacklistRepository.save(blacklistedToken);
    }

    public void addTokensToBlackList(String accessToken, String refreshToken) {
        addTokenToBlacklist(accessToken);
        addTokenToBlacklist(refreshToken);
    }

    @Scheduled(cron = "0 0 3 * * ?") // Очистка каждый день в 3:00
    public void cleanupExpiredTokens() {
        blacklistRepository.deleteAllExpiredSince(Instant.now());
    }
}
