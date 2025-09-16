package likelion13th.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "category")
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(name = "category_name", nullable = false)
    private String name;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();
}

/*
1)
-상품을 분류하기 위해 필요함 (ex.향수, 디퓨저)
-상품과 카테고리의 연결을 데이터베이스에 넣을 수 있음

2)
-없다면, 상품이 분류 없이 나열됨 -> 복잡성이 큼
-@ManyToMany 어노테이션 설정이 잘못 된다면, 데이터베이스에 생성되지 않아서 오류가 발생함
 */