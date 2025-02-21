package love.shop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.shop.domain.address.Address;
import love.shop.domain.delivery.Delivery;
import love.shop.domain.item.Book;
import love.shop.domain.member.Gender;
import love.shop.domain.member.Member;
import love.shop.domain.order.Order;
import love.shop.domain.orderItem.OrderItem;
import love.shop.service.member.MemberService;
import love.shop.web.signup.dto.SignupRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService service;


    @PostConstruct
    public void init() {
        service.dbInit1();
        service.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final MemberService memberService;


        public void dbInit1() {
            // 회원가입
            SignupRequestDto signupRequest = new SignupRequestDto("Hell4", "1234", "황윤철", "01099694841",
                    LocalDate.of(1997, 6, 3), Gender.MAN, "서울", "서울시 은평구 백련산로 6 (응암동, 대주피오레아파트)", "33333", "101동 1103호","dbscjf4841@naver.com");
            Long signUpMemberId = memberService.signUp(signupRequest);

            // 아이템, 주문, 배달, 주문아이템
            Member member = memberService.findMemberById(signUpMemberId);

            Book book1 = createBook("JPA1 BOOK", 10000, 100);
            em.persist(book1);
            Book book2 = createBook("JPA2 BOOK", 20000, 100);
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
            SignupRequestDto signupRequest = new SignupRequestDto("Hell5", "1234", "황윤철", "01099694841",
                    LocalDate.of(1997, 6, 3), Gender.MAN, "서울", "서울시 은평구 백련산로 6 (응암동, 대주피오레아파트)", "33333", "101동 1103호","dbscjf4841@naver.com");
            Long signUpMemberId = memberService.signUp(signupRequest);

            // 아이템, 주문, 배달, 주문아이템
            Member member = memberService.findMemberById(signUpMemberId);

            Book book1 = createBook("JPA2 BOOK", 10000, 100);
            em.persist(book1);
            Book book2 = createBook("JPA3 BOOK", 20000, 100);
            em.persist(book2);
            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Address address = new Address("서울", "백련산로 6 대주피오레아파트", "33433", "101동 1103호", member);

            Delivery delivery = createDelivery(member, address);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);

            em.persist(order);
        }

        private Book createBook(String bookName, int price, int quantity) {
            return new Book(bookName, price, quantity);
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
