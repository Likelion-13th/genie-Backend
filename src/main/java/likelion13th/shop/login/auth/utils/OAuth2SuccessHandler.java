package likelion13th.shop.login.auth.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion13th.shop.domain.Address;
import likelion13th.shop.domain.User;
import likelion13th.shop.login.auth.dto.JwtDto;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.login.auth.service.JpaUserDetailsManager;
import likelion13th.shop.login.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JpaUserDetailsManager jpaUserDetailsManager;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        String providerId = (String) oAuth2User.getAttribute("provider_id");
        String nickname   = (String) oAuth2User.getAttribute("nickname");

        String maskedPid  = (providerId != null && providerId.length() > 4) ? providerId.substring(0, 4) + "***" : "***";
        String maskedNick = (nickname != null && !nickname.isBlank()) ? "*(hidden)*" : "(none)";
        log.info("OAuth2 Success - providerId(masked)={}, nickname={}", maskedPid, maskedNick);

        if (!jpaUserDetailsManager.userExists(providerId)) {
            User newUser = User.builder()
                    .providerId(providerId)
                    .usernickname(nickname)
                    .deletable(true)
                    .build();

            newUser.setAddress(new Address("10540", "경기도 고양시 덕양구 항공대학로 76", "한국항공대학교"));

            CustomUserDetails userDetails = new CustomUserDetails(newUser);
            jpaUserDetailsManager.createUser(userDetails);
            log.info("신규 회원 등록 완료 - providerId(masked)={}", maskedPid);
        } else {
            log.info("기존 회원 로그인 - providerId(masked)={}", maskedPid);
        }

        JwtDto jwt = userService.jwtMakeSave(providerId);
        log.info("JWT 발급 완료 - providerId(masked)={}", maskedPid);

        String frontendRedirectUri = request.getParameter("redirect_uri");
        List<String> authorizedUris = List.of(
                "https://genie-likelion.netlify.app/",
                "http://localhost:3000",
                "http://localhost:8080"
        );
        if (frontendRedirectUri == null || !authorizedUris.contains(frontendRedirectUri)) {
            frontendRedirectUri = "https://genie-likelion.netlify.app/";
        }

        String redirectUrl = UriComponentsBuilder
                .fromUriString(frontendRedirectUri)
                .queryParam("accessToken", jwt.getAccessToken())
                .build()
                .toUriString();

        log.info("Redirecting to authorized frontend host: {}", frontendRedirectUri);

        response.sendRedirect(redirectUrl);
    }
}

/*
1)
-소셜 로그인 확인 후, 데이터베이스에 회원을 자동 회원가입하도록 함
-소셜 로그인 인증 후, jwt를 발급함

2)
-신규 사용자는 자동으로 회원가입하지 못함
-소셜 로그인 인증 후에도 로그인이 완료되지 않음
-jwt를 발급하지 못함 -> 토큰을 발급하지 못함 -> 로그인 후에 장바구니 담기, 프로필 조회 등 기능들을 하지 못함
 */
