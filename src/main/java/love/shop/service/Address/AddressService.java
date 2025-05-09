package love.shop.service.Address;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.address.Address;
import love.shop.repository.address.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public List<Address> findAddressesByMemberId(Long memberId) {
        return addressRepository.findAddressesByMemberId(memberId);
    }

}
