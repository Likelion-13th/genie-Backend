package likelion13th.shop.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@NoArgsConstructor
public class UserRequestDto {

    @Schema(description = "UserReqDto")
    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserReqDto {
        private Long userId;
        private String providerId;
        private String usernickname;
    }
}
