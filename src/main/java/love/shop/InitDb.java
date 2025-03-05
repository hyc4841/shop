package love.shop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.ItemCategory.ItemCategory;
import love.shop.domain.address.Address;
import love.shop.domain.category.Category;
import love.shop.domain.delivery.Delivery;
import love.shop.domain.delivery.DeliveryStatus;
import love.shop.domain.item.Book;
import love.shop.domain.member.Gender;
import love.shop.domain.member.Member;
import love.shop.domain.member.PasswordAndCheck;
import love.shop.domain.order.Order;
import love.shop.domain.orderItem.OrderItem;
import love.shop.service.item.ItemService;
import love.shop.service.member.MemberService;
import love.shop.web.signup.dto.SignupRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService service;


    @PostConstruct
    public void init() {
        service.initCategories();
        service.dbInit1();
        service.dbInit2();
        service.initItem();

    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final MemberService memberService;
        private final ItemService itemService;


        public void dbInit1() {

            PasswordAndCheck passwordAndCheck = new PasswordAndCheck("1234", "1234");

            // 회원가입
            SignupRequestDto signupRequest = new SignupRequestDto("Hell4", passwordAndCheck,"황윤철", "01099694841",
                    "dbscjf4841@naver.com", LocalDate.of(1997, 6, 3), Gender.MAN, "서울", "서울시 은평구 백련산로 6 (응암동, 대주피오레아파트)", "33333", "101동 1103호");

            Long signUpMemberId = memberService.signUp(signupRequest);

            // 아이템, 주문, 배달, 주문아이템
            Member member = memberService.findMemberById(signUpMemberId);

            // 1. 카테고리 생성
            Category categoryToy = itemService.findCategoryByName("반려동물/취미/사무");
            Category categoryBook = itemService.findCategoryByName("식품/유아/완구");

            // 2. 아이템 카테고리 생성(카테고리만 넣은 아이템-카테고리)
            ItemCategory itemCategoryBook = ItemCategory.createItemCategory(categoryBook);
            ItemCategory itemCategoryToy = ItemCategory.createItemCategory(categoryToy);

            // 3. 아이템 생성(아이템-카테고리를 넣어서 완성 시킨다.)
            Book book = Book.createBook("저자1", "41541", "JPA1 BOOK", 10000, 100, itemCategoryBook, itemCategoryToy);
            em.persist(book);

            // 어떻게 만들든 연관관계 이어주고 저장만 제대로 하면 되는듯.
            Book book1 = createBook("저자1", "41541", "JPA1 BOOK", 10000, 100);
            em.persist(book1);
            Book book2 = createBook("저자2", "5461654", "JPA4 BOOK", 20000, 200);
            em.persist(book2);
            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);
            Address address = new Address("서울", "백련산로 6 대주피오레아파트", "33433", "101동 1103호", member);

            Delivery delivery = createDelivery(member, address);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);

            em.persist(order);

        }

        public void dbInit2() {
            // 회원가입
            PasswordAndCheck passwordAndCheck = new PasswordAndCheck("1234", "1234");
            SignupRequestDto signupRequest = new SignupRequestDto("Hell5", passwordAndCheck,"가나다", "01099694841",
                    "dbscjf4841@naver.com", LocalDate.of(1997, 6, 3), Gender.MAN, "서울", "서울시 은평구 백련산로 6 (응암동, 대주피오레아파트)", "33333", "101동 1103호");
            Long signUpMemberId = memberService.signUp(signupRequest);

            // 아이템, 주문, 배달, 주문아이템
            Member member = memberService.findMemberById(signUpMemberId);

            Book book1 = createBook("저자1", "41541", "JPA2 BOOK", 10000, 100);
            em.persist(book1);
            Book book2 = createBook("저자2", "5461654", "JPA3 BOOK", 20000, 200);
            em.persist(book2);

            // 아이템에 카테고리 설정해주는건 저장할 때 해주면 제일 베스트인듯?


            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Address address = new Address("서울", "백련산로 6 대주피오레아파트", "33433", "101동 1103호", member);

            Delivery delivery = createDelivery(member, address);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);

            em.persist(order);
        }

        public void initItem() {


        }

        public void initCategories() {
            // 1차 대분류
            Category ai = new Category("Ai");
            Category toy = new Category("가전/TV");
            Category pc = new Category("컴퓨터/노트북/조립PC");
            Category mobile = new Category("태블릿/모바일/디카");
            Category golf = new Category("골프/스포츠");
            Category car = new Category("자동차/용품/공구");
            Category furniture = new Category("가구/조명");
            Category food = new Category("식품/유아/완구");
            Category living = new Category("생활/주방/건강");
            Category fashion = new Category("패션/잡화/뷰티");
            Category animal = new Category("반려동물/취미/사무");

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
                Category category = new Category(categoryName, "Ai");
                ai.addChild(category);
                em.persist(category);
                iterator25.remove();
            }

            // 가전/TV
            list.add("TV");
            list.add("영상가전");
            list.add("음향가전");
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                String categoryName = iterator.next();
                Category category = new Category(categoryName, "영상/음향가전");
                toy.addChild(category);
                em.persist(category);
                iterator.remove();
            }

            list.add("세탁기/건조기");
            list.add("청소기");
            list.add("에어컨/계절가전");
            list.add("이미용/소형가전");
            Iterator<String> iterator1 = list.iterator();
            while (iterator1.hasNext()) {
                String categoryName = iterator1.next();
                Category category = new Category(categoryName, "생활/계절가전");
                toy.addChild(category);
                em.persist(category);
                iterator1.remove();
            }

            list.add("냉장고/김치냉장고");
            list.add("전기밥솥");
            list.add("식기세척/건조기");
            list.add("오븐/레인지/인덕션");
            list.add("정수기/소형가전");
            Iterator<String> iterator2 = list.iterator();
            while (iterator2.hasNext()) {
                String categoryName = iterator2.next();
                Category category = new Category(categoryName, "주방가전");
                toy.addChild(category);
                em.persist(category);
                iterator2.remove();
            }

            // 컴퓨터/노트북/조립PC
            list.add("노트북");
            list.add("게이밍 노트북");
            list.add("브랜드PC/조립PC");
            list.add("딥러닐(GPU)PC");
            Iterator<String> iterator3 = list.iterator();
            while (iterator3.hasNext()) {
                String categoryName = iterator3.next();
                Category category = new Category(categoryName, "노트북/데스크탑");
                pc.addChild(category);
                em.persist(category);
                iterator3.remove();
            }

            list.add("모니터");
            list.add("게이밍 모니터");
            list.add("복합기/프린터/SW");
            Iterator<String> iterator4 = list.iterator();
            while (iterator4.hasNext()) {
                String categoryName = iterator4.next();
                Category category = new Category(categoryName, "모니터/복합기");
                pc.addChild(category);
                em.persist(category);
                iterator4.remove();
            }

            list.add("주요부품");
            list.add("저장장치");
            list.add("키보드/마우스/웹캠");
            list.add("공유기/주변기기");
            Iterator<String> iterator5 = list.iterator();
            while (iterator5.hasNext()) {
                String categoryName = iterator5.next();
                Category category = new Category(categoryName, "PC부품");
                pc.addChild(category);
                em.persist(category);
                iterator5.remove();
            }

            list.add("게임기/게이밍가구");
            list.add("사운드/스피커");
            Iterator<String> iterator6 = list.iterator();
            while (iterator6.hasNext()) {
                String categoryName = iterator6.next();
                Category category = new Category(categoryName, "게임/사운드");
                pc.addChild(category);
                em.persist(category);
                iterator6.remove();
            }

            // 태블릿/모바일/디카
            list.add("갤럭시 최신형");
            list.add("애플 최신형");
            Iterator<String> iterator7 = list.iterator();
            while (iterator7.hasNext()) {
                String categoryName = iterator7.next();
                Category category = new Category(categoryName, "신제품 바로가기");
                mobile.addChild(category);
                em.persist(category);
                iterator7.remove();
            }

            list.add("휴대폰/스마트폰");
            list.add("태블릿/전자책");
            list.add("스마트워치/VR");
            list.add("충전기/보조배터리");
            list.add("케이스/필름/터치펜");
            Iterator<String> iterator8 = list.iterator();
            while (iterator8.hasNext()) {
                String categoryName = iterator8.next();
                Category category = new Category(categoryName, "태블릿/스마트폰");
                mobile.addChild(category);
                em.persist(category);
                iterator8.remove();
            }

            list.add("이어폰/헤드폰");
            list.add("블루투스/Ai스피커");
            list.add("휴대용 플레이어");
            Iterator<String> iterator9 = list.iterator();
            while (iterator9.hasNext()) {
                String categoryName = iterator9.next();
                Category category = new Category(categoryName, "포터블 음향");
                mobile.addChild(category);
                em.persist(category);
                iterator9.remove();
            }

            list.add("카메라/렌즈/캠코더");
            list.add("플래시/액세서리");
            list.add("메모리카드/리더기");
            Iterator<String> iterator10 = list.iterator();
            while (iterator10.hasNext()) {
                String categoryName = iterator10.next();
                Category category = new Category(categoryName, "디카");
                mobile.addChild(category);
                em.persist(category);
                iterator10.remove();
            }

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
            while (iterator11.hasNext()) {
                String categoryName = iterator11.next();
                Category category = new Category(categoryName, "아웃도어/스포츠");
                golf.addChild(category);
                em.persist(category);
                iterator11.remove();
            }

            list.add("라운딩갈때");
            list.add("골프클럽");
            list.add("남성골프패션");
            list.add("여성골프패션");
            list.add("골프용품");
            Iterator<String> iterator12 = list.iterator();
            while (iterator12.hasNext()) {
                String categoryName = iterator12.next();
                Category category = new Category(categoryName, "골프");
                golf.addChild(category);
                em.persist(category);
                iterator12.remove();
            }

            // 자동차/용품/공구
            list.add("타이어/휠/배터리");
            list.add("오일/첨가제/필터");
            list.add("블박/충전/전자제품");
            list.add("세차/와이퍼/방향제");
            list.add("부품/외장/안전");
            list.add("매트/시트/인테리어");
            list.add("스쿠터/오토바이");
            Iterator<String> iterator13 = list.iterator();
            while (iterator13.hasNext()) {
                String categoryName = iterator13.next();
                Category category = new Category(categoryName, "자동차용품");
                car.addChild(category);
                em.persist(category);
                iterator13.remove();
            }

            list.add("전동드릴/드라이버");
            list.add("절삭/연마공구");
            list.add("측정공구/수공구");
            list.add("송풍기/타카/글루건");
            list.add("전기/인테리어/자재");
            list.add("유압/용접/동력공구");
            list.add("안전용품/운반/건설");
            list.add("예초기/원예/농업");
            Iterator<String> iterator14 = list.iterator();
            while (iterator14.hasNext()) {
                String categoryName = iterator14.next();
                Category category = new Category(categoryName, "공구/산업용품");
                car.addChild(category);
                em.persist(category);
                iterator14.remove();
            }

            // 가구/조명
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
            while (iterator15.hasNext()) {
                String categoryName = iterator15.next();
                Category category = new Category(categoryName, "가구/조명/시공");
                furniture.addChild(category);
                em.persist(category);
                iterator15.remove();
            }

            list.add("오늘의집");
            list.add("GS SHOP");
            Iterator<String> iterator16 = list.iterator();
            while (iterator16.hasNext()) {
                String categoryName = iterator16.next();
                Category category = new Category(categoryName, "가구/조명-브랜드관");
                furniture.addChild(category);
                em.persist(category);
                iterator16.remove();
            }

            // 식품/유아/완구
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
            while (iterator17.hasNext()) {
                String categoryName = iterator17.next();
                Category category = new Category(categoryName, "식품");
                food.addChild(category);
                em.persist(category);
                iterator17.remove();
            }

            list.add("분유/기저귀/물티슈");
            list.add("완구/로봇/놀이매트");
            list.add("유모차/카시트/외출");
            list.add("젖병/출산/육아용품");
            list.add("유아의류/신발/도서");
            Iterator<String> iterator18 = list.iterator();
            while (iterator18.hasNext()) {
                String categoryName = iterator18.next();
                Category category = new Category(categoryName, "유아/완구");
                food.addChild(category);
                em.persist(category);
                iterator18.remove();
            }

            // 생활/주방/건강
            list.add("세제/섬유유연제");
            list.add("구강/화장지/생리대");
            list.add("건전지/비디오폰");
            list.add("욕실용품/수전");
            list.add("세탁/청소/수납");
            list.add("모기퇴치/제습/탈취");
            Iterator<String> iterator19 = list.iterator();
            while (iterator19.hasNext()) {
                String categoryName = iterator19.next();
                Category category = new Category(categoryName, "생활용품");
                living.addChild(category);
                em.persist(category);
                iterator19.remove();
            }

            list.add("냄비/팬/조리도구");
            list.add("식기/컵/보관용기");
            list.add("주방잡화/일회용품");
            Iterator<String> iterator20 = list.iterator();
            while (iterator20.hasNext()) {
                String categoryName = iterator20.next();
                Category category = new Category(categoryName, "주방용품");
                living.addChild(category);
                em.persist(category);
                iterator20.remove();
            }

            list.add("마스크/실버용품");
            list.add("안마/마사지기");
            list.add("건강측정/물리치료");
            Iterator<String> iterator21 = list.iterator();
            while (iterator21.hasNext()) {
                String categoryName = iterator21.next();
                Category category = new Category(categoryName, "건강/의료용품");
                living.addChild(category);
                em.persist(category);
                iterator21.remove();
            }

            // 패션/잡화/뷰티
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
            while (iterator22.hasNext()) {
                String categoryName = iterator22.next();
                Category category = new Category(categoryName, "패션잡화/의류");
                fashion.addChild(category);
                em.persist(category);
                iterator22.remove();
            }

            list.add("남성뷰티");
            list.add("스킨케어");
            list.add("헤어케어");
            list.add("바디케어");
            list.add("향수/메이크업");
            Iterator<String> iterator23 = list.iterator();
            while (iterator23.hasNext()) {
                String categoryName = iterator23.next();
                Category category = new Category(categoryName, "뷰티");
                fashion.addChild(category);
                em.persist(category);
                iterator23.remove();
            }

            list.add("무신사스토어");
            Iterator<String> iterator24 = list.iterator();
            while (iterator24.hasNext()) {
                String categoryName = iterator24.next();
                Category category = new Category(categoryName, "패션/잡화/뷰티-브랜드관");
                fashion.addChild(category);
                em.persist(category);
                iterator24.remove();
            }

            // 반려동물/취미/사무
            list.add("강아지용품");
            list.add("고양이용품");
            list.add("수족관/소동물용품");
            Iterator<String> iterator30 = list.iterator();
            while (iterator30.hasNext()) {
                String categoryName = iterator30.next();
                Category category = new Category(categoryName, "반려동물용품");
                animal.addChild(category);
                em.persist(category);
                iterator30.remove();
            }

            list.add("악기/디지털피아노");
            list.add("드론/레고/키덜트");
            list.add("상품권/쿠폰/원예");
            Iterator<String> iterator26 = list.iterator();
            while (iterator26.hasNext()) {
                String categoryName = iterator26.next();
                Category category = new Category(categoryName, "취미/상품권");
                animal.addChild(category);
                em.persist(category);
                iterator26.remove();
            }

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
            while (iterator27.hasNext()) {
                String categoryName = iterator27.next();
                Category category = new Category(categoryName, "문구/사무용품");
                animal.addChild(category);
                em.persist(category);
                iterator27.remove();
            }

            list.add("도서");
            Iterator<String> iterator28 = list.iterator();
            while (iterator28.hasNext()) {
                String categoryName = iterator28.next();
                Category category = new Category(categoryName, "도서");
                animal.addChild(category);
                em.persist(category);
                iterator28.remove();
            }

            // 3차 분류
            // 노트북
            Category laptop = itemService.findCategoryByName("노트북");
            list.add("AI 노트북");
            list.add("게이밍 노트북");
            list.add("APPLE 맥묵");
            list.add("인텔 CPU 노트북");
            list.add("AMD CPU 노트북");
            list.add("주변기기");
            Iterator<String> iterator29 = list.iterator();
            while (iterator29.hasNext()) {
                String categoryName = iterator29.next();
                Category category = new Category(categoryName);
                laptop.addChild(category);
                em.persist(category);
                iterator29.remove();
            }

            // 게이밍 노트북
            Category gamingLapTop = itemService.findCategoryByNameAndParentName("게이밍 노트북", "노트북");
            log.info("카테고리 이름은 게이밍 노트북, 부모 이름은 노트북={}", gamingLapTop.getId());

            list.add("RTX4060");
            list.add("RTX4070");
            list.add("배틀그라운드");
            Iterator<String> iterator31 = list.iterator();
            while (iterator31.hasNext()) {
                String categoryName = iterator31.next();
                Category category = new Category(categoryName);
                gamingLapTop.addChild(category);
                em.persist(category);
                iterator31.remove();
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
            while (iterator32.hasNext()) {
                String categoryName = iterator32.next();
                Category category = new Category(categoryName);
                brandPC.addChild(category);
                em.persist(category);
                iterator32.remove();
            }














        }

        private Book createBook(String author, String isbn, String name, int price, int stockQuantity) {
            return new Book(author, isbn, name, price, stockQuantity);
        }

        private Delivery createDelivery(Member member, Address address) {

            return new Delivery(address, DeliveryStatus.PENDING);
        }

    }
}
