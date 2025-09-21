package likelion13th.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import likelion13th.shop.domain.entity.BaseEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "item")

public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String imagepath;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private boolean isNew=false;

    private String description;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
}

/*
1)
-상품 정보(이름, 가격, 브랜드, 이미지 등)를 데이터베이스에 저장하고 관리하기 위해 필요함
-BseEntity를 상속받아 상품 데이터에 자동으로 기록하여 이력을 관리하게 함
-상품이 어떤 상품에 속하는지 나타내고 카테고리별 상품 조회 같은 기능을 함

2)
-이 엔티티가 있어야 상품이 존재하게 함 -> 상품 등록, 조회하고 기본적인 기능을 구현하게 함
-@Entity, @Id같은 어노테이션이 없다면, 해당 테이블과 매핑할 수 없음
 */
