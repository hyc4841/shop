package love.shop.web.item.filter.tv;

import java.util.ArrayList;
import java.util.List;

public class TVSpeakerChannel {

    private List<String> tvSpeakerChannel;

    public TVSpeakerChannel() {
        this.tvSpeakerChannel = createTVSpeakerChannelList();
    }

    public static List<String> createTVSpeakerChannelList() {
        List<String> tvSoundList = new ArrayList<>();
        tvSoundList.add("2.0채널");
        tvSoundList.add("2.0.2채널");
        tvSoundList.add("2.1채널");
        tvSoundList.add("2.1.2채널");
        tvSoundList.add("3.1.2채널");
        tvSoundList.add("4.0채널");
        tvSoundList.add("4.1채널");
        tvSoundList.add("4.2채널");

        return tvSoundList;
    }
}
