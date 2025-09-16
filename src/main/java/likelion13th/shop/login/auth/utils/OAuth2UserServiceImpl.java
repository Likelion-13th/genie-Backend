package likelion13th.shop.login.auth.utils;

import likelion13th.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor

public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String providerId = oAuth2User.getAttributes().get("id").toString();
        @SuppressWarnings("unhecked")
        Map<String, Object> properties =
                (Map<String, Object>) oAuth2User.getAttributes().getOrDefault("properties", Collections.emptyMap());
        String nickname = properties.getOrDefault("nickname", "카카오사용자").toString();

        Map<String, Object> extendedAttributes = new HashMap<>(oAuth2User.getAttributes());
        extendedAttributes.put("nickname", nickname);
        extendedAttributes.put("provider_id", providerId);

        return new DefaultOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                extendedAttributes, "provider_id"
        );
    }
}

/*
1)
-사용자를 고유 식별자를 지정하여 필요한 값이 반환하도록 함
-여러 소셜에서 필요한 각각 다른 데이터를 표준으로 통일시켜 일관된 방식으로 사용자를 처리함
-새 사용자는 role_user의 기본 권한을 받게 되고 초기 권한 설정 로직을 담당함

2)
-고유 식별자로 지정하지 않아 기본값이 다른 값으로 변질 될 수 있음 -> 데이터베이스와 일치하지 않아 사용하지 찾지 못하고 인증에 실패함
-없다면, 소셜에서 각각 다른 데어터 구조에 대응해야함 -> 복잡성 증가
-기본 권한이 없어서 새 사용자는 권한을 갖지 못함
 */
