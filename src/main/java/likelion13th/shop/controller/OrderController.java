package likelion13th.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13th.shop.DTO.request.OrderCreateRequest;
import likelion13th.shop.DTO.response.OrderResponse;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.login.service.UserService;
import likelion13th.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@Tag(name = "주문", description = "주문 관련 API 입니다.")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "주문 생성", description = "로그인한 사용자의 주문을 생성합니다.")
    public ApiResponse<?> createOrder(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody OrderCreateRequest request
    ) {
        OrderResponse newOrder = orderService.createOrder(request, customUserDetails);
        return ApiResponse.onSuccess(SuccessCode.ORDER_CREATE_SUCCESS, newOrder);
    }

    @GetMapping
    @Operation(summary = "모든 주문 조회", description = "로그인한 사용자의 모든 주문을 목록으로 조회합니다.")
    public ApiResponse<?> getAllOrders(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<OrderResponse> orders = orderService.getAllOrders(customUserDetails);
        if (orders.isEmpty()) {
            return ApiResponse.onSuccess(SuccessCode.ORDER_LIST_EMPTY, Collections.emptyList());
        }
        return ApiResponse.onSuccess(SuccessCode.ORDER_LIST_SUCCESS, orders);
    }

    @PutMapping("/{orderId}/cancel")
    @Operation(summary = "주문 취소", description = "로그인한 사용자의 주문을 취소합니다.")
    public ApiResponse<?> cancelOrder(@PathVariable Long orderId) {

        orderService.cancelOrder(orderId);

        return ApiResponse.onSuccess(SuccessCode.ORDER_CANCEL_SUCCESS, null);

    }
}

/*
1)
-주문 기능 (주문 생성, 모든 주문 조쇠, 주문 취소)을 사용할 수 있게 하기 위해 필요함
-주문을 관리할 수 있게 하고, http 요청을 받아 서비스 로직 호출, 응답 형식을 반환함

2)
-상품 주문, 주문 내역 조회, 주문 취소의 기능을 사용하지 못함
-http이 맞지 않으면 정확성이 떨어짐
-어노테이션 누락시, 요청 식별이 불가능함
 */