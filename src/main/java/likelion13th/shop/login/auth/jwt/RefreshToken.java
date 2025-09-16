package likelion13th.shop.login.auth.jwt;

import jakarta.persistence.*;
import likelion13th.shop.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", unique = true)
    private User user;

    private String refreshToken;

    private Long ttl;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateTtl(Long ttl) {
        this.ttl = ttl;
    }
}

/*
1)
-사용자의 로그인을 계속 유지하기 위해 필요함 -> 엑세스 토큰이 만료되면 리프레시 토큰을 사용해 새로운 엑세스 토큰을 발금함
-onetoone으로 하나의 리프레시토큰을 가질 수 있게 하여 보안 강화함
-리프레시 토큰을 데이터베이스에 저장하여 토큰을 추적함

2)
-짧은 엑세스 토큰 만료 시간으로 계속 로그인 해야함
-데이터베이스에서 리프레시 토큰을 관리하지 않으면, 토큰 유출 시 무효화할 방법이 없음 -> 보안성 낮음
-onetoone이 없다면, 여러 개의 리프레시 토큰을 갖게됨 -> 오류 발생
 */
