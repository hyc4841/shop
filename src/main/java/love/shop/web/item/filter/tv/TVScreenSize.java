package love.shop.web.item.filter.tv;

import java.util.ArrayList;
import java.util.List;

public class TVScreenSize { // 화면 크기

    private List<String> tvScreenSize;

    public TVScreenSize() {
        this.tvScreenSize = createTVScreenSizeList();
    }

    public static List<String> createTVScreenSizeList() {
        List<String> tvPictureQuality = new ArrayList<>();
        tvPictureQuality.add("24인치");
        tvPictureQuality.add("27인치");
        tvPictureQuality.add("32인치");
        tvPictureQuality.add("40인치");
        tvPictureQuality.add("42인치");
        tvPictureQuality.add("48인치");
        tvPictureQuality.add("50인치");
        tvPictureQuality.add("55인치");
        tvPictureQuality.add("65인치");
        tvPictureQuality.add("70인치");
        tvPictureQuality.add("75인치");
        tvPictureQuality.add("77인치");
        tvPictureQuality.add("83인치");
        tvPictureQuality.add("85인치");
        tvPictureQuality.add("86인치");
        tvPictureQuality.add("97인치");
        tvPictureQuality.add("98인치");
        tvPictureQuality.add("100인치");
        tvPictureQuality.add("115인치");


        return tvPictureQuality;
    }
}
