package likelion13th.shop.domain;

import jakarta.persistence.*;
import likelion13th.shop.domain.entity.BaseEntity;
import likelion13th.shop.login.auth.jwt.RefreshToken;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String providerId;

    @Column(nullable = false)
    private String usernickname;

    @Column(nullable = true)
    private String phoneNumber;

    @Column(nullable = false)
    private boolean deletable = true;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private int maxMileage = 0;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private int recentTotal = 0;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private RefreshToken auth;

    @Setter
    @Embedded
    private Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);
    }

    public void useMileage(int mileage) {
        if (mileage < 0) {
            throw new IllegalArgumentException("사용할 마일리지는 0보다 커야 합니다.");
        }
        if (this.maxMileage < mileage) {
            throw new IllegalArgumentException("마일리지가 부족합니다.");
        }
        this.maxMileage -= mileage;
    }

    public void addMileage(int mileage) {
        if (mileage < 0) {
            throw new IllegalArgumentException("적립할 마일리지는 0보다 커야 합니다.");
        }
        this.maxMileage += mileage;
    }

    public void updateRecentTotal(int amount) {
        int newTotal = this.recentTotal + amount;
        if (newTotal < 0) {
            throw new IllegalArgumentException("총 결제 금액은 음수가 될 수 없습니다.");
        }
        this.recentTotal = newTotal;
    }

    public void updateAddress(Address address) {
        this.address = address;
    }
}


/*
1)
-회원 정보(ID, 닉네임, 주소, 마일리지, 전화번호 등)을 통합하여 데이터베이스에 저장하고 과닐함
-회원가입, 로그인, 마이페이지 등 회원 관리 기능을 관리함
-refreshtoken과 onetoone 관계로 로그인 세션 관리하고 인증 상태 유지함
-providerId는 소셜 로그인의 식별자로 사용됨

2)
-엔티티가 없으면 회원 관련한 모든 것들의 서비스를 구현하지 못함
-리프레시토큰의 관계가 맞지 않으면 로그인을 유지할 수 없음
 */