package likelion13th.shop.domain;

import jakarta.persistence.*;
import likelion13th.shop.domain.entity.BaseEntity;
import likelion13th.shop.global.constant.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "orders") //예약어 회피
@NoArgsConstructor
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    @Setter
    private int totalPrice;

    @Column(nullable = false)
    @Setter
    private int finalPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Order(User user, Item item, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("주문 수량은 1개 이상이어야 합니다.");
        }

        this.user = user;
        this.item = item;
        this.quantity = quantity;
        this.status = OrderStatus.PROCESSING;
        this.totalPrice = item.getPrice() * quantity;
    }

    public static Order create(User user, Item item, int quantity, int totalPrice, int finalPrice) {
        Order order = new Order(user, item, quantity);
        order.totalPrice = totalPrice;
        order.finalPrice = finalPrice;
        return order;
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public void setUser(User user) {
        this.user = user;
        if (!user.getOrders().contains(this)) {
            user.getOrders().add(this);
        }
    }
}

/*
1)
-사용자가 어떤 상품을 주문했는지 관계를 명확히 정의하여 주문 내역을 데이터베이스에 저장하고 관리하기 필요함
-유효성 검증을 할 수 있고 주문 상태 변경 등을 할 수 있음

2)
-엔티티가 없으면 주문 데이터를 저장할 수 없음
-관계 설정이 잘못되면 데이터가 꼬여서 오류가 발생함 (ex.주문 오류 등)
 */
