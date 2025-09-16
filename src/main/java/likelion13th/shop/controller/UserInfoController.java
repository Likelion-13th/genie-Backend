package likelion13th.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13th.shop.DTO.request.AddressRequest;
import likelion13th.shop.DTO.response.AddressResponse;
import likelion13th.shop.DTO.response.UserInfoResponse;
import likelion13th.shop.DTO.response.UserMileageResponse;
import likelion13th.shop.domain.User;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.login.service.UserService;
import likelion13th.shop.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 정보", description = "회원 정보 관련 API 입니다.")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserService userService;
    private final UserAddressService userAddressService;

    @GetMapping("/profile")
    @Operation(summary = "사용자 정보 조회", description = "로그인한 사용자의 정보와 주문 상태별 개수를 조회합니다.")
    public ApiResponse<?> getUserInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.getAuthenticatedUser(customUserDetails.getProviderId());

        UserInfoResponse userInfo = UserInfoResponse.from(user);

        return ApiResponse.onSuccess(SuccessCode.USER_INFO_GET_SUCCESS, userInfo);
    }

    @PostMapping("/address")
    @Operation(summary = "주소 저장", description = "로그인한 사용자의 주소를 저장합니다.")
    public ApiResponse<AddressResponse> saveAddress(
            @RequestBody AddressRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        AddressResponse addressResponse = userAddressService.saveAddress(customUserDetails.getProviderId(), request);
        return ApiResponse.onSuccess(SuccessCode.ADDRESS_SAVE_SUCCESS, addressResponse);
    }

    @GetMapping("/mileage")
    @Operation(summary = "사용 가능 마일리지 조회", description = "로그인한 사용자의 사용 가능 마일리지를 조회합니다.")
    public ApiResponse<UserMileageResponse> getAvailableMileage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.getAuthenticatedUser(customUserDetails.getProviderId());
        return ApiResponse.onSuccess(SuccessCode.USER_MILEAGE_SUCCESS, new UserMileageResponse(user.getMaxMileage()));
    }
}

/*
1)
-마이페이지의 계정 관리 기능을 제공해야하기 위해 필요함
-프로필 정보(회원 정보, 사용자 정보 조회, 주소 관리, 마일리지 조회)을 통합적으로 관리함 ->구조가 복잡하지 않음
-spring security와 연동하여 사용자 식별하고 데이터 안전하게 제공함

2)
-마이페이지에서 볼 수 있는 프로필 정보(회원정보, 사용자 정보 조회, 주소 관리, 마일리지 조회)를 확인하거나 수정할 수 없음
-사용자 인증 로직이 없으면, 프로필 정보를 조회하거나 수정할 수 없음
 */