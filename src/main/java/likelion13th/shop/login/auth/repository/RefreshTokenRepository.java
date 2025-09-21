package likelion13th.shop.login.auth.repository;

import likelion13th.shop.domain.User;
import likelion13th.shop.login.auth.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUser(User user);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.user = :user")
    void deleteByUser(@Param("user") User user);
}


/*
1)
-리프레시토큰 엔티티를 데이터베이스에 저장, 조회, 삭제하는 작업을 유연하게 함
-deletebyuser를 사용하여 사용자를 로그아웃 할 때 데이터베이스에서 토큰을 삭제하게 함

2)
-리프레시 토큰을 데이터베이스에 저장, 조회를 하지 못함
-로그아웃을 할 때, 데이터베이스에서 리프레시토큰을 삭제하지 못함 -> 보안 위험성 증가
-직접 모든 db 로직을 구현해야함 -> 코드 복잡성, 오류 증가
 */
