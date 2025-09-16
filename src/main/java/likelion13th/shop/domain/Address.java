package likelion13th.shop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@Getter
public class Address {

    @Column(nullable = false)
    private String zipcode;

    @Column(nullable = false)
    private String address;

    @Column(name = "adress_detail", nullable = false)
    private String addressDetail;

    public Address() {
        this.zipcode = "10540";
        this.address = "경기도 고양시 덕양구 항공대학로 76";
        this.addressDetail = "한국항공대학교";
    }
}

/*
1)
-우편변호, 주소, 상세 주소 같은 필드를 하나로 묶어서 관리해야 코드 중복을 방지하고 수정이 편함

2)
-없다면, 모든 엔티티들에 필드를 각각 선언해야 하므로 복잡성이 큼
 */
