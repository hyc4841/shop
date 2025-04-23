package love.shop.domain.itemSpec;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import love.shop.web.item.spec.tv.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("Tv")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TvSpec extends ItemSpec {

    @Id
    @GeneratedValue
    @Column(name = "tv_spec_id")
    private Long id;

    private final String dataType = "Tv";

    @OneToMany(mappedBy = "tvSpec", cascade = CascadeType.PERSIST) // 이거 이름 맞나?
    private List<TvBrand> tvBrand = new ArrayList<>();
    @OneToMany(mappedBy = "tvSpec", cascade = CascadeType.PERSIST)
    private List<TvDisplayPanel> tvDisplayPanels = new ArrayList<>();
    @OneToMany(mappedBy = "tvSpec", cascade = CascadeType.PERSIST)
    private List<TvDisplayType> tvDisplayTypes = new ArrayList<>();
    @OneToMany(mappedBy = "tvSpec", cascade = CascadeType.PERSIST)
    private List<TvHDR> tvHDRS = new ArrayList<>();
    @OneToMany(mappedBy = "tvSpec", cascade = CascadeType.PERSIST)
    private List<TvManufacturer> tvManufacturers = new ArrayList<>();
    @OneToMany(mappedBy = "tvSpec", cascade = CascadeType.PERSIST)
    private List<TvPictureQuality> tvPictureQualities = new ArrayList<>();
    @OneToMany(mappedBy = "tvSpec", cascade = CascadeType.PERSIST)
    private List<TvProcessor> tvProcessors = new ArrayList<>();
    @OneToMany(mappedBy = "tvSpec", cascade = CascadeType.PERSIST)
    private List<TvRefreshRate> tvRefreshRates = new ArrayList<>();
    @OneToMany(mappedBy = "tvSpec", cascade = CascadeType.PERSIST)
    private List<TvResolution> tvResolutions = new ArrayList<>();
    @OneToMany(mappedBy = "tvSpec", cascade = CascadeType.PERSIST)
    private List<TvScreenSize> tvScreenSizes = new ArrayList<>();
    @OneToMany(mappedBy = "tvSpec", cascade = CascadeType.PERSIST)
    private List<TvSound> tvSounds = new ArrayList<>();;
    @OneToMany(mappedBy = "tvSpec", cascade = CascadeType.PERSIST)
    private List<TvSpeakerChannel> tvSpeakerChannels = new ArrayList<>();
    @OneToMany(mappedBy = "tvSpec", cascade = CascadeType.PERSIST)
    private List<TvSpeakerOutput> tvSpeakerOutputs = new ArrayList<>();

    public TvSpec(String dataType) {
        super(dataType);
    }

    @Override
    public String getDataType() {
        return dataType;
    }
}
