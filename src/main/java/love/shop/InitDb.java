package love.shop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.ItemCategory.ItemCategory;
import love.shop.domain.address.Address;
import love.shop.domain.category.Category;
import love.shop.domain.delivery.Delivery;
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
            Category categoryToy = itemService.findCategoryByName("장난감");
            Category categoryBook = itemService.findCategoryByName("책");

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


        public void initCategories() {
            Category book = new Category("책");
            Category toy = new Category("장난감");
            Category necessaries = new Category("생필품");
            Category electronics = new Category("전자제품");
            Category kitchen = new Category("주방");
            Category stationery = new Category("문구");
            Category appliances = new Category("가전");
            Category computer = new Category("컴퓨터");
            Category earphone = new Category("이어폰");
            Category monitor = new Category("모니터");
            Category fruit = new Category("과일");

            em.persist(book);
            em.persist(toy);
            em.persist(necessaries);
            em.persist(electronics);
            em.persist(kitchen);
            em.persist(stationery);
            em.persist(appliances);
            em.persist(computer);
            em.persist(earphone);
            em.persist(monitor);
            em.persist(fruit);
        }

        private Book createBook(String author, String isbn, String name, int price, int stockQuantity) {
            return new Book(author, isbn, name, price, stockQuantity);
        }

        private Delivery createDelivery(Member member, Address address) {
            Delivery delivery = new Delivery();
            delivery.setCity("서울");
            delivery.setStreet("대주아파트");
            delivery.setZipcode("123123");
            delivery.setDetailedAddress("블라블라");

            return delivery;
        }

    }
}
