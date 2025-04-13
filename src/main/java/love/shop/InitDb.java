package love.shop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.address.Address;
import love.shop.domain.category.Category;
import love.shop.domain.delivery.Delivery;
import love.shop.domain.delivery.DeliveryStatus;
import love.shop.domain.item.Item;
import love.shop.domain.item.type.Book;
import love.shop.domain.member.Gender;
import love.shop.domain.member.Member;
import love.shop.domain.member.PasswordAndCheck;
import love.shop.domain.salesPage.SalesPage;
import love.shop.service.item.ItemService;
import love.shop.service.member.MemberService;
import love.shop.service.page.PageService;
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
            // 먼저 페이지를 만들자
            // 페이지 1
            List<String> images = new ArrayList<>();
            SalesPage page = new SalesPage("테스트 아이템 페이지", images, "이것은 테스트 입니다.");

            List<Item> itemList = new ArrayList<>();

            Item book = itemService.findOne(1L);
            Item laptop = itemService.findOne(6L);

            itemList.add(book);
            itemList.add(laptop);

            pageService.savePage(page, itemList);

            // 페이지 2
            List<String> images2 = new ArrayList<>();
            SalesPage page2 = new SalesPage("테스트 아이템 페이지2", images2, "이것은 테스트 입니다2.");

            List<Item> itemList2 = new ArrayList<>();

            Item book2 = itemService.findOne(1L);
            Item laptop2 = itemService.findOne(6L);

            itemList2.add(book2);
            itemList2.add(laptop2);

            pageService.savePage(page2, itemList2);
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
            Category tv_2 = new Category("TV", "영상/음향가전", "TV", 0);
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

            saveCategories(list, "TV", 23L, "TV");

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
