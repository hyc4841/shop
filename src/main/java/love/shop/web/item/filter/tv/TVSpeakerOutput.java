package love.shop.web.item.filter.tv;

import java.util.ArrayList;
import java.util.List;

public class TVSpeakerOutput {

    private List<String> tvSpeakerOutput;

    public TVSpeakerOutput() {
        this.tvSpeakerOutput = createTVSpeakerOutputlList();
    }

    public static List<String> createTVSpeakerOutputlList() {
        List<String> tvSoundList = new ArrayList<>();
        tvSoundList.add("20W");
        tvSoundList.add("40W");
        tvSoundList.add("60W");

        return tvSoundList;
    }
}
