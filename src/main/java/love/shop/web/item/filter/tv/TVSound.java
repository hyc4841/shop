package love.shop.web.item.filter.tv;

import java.util.ArrayList;
import java.util.List;

public class TVSound {

    private List<String> tvSounds;

    public TVSound() {
        this.tvSounds = createTVSoundList();
    }

    public static List<String> createTVSoundList() {
        List<String> tvSoundList = new ArrayList<>();
        tvSoundList.add("DTS:X");
        tvSoundList.add("돌비애트모스");
        tvSoundList.add("DTS-VIRTUAL:X");
        tvSoundList.add("dbx-tv");
        tvSoundList.add("공간인식");
        tvSoundList.add("블루투스오디오");
        tvSoundList.add("사운드바동시출력");
        tvSoundList.add("WiSA스피커");

        return tvSoundList;
    }

}
