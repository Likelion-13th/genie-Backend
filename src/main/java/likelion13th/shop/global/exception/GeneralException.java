package likelion13th.shop.global.exception;

import likelion13th.shop.global.api.BaseCode;
import likelion13th.shop.global.api.ReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private final BaseCode code;

    public static GeneralException of(BaseCode code) {
        return new GeneralException(code);
    }

    public ReasonDto getReason() {
        return this.code.getReason();
    }
}
