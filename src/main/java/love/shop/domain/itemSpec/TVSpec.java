package love.shop.domain.itemSpec;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import love.shop.web.item.filter.tv.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TVSpec {

    @Id
    @GeneratedValue
    @Column(name = "tv_spec_id")
    private Long id;

    @OneToMany(mappedBy = "TvSpec", cascade = CascadeType.PERSIST) // 이거 이름 맞나?
    private List<TVBrand> tvBrands = new ArrayList<>();
    @OneToMany
    private List<TVDisplayPanel> tvDisplayPanels;
    @OneToMany
    private List<TVDisplayType> tvDisplayTypes;
    @OneToMany
    private List<TVHDR> tvHDRs;
    @OneToMany
    private List<TVManufacturer> tvManufacturers;
    @OneToMany
    private List<String> tvPictureQualities;
    @OneToMany
    private List<TVProcessor> tvProcessors;
    @OneToMany
    private List<TVRefreshRate> tvRefreshRates;
    @OneToMany
    private List<TVResolution> tvResolutions;
    @OneToMany
    private List<String> tvScreenSizes;
    @OneToMany
    private List<String> tvSounds;
    @OneToMany
    private List<String> tvSpeakerChannels;
    @OneToMany
    private List<String> tvSpeakerOutputs;

}
