package likelion13th.shop.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddressRequest {
    private String zipcode;
    private String address;
    private String addressDetail;
}

