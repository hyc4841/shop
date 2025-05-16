package love.shop.web.address.dto;

import lombok.Data;
import love.shop.domain.address.Address;

import java.util.ArrayList;
import java.util.List;

@Data
public class AddressDto {

    private Long addressId;
    private String city;
    private String street;
    private String zipcode;
    private String detailedAddress;
    private String addressName;

    public AddressDto(Address address) {
        this.addressId = address.getId();
        this.city = address.getCity();
        this.street = address.getStreet();
        this.zipcode = address.getZipcode();
        this.detailedAddress = address.getDetailedAddress();
        this.addressName = address.getAddressName();
    }

    public static List<AddressDto> makeAddressDtoList(List<Address> addressList) {
        List<AddressDto> addressDtoList = new ArrayList<>();

        for (Address address : addressList) {
            if (address.getIsActivate()) {
                AddressDto addressDto = new AddressDto(address);
                addressDtoList.add(addressDto);
            }
        }

        return addressDtoList;
    }
}
