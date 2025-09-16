package likelion13th.shop.service;

import likelion13th.shop.DTO.response.ItemResponse;
import likelion13th.shop.domain.Category;
import likelion13th.shop.domain.Item;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category findCategoryById(Long categoryId){
        return categoryRepository.findById(categoryId)
                .orElseThrow(()-> new GeneralException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public List<ItemResponse> getItemsByCategory(Long categoryId) {
        Category category = findCategoryById(categoryId);

        List<Item> items = category.getItems();
        return items.stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());
    }
}