package love.shop.web.item.filter.tv;

import java.util.ArrayList;
import java.util.List;

public class TVPictureQuality {

    private List<String> tvPictureQuality;

    public static List<String> createTVPictureQualityList() {
        List<String> tvPictureQuality = new ArrayList<>();
        tvPictureQuality.add("4K업스케일링");
        tvPictureQuality.add("장르맞춤화면");
        tvPictureQuality.add("모션보간(MEMC)");

        return tvPictureQuality;
    }



}
