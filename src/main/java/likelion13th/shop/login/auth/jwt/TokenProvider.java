package likelion13th.shop.login.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.login.auth.dto.JwtDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    private final Key secretKey;
    private final Long accessTokenExpiration;
    private final Long refreshTokenExpiration;

    public TokenProvider(
            @Value("${JWT_SECRET}") String secretKey,
            @Value("${JWT_EXPIRATION}") Long accessTokenExpiration,
            @Value("${JWT_REFRESH_EXPIRATION}") Long refreshTokenExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public JwtDto generateTokens(UserDetails userDetails) {
        log.info("JWT 생성 시작: 사용자{}", userDetails.getUsername());
        String userId = userDetails.getUsername();
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = createToken(userId, authorities, accessTokenExpiration);

        String refreshToken = createToken(userId, null, refreshTokenExpiration);

        log.info("JWT 사용완료: 사용자 {}", userDetails.getUsername());
        return new JwtDto(accessToken, refreshToken);

    }

    private String createToken(String providerId, String authorities, Long expirationTime) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(providerId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256);

        if (authorities != null) {
            builder.claim("authorities", authorities);
        }

        return builder.compact().toString();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            log.warn("토큰 만류");
            throw e;
        } catch (JwtException e) {
            log.warn("JWT 파싱 실패");
            throw new GeneralException(ErrorCode.TOKEN_INVALID);
        }

    }

    public Collection<? extends GrantedAuthority> getAuthFromClaims(Claims claims) {
        String authoritiesString = claims.get("authorities", String.class);
        if (authoritiesString != null && authoritiesString.isEmpty()) {
            log.warn("권한 정보가 없다 - 기본 ROLE_USER 부여");
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return Arrays.stream(authoritiesString.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Claims parseClaimsAllowExpired(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}

/*
1)
-secretkey를 사용하여 디지털 서명을 추가함 -> 토큰 검증 시 secretykey를 활용하여 보안성을 높임
-jwt의 생성, 서명, 검증, 정보 추출 등 모든 과정을 수행함 -> 복잡성 낮아짐
-

2)
-없다면, jwt를 생성하거나 검증핮 못함 -> 토큰 기반의 로그인이 작동하지 않음
-secretykey가 잘못된다면, 외부에서 토큰을 무단으로 사용할 수 있음
-만료된 엑세스 토큰을 리프레시 토큰과 비교할 수 없어서 재발급 할 수 없고 토큰을 검증하더라도 authorization 처리가 불가능함
 */
