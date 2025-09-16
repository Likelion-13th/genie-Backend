package likelion13th.shop.service;

import likelion13th.shop.domain.User;
import likelion13th.shop.domain.Address;
import likelion13th.shop.DTO.request.AddressRequest;
import likelion13th.shop.DTO.response.AddressResponse;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.exception.CustomException;
import likelion13th.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAddressService {

    private final UserRepository userRepository;

    @Transactional
    public AddressResponse saveAddress(String providerId, AddressRequest request) {
        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String zipcode = request.getZipcode();
        String address = request.getAddress();
        String detail = request.getAddressDetail();

        Address newAddress = new Address(zipcode, address, detail);
        user.updateAddress(newAddress);
        userRepository.save(user);

        return new AddressResponse(user.getAddress());
    }

    @Transactional(readOnly = true)
    public AddressResponse getAddress(String providerId) {
        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return new AddressResponse(user.getAddress());
    }
}
