package love.shop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.category.Category;
import love.shop.domain.item.Item;
import love.shop.domain.itemOption.ItemOption;
import love.shop.domain.itemSpec.spec.lapTop.*;
import love.shop.domain.itemSpec.spec.tv.TvSpec;
import love.shop.domain.itemSpec.spec.tv.*;
import love.shop.domain.member.Gender;
import love.shop.domain.member.Member;
import love.shop.domain.member.PasswordAndCheck;
import love.shop.domain.salesPage.SalesPage;
import love.shop.service.item.ItemService;
import love.shop.service.member.MemberService;
import love.shop.service.page.PageService;
import love.shop.web.item.saveDto.TVSaveReqDto;
import love.shop.web.signup.dto.SignupRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService service;

    @PostConstruct
    public void init() throws MethodArgumentNotValidException {
//        service.initCategories();
//        service.initMember();
//        service.initItem();
//        service.initItemSpec();
//        service.pageInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final ItemService itemService;
        private final PageService pageService;
        private final MemberService memberService;

        public void initItemSpec() {

            LapTopSpec lapTopSpec = new LapTopSpec("LapTop");

            new LapTopManufactureBrand("msi", true, lapTopSpec);
            new LapTopManufactureBrand("LG전자", true, lapTopSpec);
            new LapTopManufactureBrand("삼성전자", true, lapTopSpec);
            new LapTopManufactureBrand("레노버", true, lapTopSpec);
            new LapTopManufactureBrand("ASUS", true, lapTopSpec);
            new LapTopManufactureBrand("HP", false, lapTopSpec);
            new LapTopManufactureBrand("APPLE", false, lapTopSpec);

            new LapTopCpu("코어7", true, lapTopSpec);
            new LapTopCpu("코어5", true, lapTopSpec);
            new LapTopCpu("라이젠9", true, lapTopSpec);
            new LapTopCpu("라이젠7", true, lapTopSpec);
            new LapTopCpu("M1", true, lapTopSpec);
            new LapTopCpu("M2", false, lapTopSpec);
            new LapTopCpu("코어3", false, lapTopSpec);
            new LapTopCpu("라이젠5", false, lapTopSpec);

            new LapTopBrand("갤럭시북5 프로", true, lapTopSpec);
            new LapTopBrand("아이디어패드", true, lapTopSpec);
            new LapTopBrand("오멘", true, lapTopSpec);
            new LapTopBrand("갤럭시북", true, lapTopSpec);
            new LapTopBrand("그램16", true, lapTopSpec);
            new LapTopBrand("그램15", false, lapTopSpec);
            new LapTopBrand("맥북에어13", false, lapTopSpec);
            new LapTopBrand("맥북에어15", false, lapTopSpec);

            new LapTopScreenSize("14인치", true, lapTopSpec);
            new LapTopScreenSize("15인치", true, lapTopSpec);
            new LapTopScreenSize("16인치", true, lapTopSpec);
            new LapTopScreenSize("17인치", true, lapTopSpec);
            new LapTopScreenSize("13인치", false, lapTopSpec);
            new LapTopScreenSize("12인치", false, lapTopSpec);
            new LapTopScreenSize("8인치", false, lapTopSpec);

            new LapTopStorage("128GB", true, lapTopSpec);
            new LapTopStorage("256GB", true, lapTopSpec);
            new LapTopStorage("512GB", true, lapTopSpec);
            new LapTopStorage("1TB", true, lapTopSpec);
            new LapTopStorage("2TB", false, lapTopSpec);
            new LapTopStorage("3TB", false, lapTopSpec);

            itemService.saveItemSpecList(lapTopSpec);

            TvSpec tvSpec = new TvSpec("Tv");
            // tvBrand 영역
            new TvBrand("나노셀", tvSpec, true);
            new TvBrand("네모로", tvSpec, true);
            new TvBrand("네오QLED", tvSpec, true);
            new TvBrand("노바", tvSpec, true);
            new TvBrand("노브랜드", tvSpec, true);
            new TvBrand("더 세로", tvSpec, false);
            new TvBrand("더 세리프", tvSpec, false);
            new TvBrand("더 프레임", tvSpec, false);
            new TvBrand("데이니즈", tvSpec, false);
            new TvBrand("드림TV", tvSpec, false);
            new TvBrand("디지털사이니지", tvSpec, false);
            new TvBrand("라익미", tvSpec, false);
            new TvBrand("루컴즈전자", tvSpec, false);
            new TvBrand("룸앤TV", tvSpec, false);
            new TvBrand("몬스터박스", tvSpec, false);
            new TvBrand("스마트뷰", tvSpec, false);
            new TvBrand("스탠바이미", tvSpec, false);
            new TvBrand("스탠바이미2", tvSpec, false);
            new TvBrand("시그니처", tvSpec, false);
            new TvBrand("아도니스", tvSpec, false);
            new TvBrand("아이뮤즈", tvSpec, false);
            new TvBrand("아인츠", tvSpec, false);
            new TvBrand("아임e", tvSpec, false);
            new TvBrand("아텝", tvSpec, false);
            new TvBrand("에어리브", tvSpec, false);
            new TvBrand("에코비전", tvSpec, false);
            new TvBrand("엑사비오", tvSpec, false);
            new TvBrand("브제컬렉션", tvSpec, false);
            new TvBrand("올레드", tvSpec, false);
            new TvBrand("올레드 evo", tvSpec, false);
            new TvBrand("와이드뷰", tvSpec, false);
            new TvBrand("우버", tvSpec, false);
            new TvBrand("울트라HD", tvSpec, false);
            new TvBrand("익스프레스럭코리아", tvSpec, false);
            new TvBrand("인피니아", tvSpec, false);
            new TvBrand("일렉트로맨", tvSpec, false);
            new TvBrand("제스타", tvSpec, false);
            new TvBrand("치크", tvSpec, false);
            new TvBrand("카레나", tvSpec, false);
            new TvBrand("코스모", tvSpec, false);
            new TvBrand("쿠카", tvSpec, false);
            new TvBrand("큐닉스", tvSpec, false);
            new TvBrand("큐벨", tvSpec, false);
            new TvBrand("큐빅스전자", tvSpec, false);
            new TvBrand("큐스타", tvSpec, false);
            new TvBrand("클라쎄", tvSpec, false);
            new TvBrand("파브", tvSpec, false);
            new TvBrand("플럭스", tvSpec, false);
            new TvBrand("하이메이드", tvSpec, false);
            new TvBrand("행운", tvSpec, false);
            new TvBrand("호텔TV", tvSpec, false);
            new TvBrand("APEX", tvSpec, false);
            new TvBrand("홈플래닛", tvSpec, false);
            new TvBrand("BoNe", tvSpec, false);
            new TvBrand("BOSSWIZ", tvSpec, false);
            new TvBrand("Crystal UHD", tvSpec, false);
            new TvBrand("HOOK TV", tvSpec, false);
            new TvBrand("METZ", tvSpec, false);
            new TvBrand("MONEX", tvSpec, false);
            new TvBrand("MOTV", tvSpec, false);
            new TvBrand("MOZEE", tvSpec, false);
            new TvBrand("MyView", tvSpec, false);
            new TvBrand("Newsync", tvSpec, false);
            new TvBrand("PANTHEON", tvSpec, false);
            new TvBrand("PLANTIUM", tvSpec, false);
            new TvBrand("QLED", tvSpec, false);
            new TvBrand("QNED", tvSpec, false);
            new TvBrand("QNED evo", tvSpec, false);
            new TvBrand("SMART", tvSpec, false);
            new TvBrand("TiBi", tvSpec, false);
            new TvBrand("ViewSync", tvSpec, false);
            new TvBrand("ViewSys", tvSpec, false);
            new TvBrand("ZEPA", tvSpec, false);

            // TvDisplayPanel
            new TvDisplayPanel("IPS패널계열", tvSpec, false);
            new TvDisplayPanel("VA패널계열", tvSpec, false);

            // TvDisplayType
            new TvDisplayType("OLED TV", tvSpec, true);
            new TvDisplayType("미니LED TV", tvSpec, true);
            new TvDisplayType("QLED TV", tvSpec, true);
            new TvDisplayType("QNED TV", tvSpec, true);
            new TvDisplayType("LED 모니터", tvSpec, true);
            new TvDisplayType("상업용디스플레이", tvSpec, false);

            // TvHDR
            new TvHDR("돌비비전IQ", tvSpec, true);
            new TvHDR("돌비비전", tvSpec, true);
            new TvHDR("HDR10+", tvSpec, true);
            new TvHDR("HDR10", tvSpec, true);
            new TvHDR("HDR자동조절", tvSpec, true);
            new TvHDR("HLG", tvSpec, false);
            new TvHDR("아이맥스인핸스드", tvSpec, false);
            new TvHDR("톤매핑", tvSpec, false);

            // TvManufacturer
            new TvManufacturer("경성글로벌코리아", tvSpec, true);
            new TvManufacturer("금영", tvSpec, true);
            new TvManufacturer("넥스디지탈", tvSpec, true);
            new TvManufacturer("다우리", tvSpec, true);
            new TvManufacturer("담테크", tvSpec, true);
            new TvManufacturer("대성글로벌코리아", tvSpec, false);
            new TvManufacturer("대우디스플레이", tvSpec, false);
            new TvManufacturer("대우써머스", tvSpec, false);
            new TvManufacturer("대우테크", tvSpec, false);
            new TvManufacturer("더함", tvSpec, false);
            new TvManufacturer("델로라", tvSpec, false);
            new TvManufacturer("델로스", tvSpec, false);
            new TvManufacturer("델리파스", tvSpec, false);
            new TvManufacturer("도안파비스", tvSpec, false);
            new TvManufacturer("디에스샵", tvSpec, false);
            new TvManufacturer("디엑스", tvSpec, false);
            new TvManufacturer("디엘티", tvSpec, false);
            new TvManufacturer("디지탈맥스", tvSpec, false);
            new TvManufacturer("라익미", tvSpec, false);
            new TvManufacturer("래안텍", tvSpec, false);
            new TvManufacturer("레닷", tvSpec, false);
            new TvManufacturer("롯데하이마트", tvSpec, false);
            new TvManufacturer("루컴즈전자", tvSpec, false);
            new TvManufacturer("리치웰디바이스", tvSpec, false);
            new TvManufacturer("리플렉스", tvSpec, false);
            new TvManufacturer("마루나", tvSpec, false);
            new TvManufacturer("메가", tvSpec, false);
            new TvManufacturer("메가자이언트", tvSpec, false);
            new TvManufacturer("메디하임", tvSpec, false);
            new TvManufacturer("미디어빌리지테크", tvSpec, false);
            new TvManufacturer("비엠텍씨앤씨", tvSpec, false);
            new TvManufacturer("비트엠", tvSpec, false);
            new TvManufacturer("살루스", tvSpec, false);
            new TvManufacturer("삼성전자", tvSpec, false);
            new TvManufacturer("샤오미", tvSpec, false);
            new TvManufacturer("센티오", tvSpec, false);
            new TvManufacturer("스마트테크", tvSpec, false);
            new TvManufacturer("스마트플랫", tvSpec, false);
            new TvManufacturer("시스메이트", tvSpec, false);
            new TvManufacturer("시티브", tvSpec, false);
            new TvManufacturer("신일전자", tvSpec, false);
            new TvManufacturer("써밋티비", tvSpec, false);
            new TvManufacturer("아남전자", tvSpec, false);
            new TvManufacturer("아바이노베이션", tvSpec, false);
            new TvManufacturer("아워스페이스", tvSpec, false);
            new TvManufacturer("아이리버", tvSpec, false);
            new TvManufacturer("아이사", tvSpec, false);
            new TvManufacturer("아이온코리아", tvSpec, false);
            new TvManufacturer("아이티스토리글로벌", tvSpec, false);
            new TvManufacturer("알에프", tvSpec, false);
            new TvManufacturer("에스티뷰", tvSpec, false);
            new TvManufacturer("에스포", tvSpec, false);
            new TvManufacturer("엔클롬", tvSpec, false);
            new TvManufacturer("엠베스텍", tvSpec, false);
            new TvManufacturer("엠텍코리아", tvSpec, false);
            new TvManufacturer("영코리아", tvSpec, false);
            new TvManufacturer("옐로우나이프", tvSpec, false);
            new TvManufacturer("오존컴퍼니", tvSpec, false);
            new TvManufacturer("오토모", tvSpec, false);
            new TvManufacturer("올인닷컴", tvSpec, false);
            new TvManufacturer("와사비망고", tvSpec, false);
            new TvManufacturer("와이드뷰", tvSpec, false);
            new TvManufacturer("와이드테크", tvSpec, false);
            new TvManufacturer("우신총판", tvSpec, false);
            new TvManufacturer("원테크", tvSpec, false);
            new TvManufacturer("웨스팅하우스", tvSpec, false);
            new TvManufacturer("위니아", tvSpec, false);
            new TvManufacturer("위니아전자", tvSpec, false);
            new TvManufacturer("위드라이프", tvSpec, false);
            new TvManufacturer("유니피엠", tvSpec, false);
            new TvManufacturer("유맥스", tvSpec, false);
            new TvManufacturer("이노스", tvSpec, false);
            new TvManufacturer("이노시아", tvSpec, false);
            new TvManufacturer("이마트", tvSpec, false);
            new TvManufacturer("이브이인터내셔널", tvSpec, false);
            new TvManufacturer("ESTLA", tvSpec, false);
            new TvManufacturer("이엔티비", tvSpec, false);
            new TvManufacturer("이지아이티시스템", tvSpec, false);
            new TvManufacturer("익스프레스럭코리아", tvSpec, false);
            new TvManufacturer("인켈", tvSpec, false);
            new TvManufacturer("자비오씨엔씨", tvSpec, false);
            new TvManufacturer("잘컴", tvSpec, false);
            new TvManufacturer("장은테크", tvSpec, false);
            new TvManufacturer("재원전자", tvSpec, false);
            new TvManufacturer("저스트인테크", tvSpec, false);
            new TvManufacturer("제노스미디어", tvSpec, false);
            new TvManufacturer("제이투닷컴", tvSpec, false);
            new TvManufacturer("주연전자", tvSpec, false);
            new TvManufacturer("주연테크", tvSpec, false);
            new TvManufacturer("지닷카멜레온", tvSpec, false);
            new TvManufacturer("지온디스플레이", tvSpec, false);
            new TvManufacturer("지원아이앤씨", tvSpec, false);
            new TvManufacturer("창홍", tvSpec, false);
            new TvManufacturer("카멜", tvSpec, false);
            new TvManufacturer("카이져", tvSpec, false);
            new TvManufacturer("컴스톤", tvSpec, false);
            new TvManufacturer("케이티에이코리아", tvSpec, false);
            new TvManufacturer("코모도", tvSpec, false);
            new TvManufacturer("쿠잉", tvSpec, false);
            new TvManufacturer("쿠팡", tvSpec, false);
            new TvManufacturer("큐닉스그룹", tvSpec, false);
            new TvManufacturer("큐브코리아", tvSpec, false);
            new TvManufacturer("크라이저", tvSpec, false);
            new TvManufacturer("클라인즈", tvSpec, false);
            new TvManufacturer("키고조명", tvSpec, false);
            new TvManufacturer("타키온", tvSpec, false);
            new TvManufacturer("트루비", tvSpec, false);
            new TvManufacturer("트리", tvSpec, false);
            new TvManufacturer("티베라", tvSpec, false);
            new TvManufacturer("티브이지", tvSpec, false);
            new TvManufacturer("티비루", tvSpec, false);
            new TvManufacturer("티비아이티", tvSpec, false);
            new TvManufacturer("티비존", tvSpec, false);
            new TvManufacturer("티비퀸", tvSpec, false);
            new TvManufacturer("티엑스", tvSpec, false);
            new TvManufacturer("파트로", tvSpec, false);
            new TvManufacturer("포스텍", tvSpec, false);
            new TvManufacturer("포에스", tvSpec, false);
            new TvManufacturer("포유디지탈", tvSpec, false);
            new TvManufacturer("폴라로이드", tvSpec, false);
            new TvManufacturer("PRISM", tvSpec, false);
            new TvManufacturer("프리토스", tvSpec, false);
            new TvManufacturer("피데스", tvSpec, false);
            new TvManufacturer("피디케이전자", tvSpec, false);
            new TvManufacturer("필루체", tvSpec, false);
            new TvManufacturer("필립스", tvSpec, false);
            new TvManufacturer("하나디에스", tvSpec, false);
            new TvManufacturer("하이드림LCD", tvSpec, false);
            new TvManufacturer("하이센스", tvSpec, false);
            new TvManufacturer("한샘", tvSpec, false);
            new TvManufacturer("한성컴퓨터", tvSpec, false);
            new TvManufacturer("한화테크윈", tvSpec, false);
            new TvManufacturer("화봄", tvSpec, false);
            new TvManufacturer("ATEN", tvSpec, false);
            new TvManufacturer("CIMA", tvSpec, false);
            new TvManufacturer("GMOUNT", tvSpec, false);
            new TvManufacturer("HKC", tvSpec, false);
            new TvManufacturer("JVC", tvSpec, false);
            new TvManufacturer("KT CS", tvSpec, false);
            new TvManufacturer("LG전자", tvSpec, false);
            new TvManufacturer("LNP", tvSpec, false);
            new TvManufacturer("MARK", tvSpec, false);
            new TvManufacturer("METZ", tvSpec, false);
            new TvManufacturer("NEC", tvSpec, false);
            new TvManufacturer("OIC 코리아", tvSpec, false);
            new TvManufacturer("OTC", tvSpec, false);
            new TvManufacturer("PICO", tvSpec, false);
            new TvManufacturer("TCL", tvSpec, false);
            new TvManufacturer("TJ미디어", tvSpec, false);
            new TvManufacturer("TNM", tvSpec, false);
            new TvManufacturer("TUTU", tvSpec, false);
            new TvManufacturer("VRID", tvSpec, false);

            // TvPictureQuality
            new TvPictureQuality("필름메이커모드", tvSpec, true);
            new TvPictureQuality("모션보간(MEMC)", tvSpec, true);
            new TvPictureQuality("장르맞춤화면", tvSpec, true);
            new TvPictureQuality("스마트캘리브레이션", tvSpec, true);
            new TvPictureQuality("8K업스케일링", tvSpec, true);
            new TvPictureQuality("4K업스케일링", tvSpec, false);
            new TvPictureQuality("스마트캘리브레이션Pro", tvSpec, false);

            // TvProcessor
            new TvProcessor("알파11 AI", tvSpec, true);
            new TvProcessor("알파8 AI", tvSpec, true);
            new TvProcessor("알파9 7세대", tvSpec, true);
            new TvProcessor("알파5 7세대", tvSpec, true);
            new TvProcessor("알파9 6세대", tvSpec, true);
            new TvProcessor("알파7 6세대", tvSpec, false);
            new TvProcessor("알파5 6세대", tvSpec, false);
            new TvProcessor("알파9 5세대", tvSpec, false);
            new TvProcessor("알파7 5세대", tvSpec, false);
            new TvProcessor("알파5 5세대", tvSpec, false);
            new TvProcessor("알파9 4세대", tvSpec, false);
            new TvProcessor("알파7 4세대", tvSpec, false);
            new TvProcessor("알파9 3세대", tvSpec, false);
            new TvProcessor("알파7 3세대", tvSpec, false);
            new TvProcessor("알파9 2세대", tvSpec, false);
            new TvProcessor("알파7 2세대", tvSpec, false);
            new TvProcessor("NQ4 AI Gen2", tvSpec, false);
            new TvProcessor("NQ8 AI Gen2", tvSpec, false);
            new TvProcessor("NQ8 AI Gen3", tvSpec, false);
            new TvProcessor("NQ4 AI Gen3", tvSpec, false);
            new TvProcessor("뉴럴퀀텀8K", tvSpec, false);
            new TvProcessor("뉴럴퀀텀8K Lite", tvSpec, false);
            new TvProcessor("뉴럴퀀텀4K", tvSpec, false);
            new TvProcessor("네오퀀텀8K", tvSpec, false);
            new TvProcessor("네오퀀텀4K", tvSpec, false);
            new TvProcessor("퀀텀8K", tvSpec, false);
            new TvProcessor("퀀텀4K", tvSpec, false);
            new TvProcessor("퀀텀4K Lite", tvSpec, false);
            new TvProcessor("크리스털UHD", tvSpec, false);
            new TvProcessor("UHD엔진", tvSpec, false);
            new TvProcessor("MT9972", tvSpec, false);
            new TvProcessor("MT9900", tvSpec, false);
            new TvProcessor("MT9675", tvSpec, false);
            new TvProcessor("MT9653", tvSpec, false);
            new TvProcessor("MT9653(펜토닉700)", tvSpec, false);
            new TvProcessor("MT9618", tvSpec, false);
            new TvProcessor("MT9617", tvSpec, false);
            new TvProcessor("MT9612", tvSpec, false);
            new TvProcessor("MT9611", tvSpec, false);
            new TvProcessor("MT9603", tvSpec, false);
            new TvProcessor("MT9602", tvSpec, false);
            new TvProcessor("MT9216", tvSpec, false);
            new TvProcessor("MT5599", tvSpec, false);
            new TvProcessor("MSTAR MSD9216", tvSpec, false);
            new TvProcessor("MSTAR MSD6886", tvSpec, false);
            new TvProcessor("MSTAR MSD6683", tvSpec, false);
            new TvProcessor("MSTAR MSD6586", tvSpec, false);
            new TvProcessor("MSTAR MSD3683", tvSpec, false);
            new TvProcessor("MSTAR MSD3553", tvSpec, false);
            new TvProcessor("MSTAR MSD3458", tvSpec, false);
            new TvProcessor("MSTAR MSD3393", tvSpec, false);
            new TvProcessor("RT2885", tvSpec, false);
            new TvProcessor("RTD2880", tvSpec, false);
            new TvProcessor("RT2851", tvSpec, false);
            new TvProcessor("RT2841", tvSpec, false);
            new TvProcessor("RTD2851", tvSpec, false);
            new TvProcessor("RT2875P", tvSpec, false);
            new TvProcessor("AiPQ", tvSpec, false);
            new TvProcessor("AiPQ 2.0", tvSpec, false);
            new TvProcessor("AiPQ 3.0", tvSpec, false);
            new TvProcessor("쿼드 A55", tvSpec, false);
            new TvProcessor("알파8 AI Gen2", tvSpec, false);
            new TvProcessor("알파11 4K AI Gen2", tvSpec, false);
            new TvProcessor("알파9 4K AI Gen8", tvSpec, false);
            new TvProcessor("알파7 4K AI Gen8", tvSpec, false);

            // TvRefreshRate
            new TvRefreshRate("144Hz", tvSpec, true);
            new TvRefreshRate("120Hz", tvSpec, true);
            new TvRefreshRate("75Hz", tvSpec, true);
            new TvRefreshRate("60Hz", tvSpec, true);

            // TvResolution
            new TvResolution("8K UHD", tvSpec, true);
            new TvResolution("4K UHD", tvSpec, true);
            new TvResolution("QHD", tvSpec, true);
            new TvResolution("FHD", tvSpec, true);
            new TvResolution("HD", tvSpec, true);

            // TvScreenSize
            new TvScreenSize("43인치(109cm)", tvSpec, true);
            new TvScreenSize("55인치(139cm)", tvSpec, true);
            new TvScreenSize("65인치(163cm)", tvSpec, true);
            new TvScreenSize("75인치(189cm)", tvSpec, true);
            new TvScreenSize("85인치(215cm)", tvSpec, true);
            new TvScreenSize("185인치(467cm)", tvSpec, false);
            new TvScreenSize("170인치(432cm)", tvSpec, false);
            new TvScreenSize("150인치(381cm)", tvSpec, false);
            new TvScreenSize("130인치(330cm)", tvSpec, false);
            new TvScreenSize("115인치(291cm)", tvSpec, false);
            new TvScreenSize("110인치(279cm)", tvSpec, false);
            new TvScreenSize("109인치(277cm)", tvSpec, false);
            new TvScreenSize("100인치(254cm)", tvSpec, false);
            new TvScreenSize("98인치(248cm)", tvSpec, false);
            new TvScreenSize("97인치(245cm)", tvSpec, false);
            new TvScreenSize("88인치(222cm)", tvSpec, false);
            new TvScreenSize("86인치(218cm)", tvSpec, false);
            new TvScreenSize("83인치(209cm)", tvSpec, false);
            new TvScreenSize("82인치(207cm)", tvSpec, false);
            new TvScreenSize("79인치(199cm)", tvSpec, false);
            new TvScreenSize("77인치(195cm)", tvSpec, false);
            new TvScreenSize("72인치(183cm)", tvSpec, false);
            new TvScreenSize("70인치(177cm)", tvSpec, false);
            new TvScreenSize("64인치(162cm)", tvSpec, false);
            new TvScreenSize("60인치(152cm)", tvSpec, false);
            new TvScreenSize("58인치(147cm)", tvSpec, false);
            new TvScreenSize("50인치(127cm)", tvSpec, false);
            new TvScreenSize("49인치(123cm)", tvSpec, false);
            new TvScreenSize("48인치(122cm)", tvSpec, false);
            new TvScreenSize("47인치(119cm)", tvSpec, false);
            new TvScreenSize("46인치(116cm)", tvSpec, false);
            new TvScreenSize("42인치(106cm)", tvSpec, false);
            new TvScreenSize("40인치(101cm)", tvSpec, false);
            new TvScreenSize("37인치(94cm)", tvSpec, false);
            new TvScreenSize("36인치(91cm)", tvSpec, false);
            new TvScreenSize("32인치(81cm)", tvSpec, false);
            new TvScreenSize("30인치(76cm)", tvSpec, false);
            new TvScreenSize("29인치(73cm)", tvSpec, false);
            new TvScreenSize("28인치(70cm)", tvSpec, false);
            new TvScreenSize("27인치(69cm)", tvSpec, false);
            new TvScreenSize("24인치(61cm)", tvSpec, false);
            new TvScreenSize("22인치(55cm)", tvSpec, false);
            new TvScreenSize("21인치(53cm)", tvSpec, false);
            new TvScreenSize("20인치(51cm)", tvSpec, false);
            new TvScreenSize("14인치(35cm)", tvSpec, false);
            new TvScreenSize("13인치(33cm)", tvSpec, false);
            new TvScreenSize("12인치(30cm)", tvSpec, false);
            new TvScreenSize("10인치(25cm)", tvSpec, false);

            // TvSound
            new TvSound("블루투스오디오", tvSpec, true);
            new TvSound("사운드바동시출력", tvSpec, true);
            new TvSound("돌비애트모스", tvSpec, true);
            new TvSound("DTS:X", tvSpec, true);
            new TvSound("DTS-VIRTUAL:X", tvSpec, true);
            new TvSound("dbx-tv", tvSpec, false);
            new TvSound("공간인식", tvSpec, false);
            new TvSound("WiSA스피커", tvSpec, false);

            // TvSpeakerChannel
            new TvSpeakerChannel("2.0채널", tvSpec, true);
            new TvSpeakerChannel("2.0.2채널", tvSpec, true);
            new TvSpeakerChannel("2.1채널", tvSpec, true);
            new TvSpeakerChannel("2.1.2채널", tvSpec, true);
            new TvSpeakerChannel("2.2채널", tvSpec, true);
            new TvSpeakerChannel("2.2.2채널", tvSpec, false);
            new TvSpeakerChannel("3.1.2채널", tvSpec, false);
            new TvSpeakerChannel("4.0채널", tvSpec, false);
            new TvSpeakerChannel("4.1채널", tvSpec, false);
            new TvSpeakerChannel("4.2채널", tvSpec, false);
            new TvSpeakerChannel("4.2.2채널", tvSpec, false);
            new TvSpeakerChannel("6.2.2채널", tvSpec, false);
            new TvSpeakerChannel("6.2.4채널", tvSpec, false);
            new TvSpeakerChannel("6.4.4채널", tvSpec, false);

            // TvSpeakerOutput
            new TvSpeakerOutput("~19W", tvSpec, true);
            new TvSpeakerOutput("20W", tvSpec, true);
            new TvSpeakerOutput("~40W", tvSpec, true);
            new TvSpeakerOutput("~60W", tvSpec, true);
            new TvSpeakerOutput("61~W", tvSpec, true);

            itemService.saveItemSpecList(tvSpec);
        }

        public void initItem() {
            // 1. ItemSaveReqDto 생성
            List<Integer> categories1 = new ArrayList<>(Arrays.asList(2, 23, 193));
            TVSaveReqDto tv1 = new TVSaveReqDto("27LX6TPGA", 1103200, 9999, categories1, "TV",
                    "스탠바이미", "IPS","LED_TV", "HDR10", "LG전자",
                    null, null, "Hz_60", "QHD", "27인치",
                    "돌비애트모스", null, null);
            itemService.saveItemWithCategory(tv1);

            TVSaveReqDto tv2 = new TVSaveReqDto("27LX6TPGA", 1103200, 9999, categories1, "TV",
                    "스탠바이미", "IPS","LED_TV", "HDR10", "LG전자",
                    null, null, "Hz_60", "QHD", "27인치",
                    "돌비애트모스", null, null);
            itemService.saveItemWithCategory(tv2);

            TVSaveReqDto tv3 = new TVSaveReqDto("27LX6TPGA", 1103200, 9999, categories1, "TV",
                    "스탠바이미", "IPS","LED_TV", "HDR10", "LG전자",
                    null, null, "Hz_60", "QHD", "27인치",
                    "돌비애트모스", null, null);
            itemService.saveItemWithCategory(tv3);

            TVSaveReqDto tv4 = new TVSaveReqDto("27LX6TPGA", 1103200, 9999, categories1, "TV",
                    "스탠바이미", "IPS","LED_TV", "HDR10", "LG전자",
                    null, null, "Hz_60", "QHD", "27인치",
                    "돌비애트모스", null, null);
            itemService.saveItemWithCategory(tv4);

            TVSaveReqDto tv5 = new TVSaveReqDto("27LX6TPGA", 1103200, 9999, categories1, "TV",
                    "스탠바이미", "IPS","LED_TV", "HDR10", "LG전자",
                    null, null, "Hz_60", "QHD", "27인치",
                    "돌비애트모스", null, null);
            itemService.saveItemWithCategory(tv5);

        }

        public void initMember() {
            PasswordAndCheck passwordAndCheck = new PasswordAndCheck("1234", "1234");

            SignupRequestDto signupMember1 = new SignupRequestDto("hyc4841", passwordAndCheck, "황윤철", "01099694841",
                    "dbscjf4841@naver.com", LocalDate.of(1997, 6, 3), Gender.MAN, "서울", "백련산로 6",
                    "34334", "대주아파트 101동 1103호");

            Member member1 = memberService.signUp(signupMember1);
            log.info("회원가입 멤버={}", member1);

            SignupRequestDto signupMember2 = new SignupRequestDto("hyc48412", passwordAndCheck, "황윤철", "01099694841",
                    "hyc4841@gmail.com", LocalDate.of(1997, 6, 3), Gender.MAN, "서울", "백련산로 6",
                    "34334", "대주아파트 101동 1103호");

            Member member2 = memberService.signUp(signupMember2);
            log.info("회원가입 멤버={}", member2);

            SignupRequestDto signupMember3 = new SignupRequestDto("hyc48413", passwordAndCheck, "황윤철", "01099694841",
                    "hyc4841@nate.com", LocalDate.of(1997, 6, 3), Gender.MAN, "서울", "백련산로 6",
                    "34334", "대주아파트 101동 1103호");

            Member member3 = memberService.signUp(signupMember3);
            log.info("회원가입 멤버={}", member3);
        }


        public void pageInit() {

            Item lapTop1 = itemService.findOne(1L);
            Item lapTop2 = itemService.findOne(2L);
            Item lapTop3 = itemService.findOne(3L);
            Item lapTop4 = itemService.findOne(4L);
            Item lapTop5 = itemService.findOne(5L);

            // 상품 페이지 만드는 조건
            // itemOption만 연결해서 만들기
            List<String> images = new ArrayList<>();
            images.add("테스트 이미지 1");
            // 상품 판매 페이지 생성
            SalesPage salesPage = new SalesPage("상품 테스트 페이지 노트북 판매", images, "이것은 테스트이며 노트북 판매 페이지 입니다.");

            ItemOption topOption = new ItemOption("모델", 0);
            topOption.setSalesPage(salesPage);
            
            ItemOption inch_15 = new ItemOption("15인치", 1);
            inch_15.setParent(topOption);
            inch_15.setSalesPage(salesPage);
            ItemOption inch_16 = new ItemOption("16인치", 2);
            inch_16.setParent(topOption);
            inch_16.setSalesPage(salesPage);
            ItemOption inch_17 = new ItemOption("17인치", 3);
            inch_17.setParent(topOption);
            inch_17.setSalesPage(salesPage);
            ItemOption inch_14 = new ItemOption("14인치", 4);
            inch_14.setParent(topOption);
            inch_14.setSalesPage(salesPage);
            ItemOption inch_13 = new ItemOption("13인치", 5);
            inch_13.setParent(topOption);
            inch_13.setSalesPage(salesPage);

            ItemOption inch_15_color = new ItemOption("색상", 6);// 15인치 1
            inch_15_color.setParent(inch_15);
            inch_15_color.setSalesPage(salesPage);

            ItemOption inch_15_color_black = new ItemOption("블랙", 7);
            inch_15_color_black.setParent(inch_15_color);
            inch_15_color_black.setSalesPage(salesPage);
            inch_15_color_black.setItem(lapTop1);
            inch_15_color_black.setIsMainItem(true);
            
            ItemOption inch_15_color_silver = new ItemOption("실버", 8);
            inch_15_color_silver.setParent(inch_15_color);
            inch_15_color_silver.setSalesPage(salesPage);
            inch_15_color_silver.setItem(lapTop2);

            ItemOption inch_15_color_gray = new ItemOption("그레이", 9);
            inch_15_color_gray.setParent(inch_15_color);
            inch_15_color_gray.setSalesPage(salesPage);
            inch_15_color_gray.setItem(lapTop3);

            ItemOption inch_16_color = new ItemOption("색상", 10); // 16인치 2
            inch_16_color.setParent(inch_16);
            inch_16_color.setSalesPage(salesPage);

            ItemOption inch_16_color_black = new ItemOption("블랙", 11);
            inch_16_color_black.setParent(inch_16_color);
            inch_16_color_black.setSalesPage(salesPage);
            inch_16_color_black.setItem(lapTop1);

            ItemOption inch_16_color_silver = new ItemOption("실버", 12);
            inch_16_color_silver.setParent(inch_16_color);
            inch_16_color_silver.setSalesPage(salesPage);
            inch_16_color_silver.setItem(lapTop2);

            ItemOption inch_16_color_gray = new ItemOption("그레이", 13);
            inch_16_color_gray.setParent(inch_16_color);
            inch_16_color_gray.setSalesPage(salesPage);
            inch_16_color_gray.setItem(lapTop3);

            ItemOption inch_17_color = new ItemOption("색상", 14); // 17인치 3
            inch_17_color.setParent(inch_17);
            inch_17_color.setSalesPage(salesPage);

            ItemOption inch_17_color_black = new ItemOption("블랙", 15);
            inch_17_color_black.setParent(inch_17_color);
            inch_17_color_black.setSalesPage(salesPage);
            inch_17_color_black.setItem(lapTop1);

            ItemOption inch_17_color_silver = new ItemOption("실버", 16);
            inch_17_color_silver.setParent(inch_17_color);
            inch_17_color_silver.setSalesPage(salesPage);
            inch_17_color_silver.setItem(lapTop2);

            ItemOption inch_17_color_gray = new ItemOption("그레이", 17);
            inch_17_color_gray.setParent(inch_17_color);
            inch_17_color_gray.setSalesPage(salesPage);
            inch_17_color_gray.setItem(lapTop3);

            ItemOption inch_14_color = new ItemOption("색상", 18); // 14인치 4
            inch_14_color.setParent(inch_14);
            inch_14_color.setSalesPage(salesPage);

            ItemOption inch_14_color_black = new ItemOption("블랙", 19);
            inch_14_color_black.setParent(inch_14_color);
            inch_14_color_black.setSalesPage(salesPage);
            inch_14_color_black.setItem(lapTop1);

            ItemOption inch_14_color_silver = new ItemOption("실버", 20);
            inch_14_color_silver.setParent(inch_14_color);
            inch_14_color_silver.setSalesPage(salesPage);
            inch_14_color_silver.setItem(lapTop2);

            ItemOption inch_14_color_gray = new ItemOption("그레이", 21);
            inch_14_color_gray.setParent(inch_14_color);
            inch_14_color_gray.setSalesPage(salesPage);
            inch_14_color_gray.setItem(lapTop3);

            ItemOption inch_13_color = new ItemOption("색상", 22); // 13인치 5
            inch_13_color.setParent(inch_13);
            inch_13_color.setSalesPage(salesPage);

            ItemOption inch_13_color_black = new ItemOption("블랙", 23);
            inch_13_color_black.setParent(inch_13_color);
            inch_13_color_black.setSalesPage(salesPage);
            inch_13_color_black.setItem(lapTop1);

            ItemOption inch_13_color_silver = new ItemOption("실버", 24);
            inch_13_color_silver.setParent(inch_13_color);
            inch_13_color_silver.setSalesPage(salesPage);
            inch_13_color_silver.setItem(lapTop4);

            ItemOption inch_13_color_gray = new ItemOption("그레이", 25);
            inch_13_color_gray.setParent(inch_13_color);
            inch_13_color_gray.setSalesPage(salesPage);
            inch_13_color_gray.setItem(lapTop5);

            em.persist(salesPage);
        }

        private void saveCategories(List<String> itemList, String subCategory, Long parentId, String type) {
            Category parent = itemService.findCategoryById(parentId);
            int sequence = 0;
            if (type != null) {
                for (String categoryName : itemList) {
                    Category child = new Category(categoryName, subCategory, type, sequence);
                    parent.addChild(child);
                    em.persist(child);
                    sequence++;
                }
            } else {
                for (String categoryName : itemList) {
                    Category child = new Category(categoryName, subCategory, sequence);
                    parent.addChild(child);
                    em.persist(child);
                    sequence++;
                }
            }

            itemList.clear();
        }

        public void initCategories() {
            // 1차 대분류
            Category ai = new Category("Ai", "Major category");
            Category toy = new Category("가전/TV", "Major category");
            Category pc = new Category("컴퓨터/노트북/조립PC", "Major category");
            Category mobile = new Category("태블릿/모바일/디카", "Major category");
            Category golf = new Category("골프/스포츠", "Major category");
            Category car = new Category("자동차/용품/공구", "Major category");
            Category furniture = new Category("가구/조명", "Major category");
            Category food = new Category("식품/유아/완구", "Major category");
            Category living = new Category("생활/주방/건강", "Major category");
            Category fashion = new Category("패션/잡화/뷰티", "Major category");
            Category animal = new Category("반려동물/취미/사무", "Major category");

            em.persist(ai);
            em.persist(toy);
            em.persist(pc);
            em.persist(mobile);
            em.persist(golf);
            em.persist(car);
            em.persist(furniture);
            em.persist(food);
            em.persist(living);
            em.persist(fashion);
            em.persist(animal);

            // 2차 분류
            List<String> list = new ArrayList<>();

            int sequence = 0;

            // Ai
            list.add("AI 노트북");
            list.add("스마트폰/태블릿");
            list.add("TV");
            list.add("냉장고");
            list.add("세탁기/건조기");
            list.add("청소기");
            list.add("식기세척기/인덕션");
            list.add("에어컨/공기청정기");

            Iterator<String> iterator25 = list.iterator();
            while (iterator25.hasNext()) {
                String categoryName = iterator25.next();
                Category category = new Category(categoryName, "Ai", sequence);
                ai.addChild(category);
                em.persist(category);
                iterator25.remove();
                sequence++;
            }

            // 가전/TV -> 서브 카테고리가 있는 카테고리
            // 영상/음향가전

            Category subcategory1 = new Category("영상/음향가전", null, sequence);
            toy.addChild(subcategory1);
            em.persist(subcategory1);

            list.add("영상가전");
            list.add("음향가전");
            Iterator<String> iterator = list.iterator();
            sequence = 1;
            while (iterator.hasNext()) {
                String categoryName = iterator.next();
                Category category = new Category(categoryName, "영상/음향가전", sequence);
                subcategory1.addChild(category);
                em.persist(category);
                iterator.remove();
                sequence++;
            }
            // tv 따로 저장
            Category tv_2 = new Category("TV", "영상/음향가전", "Tv", 0);
            subcategory1.addChild(tv_2);
            em.persist(tv_2);


            // 생활/계절가전
            Category subcategory2 = new Category("생활/계절가전", null, sequence);
            toy.addChild(subcategory2);
            em.persist(subcategory2);

            // 생활 계절 가전
            list.add("세탁기/건조기");
            list.add("에어컨/계절가전");
            list.add("이미용/소형가전");
            Iterator<String> iterator1 = list.iterator();
            sequence = 1;
            while (iterator1.hasNext()) {
                String categoryName = iterator1.next();
                Category category = new Category(categoryName, "생활/계절가전", sequence);
                subcategory2.addChild(category);
                em.persist(category);
                iterator1.remove();
                sequence++;
            }
            // 청소기 따로 저장
            Category vacuum = new Category("청소기", "생활/계절가전", "VacuumCleaner", 0);
            subcategory2.addChild(vacuum);
            em.persist(vacuum);


            Category subcategory3 = new Category("주방가전", null, sequence);
            toy.addChild(subcategory3);
            em.persist(subcategory3);

            // 주방가전
            list.add("냉장고/김치냉장고");
            list.add("전기밥솥");
            list.add("식기세척/건조기");
            list.add("오븐/레인지/인덕션");
            list.add("정수기/소형가전");
            Iterator<String> iterator2 = list.iterator();
            sequence = 0;
            while (iterator2.hasNext()) {
                String categoryName = iterator2.next();
                Category category = new Category(categoryName, "주방가전", sequence);
                subcategory3.addChild(category);
                em.persist(category);
                iterator2.remove();
                sequence++;
            }

            /// 컴퓨터/노트북/조립pc
            // 노트북/데스크탑
            Category subcategory4 = new Category("노트북/데스크탑", null, sequence);
            pc.addChild(subcategory4);
            em.persist(subcategory4);

            // 컴퓨터/노트북/조립PC
            list.add("노트북");
            list.add("게이밍 노트북");
            list.add("브랜드PC/조립PC");
            list.add("딥러닐(GPU)PC");
            Iterator<String> iterator3 = list.iterator();
            sequence = 0;
            while (iterator3.hasNext()) {
                String categoryName = iterator3.next();
                Category category;
                if (Objects.equals(categoryName, "노트북")) {
                    category = new Category(categoryName, "노트북/데스크탑", "LapTop", sequence);
                } else {
                    category = new Category(categoryName, "노트북/데스크탑", sequence);
                }
                subcategory4.addChild(category);
                em.persist(category);
                iterator3.remove();
                sequence++;
            }

            // 모니터/복합기
            Category subcategory5 = new Category("모니터/복합기", null, sequence);
            pc.addChild(subcategory5);
            em.persist(subcategory5);

            list.add("모니터");
            list.add("게이밍 모니터");
            list.add("복합기/프린터/SW");
            Iterator<String> iterator4 = list.iterator();
            sequence = 0;
            while (iterator4.hasNext()) {
                String categoryName = iterator4.next();
                Category category = new Category(categoryName, "모니터/복합기", sequence);
                subcategory5.addChild(category);
                em.persist(category);
                iterator4.remove();
                sequence++;
            }

            // pc부품
            Category subcategory6 = new Category("pc부품", null, sequence);
            pc.addChild(subcategory6);
            em.persist(subcategory6);

            list.add("주요부품");
            list.add("저장장치");
            list.add("키보드/마우스/웹캠");
            list.add("공유기/주변기기");
            Iterator<String> iterator5 = list.iterator();
            sequence = 0;
            while (iterator5.hasNext()) {
                String categoryName = iterator5.next();
                Category category = new Category(categoryName, "PC부품", sequence);
                subcategory6.addChild(category);
                em.persist(category);
                iterator5.remove();
                sequence++;
            }

            // 게임/사운드
            Category subcategory7 = new Category("게임/사운드", null, sequence);
            pc.addChild(subcategory7);
            em.persist(subcategory7);

            list.add("게임기/게이밍가구");
            list.add("사운드/스피커");
            Iterator<String> iterator6 = list.iterator();
            sequence = 0;
            while (iterator6.hasNext()) {
                String categoryName = iterator6.next();
                Category category = new Category(categoryName, "게임/사운드", sequence);
                subcategory7.addChild(category);
                em.persist(category);
                iterator6.remove();
                sequence++;
            }


            /// 태블릿/모바일/디카 -> mobile
            // 신제품 바로가기
            Category subcategory8 = new Category("신제품 바로가기", null, sequence);
            mobile.addChild(subcategory8);
            em.persist(subcategory8);

            list.add("갤럭시 최신형");
            list.add("애플 최신형");
            Iterator<String> iterator7 = list.iterator();
            sequence = 0;
            while (iterator7.hasNext()) {
                String categoryName = iterator7.next();
                Category category = new Category(categoryName, "신제품 바로가기", sequence);
                subcategory8.addChild(category);
                em.persist(category);
                iterator7.remove();
                sequence++;
            }

            // 태블릿/스마트폰
            Category subcategory9 = new Category("태블릿/스마트폰", null, sequence);
            mobile.addChild(subcategory9);
            em.persist(subcategory9);

            list.add("휴대폰/스마트폰");
            list.add("태블릿/전자책");
            list.add("스마트워치/VR");
            list.add("충전기/보조배터리");
            list.add("케이스/필름/터치펜");
            Iterator<String> iterator8 = list.iterator();
            sequence = 0;
            while (iterator8.hasNext()) {
                String categoryName = iterator8.next();
                Category category = new Category(categoryName, "태블릿/스마트폰", sequence);
                subcategory9.addChild(category);
                em.persist(category);
                iterator8.remove();
                sequence++;
            }

            // 포터블/음향
            Category subcategory10  = new Category("포터블/음향", null, sequence);
            mobile.addChild(subcategory10);
            em.persist(subcategory10);

            list.add("이어폰/헤드폰");
            list.add("블루투스/Ai스피커");
            list.add("휴대용 플레이어");
            Iterator<String> iterator9 = list.iterator();
            sequence = 0;
            while (iterator9.hasNext()) {
                String categoryName = iterator9.next();
                Category category = new Category(categoryName, "포터블/음향", sequence);
                subcategory10.addChild(category);
                em.persist(category);
                iterator9.remove();
                sequence++;
            }

            // 디카
            Category subcategory11  = new Category("디카", null, sequence);
            mobile.addChild(subcategory11);
            em.persist(subcategory11);

            list.add("카메라/렌즈/캠코더");
            list.add("플래시/액세서리");
            list.add("메모리카드/리더기");
            Iterator<String> iterator10 = list.iterator();
            sequence = 0;
            while (iterator10.hasNext()) {
                String categoryName = iterator10.next();
                Category category = new Category(categoryName, "디카", sequence);
                subcategory11.addChild(category);
                em.persist(category);
                iterator10.remove();
                sequence++;
            }


            // 아웃도어/스포츠
            Category subcategory12  = new Category("아웃도어/스포츠", null, sequence);
            golf.addChild(subcategory12);
            em.persist(subcategory12);

            // 골프/스포츠
            list.add("스포츠화");
            list.add("남성스포츠의류");
            list.add("여성스포츠의류");
            list.add("캠핑");
            list.add("등산");
            list.add("자전거/전동킥보드");
            list.add("구기/라켓/격투기");
            list.add("낚시");
            list.add("헬스/홈트레이닝");
            list.add("수영/수상스포츠");
            list.add("스키/겨울스포츠");
            list.add("스포츠잡화");
            list.add("젝시믹스 브랜드관");
            Iterator<String> iterator11 = list.iterator();
            sequence = 0;
            while (iterator11.hasNext()) {
                String categoryName = iterator11.next();
                Category category = new Category(categoryName, "아웃도어/스포츠", sequence);
                subcategory12.addChild(category);
                em.persist(category);
                iterator11.remove();
                sequence++;
            }
            // 골프
            Category subcategory13  = new Category("골프", null, sequence);
            golf.addChild(subcategory13);
            em.persist(subcategory13);

            list.add("라운딩갈때");
            list.add("골프클럽");
            list.add("남성골프패션");
            list.add("여성골프패션");
            list.add("골프용품");
            Iterator<String> iterator12 = list.iterator();
            sequence = 0;
            while (iterator12.hasNext()) {
                String categoryName = iterator12.next();
                Category category = new Category(categoryName, "골프", sequence);
                subcategory13.addChild(category);
                em.persist(category);
                iterator12.remove();
                sequence++;
            }



            // 자동차/용품/공구
            // 자동차용품
            Category subcategory14  = new Category("자동차용품", null, sequence);
            car.addChild(subcategory14);
            em.persist(subcategory14);

            list.add("타이어/휠/배터리");
            list.add("오일/첨가제/필터");
            list.add("블박/충전/전자제품");
            list.add("세차/와이퍼/방향제");
            list.add("부품/외장/안전");
            list.add("매트/시트/인테리어");
            list.add("스쿠터/오토바이");
            Iterator<String> iterator13 = list.iterator();
            sequence = 0;
            while (iterator13.hasNext()) {
                String categoryName = iterator13.next();
                Category category = new Category(categoryName, "자동차용품", sequence);
                subcategory14.addChild(category);
                em.persist(category);
                iterator13.remove();
                sequence++;
            }

            // 공구/산업용품
            Category subcategory15  = new Category("공구/산업용품", null, sequence);
            car.addChild(subcategory15);
            em.persist(subcategory15);

            list.add("전동드릴/드라이버");
            list.add("절삭/연마공구");
            list.add("측정공구/수공구");
            list.add("송풍기/타카/글루건");
            list.add("전기/인테리어/자재");
            list.add("유압/용접/동력공구");
            list.add("안전용품/운반/건설");
            list.add("예초기/원예/농업");
            Iterator<String> iterator14 = list.iterator();
            sequence = 0;
            while (iterator14.hasNext()) {
                String categoryName = iterator14.next();
                Category category = new Category(categoryName, "공구/산업용품", sequence);
                subcategory15.addChild(category);
                em.persist(category);
                iterator14.remove();
                sequence++;
            }


            // 가구/조명 furniture
            // 가구/조명/시공
            Category subcategory16  = new Category("가구/조명/시공", null, sequence);
            furniture.addChild(subcategory16);
            em.persist(subcategory16);

            list.add("침대/매트리스");
            list.add("소파/거실가구");
            list.add("의자");
            list.add("책상/책장");
            list.add("식탁/주방가구");
            list.add("행거/드레스룸");
            list.add("선반/수납가구");
            list.add("LED전구/조명");
            list.add("시공가구/리모델링");
            list.add("침구/커튼/카페트");
            list.add("홈데코/소품");
            Iterator<String> iterator15 = list.iterator();
            sequence = 0;
            while (iterator15.hasNext()) {
                String categoryName = iterator15.next();
                Category category = new Category(categoryName, "가구/조명/시공", sequence);
                subcategory16.addChild(category);
                em.persist(category);
                iterator15.remove();
                sequence++;
            }

            Category subcategory17  = new Category("가구/조명-브랜드관", null, sequence);
            furniture.addChild(subcategory17);
            em.persist(subcategory17);

            list.add("오늘의집");
            list.add("GS SHOP");
            Iterator<String> iterator16 = list.iterator();
            sequence = 0;
            while (iterator16.hasNext()) {
                String categoryName = iterator16.next();
                Category category = new Category(categoryName, "가구/조명-브랜드관", sequence);
                subcategory17.addChild(category);
                em.persist(category);
                iterator16.remove();
                sequence++;
            }


            // 식품/유아/완구 food
            // 식품
            Category subcategory18  = new Category("식품", null, sequence);
            food.addChild(subcategory18);
            em.persist(subcategory18);

            list.add("건강식품/홍삼");
            list.add("헬스/다이어트식품");
            list.add("생수/음료/우유");
            list.add("커피/차");
            list.add("라면/통조림");
            list.add("즉석밥/국/도시락");
            list.add("냉장/냉동/간편요리");
            list.add("쌀/농산물");
            list.add("축산/수산/건어물");
            list.add("조미료/양념/식용유");
            list.add("과자/초콜릿/시리얼");
            Iterator<String> iterator17 = list.iterator();
            sequence = 0;
            while (iterator17.hasNext()) {
                String categoryName = iterator17.next();
                Category category = new Category(categoryName, "식품", sequence);
                subcategory18.addChild(category);
                em.persist(category);
                iterator17.remove();
                sequence++;
            }

            // 유아/완구
            Category subcategory19  = new Category("유아/완구", null, sequence);
            food.addChild(subcategory19);
            em.persist(subcategory19);

            list.add("분유/기저귀/물티슈");
            list.add("완구/로봇/놀이매트");
            list.add("유모차/카시트/외출");
            list.add("젖병/출산/육아용품");
            list.add("유아의류/신발/도서");
            Iterator<String> iterator18 = list.iterator();
            sequence = 0;
            while (iterator18.hasNext()) {
                String categoryName = iterator18.next();
                Category category = new Category(categoryName, "유아/완구", sequence);
                subcategory19.addChild(category);
                em.persist(category);
                iterator18.remove();
                sequence++;
            }

            // 생활/주방/건강 living
            // 생활용품
            Category subcategory20  = new Category("생활용품", null, sequence);
            living.addChild(subcategory20);
            em.persist(subcategory20);

            list.add("세제/섬유유연제");
            list.add("구강/화장지/생리대");
            list.add("건전지/비디오폰");
            list.add("욕실용품/수전");
            list.add("세탁/청소/수납");
            list.add("모기퇴치/제습/탈취");
            sequence = 0;
            Iterator<String> iterator19 = list.iterator();
            while (iterator19.hasNext()) {
                String categoryName = iterator19.next();
                Category category = new Category(categoryName, "생활용품", sequence);
                subcategory20.addChild(category);
                em.persist(category);
                iterator19.remove();
                sequence++;
            }

            // 주방용품
            Category subcategory21  = new Category("주방용품", null, sequence);
            living.addChild(subcategory21);
            em.persist(subcategory21);

            list.add("냄비/팬/조리도구");
            list.add("식기/컵/보관용기");
            list.add("주방잡화/일회용품");
            Iterator<String> iterator20 = list.iterator();
            sequence = 0;
            while (iterator20.hasNext()) {
                String categoryName = iterator20.next();
                Category category = new Category(categoryName, "주방용품", sequence);
                subcategory21.addChild(category);
                em.persist(category);
                iterator20.remove();
                sequence++;
            }

            // 건강/의료용품
            Category subcategory22  = new Category("건강/의료용품", null, sequence);
            living.addChild(subcategory22);
            em.persist(subcategory22);

            list.add("마스크/실버용품");
            list.add("안마/마사지기");
            list.add("건강측정/물리치료");
            Iterator<String> iterator21 = list.iterator();
            sequence = 0;
            while (iterator21.hasNext()) {
                String categoryName = iterator21.next();
                Category category = new Category(categoryName, "건강/의료용품", sequence);
                subcategory22.addChild(category);
                em.persist(category);
                iterator21.remove();
                sequence++;
            }



            // 패션/잡화/뷰티 fashion
            // 패션잡화/의류
            Category subcategory23  = new Category("패션잡화/의류", null, sequence);
            fashion.addChild(subcategory23);
            em.persist(subcategory23);

            list.add("가방/지갑");
            list.add("남성신발");
            list.add("여성신발");
            list.add("시계");
            list.add("순금/쥬얼리");
            list.add("모자/패션잡화");
            list.add("남성의류");
            list.add("여성의류");
            list.add("잠옷/언더웨어");
            list.add("명품관");
            Iterator<String> iterator22 = list.iterator();
            sequence = 0;
            while (iterator22.hasNext()) {
                String categoryName = iterator22.next();
                Category category = new Category(categoryName, "패션잡화/의류", sequence);
                subcategory23.addChild(category);
                em.persist(category);
                iterator22.remove();
                sequence++;
            }

            // 뷰티
            Category subcategory24  = new Category("뷰티", null, sequence);
            fashion.addChild(subcategory24);
            em.persist(subcategory24);

            list.add("남성뷰티");
            list.add("스킨케어");
            list.add("헤어케어");
            list.add("바디케어");
            list.add("향수/메이크업");
            Iterator<String> iterator23 = list.iterator();
            sequence = 0;
            while (iterator23.hasNext()) {
                String categoryName = iterator23.next();
                Category category = new Category(categoryName, "뷰티", sequence);
                subcategory24.addChild(category);
                em.persist(category);
                iterator23.remove();
                sequence++;
            }

            // 패션/잡화/뷰티-브랜드관
            Category subcategory25  = new Category("패션/잡화/뷰티-브랜드관", null, sequence);
            fashion.addChild(subcategory25);
            em.persist(subcategory25);

            list.add("무신사스토어");
            Iterator<String> iterator24 = list.iterator();
            sequence = 0;
            while (iterator24.hasNext()) {
                String categoryName = iterator24.next();
                Category category = new Category(categoryName, "패션/잡화/뷰티-브랜드관", sequence);
                subcategory25.addChild(category);
                em.persist(category);
                iterator24.remove();
                sequence++;
            }

            // 반려동물/취미/사무 animal
            // 반려동물용품
            Category subcategory26  = new Category("반려동물용품", null, sequence);
            animal.addChild(subcategory26);
            em.persist(subcategory26);

            list.add("강아지용품");
            list.add("고양이용품");
            list.add("수족관/소동물용품");
            Iterator<String> iterator30 = list.iterator();
            sequence = 0;
            while (iterator30.hasNext()) {
                String categoryName = iterator30.next();
                Category category = new Category(categoryName, "반려동물용품", sequence);
                subcategory26.addChild(category);
                em.persist(category);
                iterator30.remove();
                sequence++;
            }

            Category subcategory27  = new Category("취미/상품권", null, sequence);
            animal.addChild(subcategory27);
            em.persist(subcategory27);

            list.add("악기/디지털피아노");
            list.add("드론/레고/키덜트");
            list.add("상품권/쿠폰/원예");
            Iterator<String> iterator26 = list.iterator();
            sequence = 0;
            while (iterator26.hasNext()) {
                String categoryName = iterator26.next();
                Category category = new Category(categoryName, "취미/상품권", sequence);
                subcategory27.addChild(category);
                em.persist(category);
                iterator26.remove();
                sequence++;
            }

            Category subcategory28  = new Category("문구/사무용품", null, sequence);
            animal.addChild(subcategory28);
            em.persist(subcategory28);

            list.add("방학 열공템");
            list.add("사무실 소모품");
            list.add("복사용지/사무용지");
            list.add("사무기기/전자칠판");
            list.add("필기/문구용품");
            list.add("화방용품");
            list.add("칠판/사무/포장용품");
            list.add("복합기/프린터/SW");
            list.add("CCTV/보안/금고");
            Iterator<String> iterator27 = list.iterator();
            sequence = 0;
            while (iterator27.hasNext()) {
                String categoryName = iterator27.next();
                Category category = new Category(categoryName, "문구/사무용품", sequence);
                subcategory28.addChild(category);
                em.persist(category);
                iterator27.remove();
                sequence++;
            }

            Category subcategory29  = new Category("도서", null, sequence);
            animal.addChild(subcategory29);
            em.persist(subcategory29);

            list.add("도서");
            Iterator<String> iterator28 = list.iterator();
            sequence = 0;
            while (iterator28.hasNext()) {
                String categoryName = iterator28.next();
                Category category = new Category(categoryName, "도서", sequence);
                subcategory29.addChild(category);
                em.persist(category);
                iterator28.remove();
                sequence++;
            }

            // 3차 분류
            // 가전/TV
            // TV
            list.add("TV전체");
            list.add("2025년형");
            list.add("고화질");
            list.add("TV액세서리");
            list.add("이동형/인테리어TV");

            saveCategories(list, "TV", 23L, "Tv");

            // 노트북
            Category laptop = itemService.findCategoryByNameAndParentName("노트북", "노트북/데스크탑");

            list.add("AI 노트북");
            list.add("게이밍 노트북");
            list.add("APPLE 맥묵");
            list.add("인텔 CPU 노트북");
            list.add("AMD CPU 노트북");
            list.add("주변기기");
            Iterator<String> iterator29 = list.iterator();
            sequence = 1;
            while (iterator29.hasNext()) {
                String categoryName = iterator29.next();
                Category category = new Category(categoryName, sequence);
                laptop.addChild(category);
                em.persist(category);
                iterator29.remove();
                sequence++;
            }

            Category lapTopall = new Category("노트북 전체", null, "LapTop", 0);
            laptop.addChild(lapTopall);
            em.persist(lapTopall);


            // 게이밍 노트북
            Category gamingLapTop = itemService.findCategoryByNameAndParentName("게이밍 노트북", "노트북/데스크탑");

            list.add("RTX4060");
            list.add("RTX4070");
            list.add("배틀그라운드");
            Iterator<String> iterator31 = list.iterator();
            sequence = 0;
            while (iterator31.hasNext()) {
                String categoryName = iterator31.next();
                Category category = new Category(categoryName, sequence);
                gamingLapTop.addChild(category);
                em.persist(category);
                iterator31.remove();
                sequence++;
            }

            Category brandPC = itemService.findCategoryByName("브랜드PC/조립PC");
            list.add("조립PC");
            list.add("브랜드PC");
            list.add("게이밍PC");
            list.add("일체형PC");
            list.add("미니PC/베어본");
            list.add("딥러닝(GPU)PC");
            list.add("서버/워크스테이션");
            Iterator<String> iterator32 = list.iterator();
            sequence = 0;
            while (iterator32.hasNext()) {
                String categoryName = iterator32.next();
                Category category = new Category(categoryName, sequence);
                brandPC.addChild(category);
                em.persist(category);
                iterator32.remove();
                sequence++;
            }


        }


    }
}
