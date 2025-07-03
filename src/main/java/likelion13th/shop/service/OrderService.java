package likelion13th.shop.service;

import jakarta.transaction.Transactional;
import likelion13th.shop.DTO.request.OrderCreateRequest;
import likelion13th.shop.DTO.response.OrderResponseDto;
import likelion13th.shop.domain.Item;
import likelion13th.shop.domain.Order;
import likelion13th.shop.domain.User;
import likelion13th.shop.global.api.Errorcode;
import likelion13th.shop.global.constant.OrderStatus;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.repository.ItemRepository;
import likelion13th.shop.repository.OrderRepository;
import likelion13th.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private int calculateFinalPrice(int totalPrice, int mileageToUse)

    int availableMileage = Math.min(mileageToUse, totalPrice);
    int finalPrice = totalPrice - availableMileage;
        return Math.max(finalPrice,0);
}


@Transactional
public OrderResponseDto createOrder(OrderCreateRequest request, Us)