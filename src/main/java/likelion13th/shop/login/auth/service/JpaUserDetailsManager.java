package likelion13th.shop.login.auth.service;

import likelion13th.shop.domain.User;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JpaUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public JpaUserDetailsManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String providerId) throws UsernameNotFoundException {
        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> {
                    log.warn("사용자 정보 없음!");
                    throw new GeneralException(ErrorCode.USER_NOT_FOUND);
                });
        return CustomUserDetails.fromEntity(user);
    }

    @Override
    public void createUser(UserDetails user) {
        if(userExists(user.getUsername())) {
            throw new GeneralException(ErrorCode.ALREADY_USED_NICKNAME);
        }

        try {
            User newUser = ((CustomUserDetails) user).toEntity();
            userRepository.save(newUser);
            log.info("사용자 생성 완료");
        } catch (ClassCastException e) {
            log.error("UserDetails -> CustomUserDetails로 변환 실패!");
            throw new GeneralException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean userExists(String providerId) {
        return userRepository.existsByProviderId(providerId);
    }


    @Override
    public void updateUser(UserDetails user) {
        log.error("사용자 정보 업데이트는 지원되지 않음 (provider_id): {}", user.getUsername());
        throw new UnsupportedOperationException("사용자 업데이트 기능은 아직 지원되지 않습니다.");
    }

    @Override
    public void deleteUser(String providerId) {
        log.error("사용자 삭제는 지원되지 않음 (provider_id): {}", providerId);
        throw new UnsupportedOperationException("사용자 삭제 기능은 아직 지원되지 않습니다.");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        log.error("비밀번호 변경은 지원되지 않음.");
        throw new UnsupportedOperationException("비밀번호 변경 기능은 아직 지원되지 않습니다.");
    }
}

/*
1)
-소셜 로그인 시 사용자의 정보를 데이터베이스에 자동으로 등록하게 함
-데이터베이스에 있는 사용자 정보를 어떻게 조회, 생성할지 관리함
-로그인 시, 데이터베이스에서 사용자 정보를 가져와서 userdetails 형태로 바꿈

2)
-소셜 로그인을 하면 데이터베이스에 등록하지 못해 로그인에 실패함
-사용자를 찾지 못해 인증에 실패하여 로그인이 불가능함
-이미 존재하는 사용자인지 알 수 없음 -> 사용자를 중복 생성하려 함
 */
