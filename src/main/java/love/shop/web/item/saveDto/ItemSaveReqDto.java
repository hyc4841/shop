package love.shop.web.item.saveDto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LapTopSaveReqDto.class, name = "LapTop"),
        @JsonSubTypes.Type(value = BookSaveReqDto.class, name = "Book"),
})
@Data
@RequiredArgsConstructor
public class ItemSaveReqDto {

    @NotBlank
    private String name;
    @NotNull
    private Integer price;
    @NotNull
    private Integer stockQuantity;

    private List<Integer> categoriesId; // 카테고리도 반드시 있어야하긴 한데..

    @NotBlank
    private String dataType;

    public ItemSaveReqDto(String name, int price, int stockQuantity, List<Integer> categoriesId, String dataType) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.categoriesId = categoriesId;
        this.dataType = dataType;
    }

    public String getDataType() {
        return dataType;
    }


}


/*
// ItemSaveReqDto API

{
---- 공통 필드 ----
"type": "LapTop",
"name": "testLapTop",
"price": 50000,
"stockQuantity": 9999,

---- 각 객체에 맞는 필드 ----
"categoriesId": [3, 32],
"dataType": "LapTop",
"lapTopBrand": "그램15",
"lapTopCpu": "코어i9",
"lapTopStorage": "TB_1",
"lapTopScreenSize": "Inch_15",
"lapTopManufactureBrand": "LG전자"}

 */

