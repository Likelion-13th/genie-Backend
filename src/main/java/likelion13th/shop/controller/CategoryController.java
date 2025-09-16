package likelion13th.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13th.shop.DTO.response.ItemResponse;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Tag(name = "카테고리", description = "카테고리 관련 API 입니다.")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{categoryId}/items")
    @Operation(summary = "카테고리별 상품 조회", description = "상품을 카테고리 별로 조회합니다.")
    public ApiResponse<?> getItemsByCategory(@PathVariable Long categoryId) {
        List<ItemResponse> items = categoryService.getItemsByCategory(categoryId);

        if (items.isEmpty()) {
            return ApiResponse.onSuccess(SuccessCode.CATEGORY_ITEMS_EMPTY, Collections.emptyList());
        }

        return ApiResponse.onSuccess(SuccessCode.CATEGORY_ITEMS_GET_SUCCESS, items);

    }
}

/*
1) 왜 필요한가
-사용자가 상품을 조회할 수 있게 http api 이용하기 위해 필요
-controller와 service 분리 해줌

2)
-없으면 상품을 조회하지 못 함
-카테고려 별 상품 기능 구현하지 못 함
-명확한 성공 처리를 할 수 없음
*/