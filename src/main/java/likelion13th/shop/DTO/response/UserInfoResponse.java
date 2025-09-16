package likelion13th.shop.DTO.response;

import likelion13th.shop.domain.Order;
import likelion13th.shop.domain.User;
import likelion13th.shop.global.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class UserInfoResponse {
    private String usernickname;
    private int recentTotal;
    private int maxMileage;
    private Map<OrderStatus, Integer> orderStatusCounts;

    public static UserInfoResponse from(User user) {
        Map<OrderStatus, Integer> orderStatusCounts = user.getOrders().stream()
                .collect(Collectors.groupingBy(
                        Order::getStatus,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));

        orderStatusCounts.putIfAbsent(OrderStatus.PROCESSING, 0);
        orderStatusCounts.putIfAbsent(OrderStatus.COMPLETE, 0);
        orderStatusCounts.putIfAbsent(OrderStatus.CANCEL, 0);

        return new UserInfoResponse(
                user.getUsernickname(),
                user.getRecentTotal(),
                user.getMaxMileage(),
                orderStatusCounts
        );
    }
}