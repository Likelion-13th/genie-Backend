package likelion13th.shop.login.auth.dto;

import lombok.*;

@Builder
@ToString
@Getter
@Setter

public class JwtDto {
    private String accesstoken;
    private String refreshtoken;

    public JwtDto(String accesstoken, String refreshtoken) {
        this.accesstoken = accesstoken;
        this.refreshtoken = refreshtoken;
    }
}

/*
1)
-로그인 성공 시, accesstoken과 refreshtoken을 전달하는 dto를 수행할 수 있게 함

2)
-엑세스와 리프레시 토큰을 보내지 않을 시 시간이 지나면 다시 로그인 하는 불편함이 생김
-없다면, 토큰을 전달할 방법이 없음 -> api를 호출하는 권할을 얻지 못함
 */
