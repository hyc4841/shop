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
        this.addressId = address == null ? null : address.getId();
        this.city = address == null ? null : address.getCity();
        this.street = address == null ? null : address.getStreet();
        this.zipcode = address == null ? null : address.getZipcode();
        this.detailedAddress = address == null ? null : address.getDetailedAddress();
        this.addressName = address == null ? null : address.getAddressName();
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
