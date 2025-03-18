package com.ola.repository;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ola.entity.Basket;
import com.ola.entity.Community;
import com.ola.entity.Member;
import com.ola.entity.OrderList;
import com.ola.entity.Product;
import com.ola.entity.Role;
import com.ola.entity.TradeBoard;

@SpringBootTest
public class RepositoryTest {

	@Autowired
	private MemberRepository memberRepo;

	@Autowired
	private CommunityRepository commuRepo;

	@Autowired
	private TradeBoardRepository tradeRepo;

	@Autowired
	private ProductRepository prodRepo;

	@Autowired
	private BasketRepository basketRepo;

	@Autowired
	private OrderListRepository orderRepo;

	@Autowired
	private PasswordEncoder encoder;

//	@Test
	public void member2Insert() {
		Member member = Member.builder().name("안중근").email("jgan@eamil.com").phoneNumber("010-2222-2222")
				.address("서울시 광진구").detailedAddress("건대2번 출구").role(Role.ROLE_MEMBER).memberId("member2")
				.password(encoder.encode("2222")).zipNum("15314").build();
		memberRepo.save(member);
		Member member2 = Member.builder().name("강감찬").email("jgan@eamil.com").phoneNumber("010-2222-2222")
				.address("서울시 광진구").detailedAddress("건대2번 출구").role(Role.ROLE_MEMBER).memberId("member3")
				.password(encoder.encode("3333")).zipNum("15314").build();
		memberRepo.save(member2);
		
	}

	@Test
	public void testAdminInsert() {
		Member member = Member.builder().name("홍길동").email("gdhong@email.com").phoneNumber("010-1111-1111")
				.address("서울시 신림동").detailedAddress("자이아파트 101동 101호").role(Role.ROLE_ADMIN).memberId("admin")
				.password(encoder.encode("1111")).zipNum("14178").build();

		memberRepo.save(member);

		Member member1 = Member.builder().name("이순신").email("sslee@email.com").phoneNumber("010-1111-1111")
				.address("서울시 강남구").detailedAddress("롯데캐슬 101동 101호").role(Role.ROLE_MEMBER).memberId("member")
				.password(encoder.encode("1111")).zipNum("14178").build();

		memberRepo.save(member1);

	}

	@Test
	public void testCommuBoard() {

		Member member = memberRepo.findById("member").get();
		IntStream.rangeClosed(1, 30).forEach(i -> {
			Community comm = Community.builder().commentCount(0).content("게시글입니다." + i).likeCount(0).member(member)
					.regDate(new Date()).viewCount(0).title("테스트 게시글" + i).build();

			commuRepo.save(comm);
		});
	}

	@Test
	public void testTradeBoard() {

		Member member = memberRepo.findById("member").get();
		IntStream.rangeClosed(1, 30).forEach(i -> {
			TradeBoard comm = TradeBoard.builder().content("신발사요." + i).tradeType(3).member(member)
					.registrationDate(new Date()).title("신발 삽니다." + i).build();

			tradeRepo.save(comm);
		});
	}

	@Test
	public void testProduct() {
		IntStream.rangeClosed(1, 14).forEach(i -> {
			Product product = Product.builder().productName("top" + i).prodCategory(1).price(10000L).prodSize("L")
					.salesQuantity(0L).inventory(1000).image("top" + i + ".jpg").build();
			prodRepo.save(product);
		});

		IntStream.rangeClosed(1, 14).forEach(i -> {
			Product product1 = Product.builder().productName("bottom" + i).prodCategory(2).price(10000L).prodSize("L")
					.salesQuantity(0L).inventory(1000).image("bottom" + i + ".jpg").build();
			prodRepo.save(product1);
		});

		IntStream.rangeClosed(1, 15).forEach(i -> {
			Product product3 = Product.builder().productName("shoes" + i).prodCategory(3).price(25000L).prodSize("265")
					.salesQuantity(0L).inventory(1000).image("shoes" + i + ".jpg").build();
			prodRepo.save(product3);
		});

		IntStream.rangeClosed(1, 15).forEach(i -> {
			Product product4 = Product.builder().productName("etc" + i).prodCategory(4).price(15000L).prodSize("f")
					.salesQuantity(0L).inventory(1000).image("etc" + i + ".jpg").build();
			prodRepo.save(product4);
		});
	}

//	@Test
	public void testBasket() {

		Product product = prodRepo.findById(1L).get();
		Product product2 = prodRepo.findById(2L).get();

		Member member = memberRepo.findById("member").get();

		Basket basket = Basket.builder().member(member).build();

		basket.getProducts().add(product); // 상품과 수량을 추가합니다
		basket.getProducts().add(product2); // 상품과 수량을 추가합니다
		basket.addProduct(product, 1); // 상품과 수량을 추가합니다
		basket.addProduct(product2, 3); // 상품과 수량을 추가합니다

		basketRepo.save(basket);

	}

	@Test
	public void testOrderList() {
		Member member = memberRepo.findById("member").get();

		IntStream.rangeClosed(1, 30).forEach(i -> {

			Map<Long, Integer> productQuantities = new HashMap<>();
			productQuantities.put(1L, 2); // Assume product number 1 with quantity 2
			productQuantities.put(2L, 3); // Assume product number 2 with quantity 3

			OrderList orderList = OrderList.builder().member(member).orderDate(new Date()) // Set current date for order
																							// date
					.productQuantities(productQuantities).build();

			orderRepo.save(orderList);
		});


		// 3개월 전 날짜 계산
		Calendar cal3MonthsAgo = Calendar.getInstance();
		cal3MonthsAgo.add(Calendar.MONTH, -3);
		Date threeMonthsAgo = cal3MonthsAgo.getTime();

		// 6개월 전 날짜 계산
		Calendar cal6MonthsAgo = Calendar.getInstance();
		cal6MonthsAgo.add(Calendar.MONTH, -6);
		Date sixMonthsAgo = cal6MonthsAgo.getTime();

		// 1년 전 날짜 계산
		Calendar cal1YearAgo = Calendar.getInstance();
		cal1YearAgo.add(Calendar.YEAR, -1);
		Date oneYearAgo = cal1YearAgo.getTime();

		// 3개월 전 주문 생성 (10개)
		IntStream.rangeClosed(1, 10).forEach(i -> {
			Map<Long, Integer> productQuantities = new HashMap<>();
			productQuantities.put(1L, 2); // Assume product number 1 with quantity 2
			productQuantities.put(2L, 3); // Assume product number 2 with quantity 3

			OrderList orderList = OrderList.builder().member(member).orderDate(threeMonthsAgo)
					.productQuantities(productQuantities).build();

			orderRepo.save(orderList);
		});

		// 6개월 전 주문 생성 (10개)
		IntStream.rangeClosed(1, 10).forEach(i -> {
			Map<Long, Integer> productQuantities = new HashMap<>();
			productQuantities.put(1L, 2); // Assume product number 1 with quantity 2
			productQuantities.put(2L, 3); // Assume product number 2 with quantity 3

			OrderList orderList = OrderList.builder().member(member).orderDate(sixMonthsAgo)
					.productQuantities(productQuantities).build();

			orderRepo.save(orderList);
		});

		// 1년 전 주문 생성 (10개)
		IntStream.rangeClosed(1, 10).forEach(i -> {
			Map<Long, Integer> productQuantities = new HashMap<>();
			productQuantities.put(1L, 2); // Assume product number 1 with quantity 2
			productQuantities.put(2L, 3); // Assume product number 2 with quantity 3

			OrderList orderList = OrderList.builder().member(member).orderDate(oneYearAgo)
					.productQuantities(productQuantities).build();

			orderRepo.save(orderList);
		});

		// 2달 전 날짜 계산
		Calendar cal2MonthsAgo = Calendar.getInstance();
		cal2MonthsAgo.add(Calendar.MONTH, -2);
		Date twoMonthsAgo = cal2MonthsAgo.getTime();

		// 2달 전 주문 생성 (2개, 제품 번호 3L과 4L 주문)
		IntStream.rangeClosed(1, 2).forEach(i -> {
			Map<Long, Integer> productQuantities = new HashMap<>();
			productQuantities.put(3L, 1); // Assume product number 3L with quantity 1
			productQuantities.put(4L, 2); // Assume product number 4L with quantity 2

			OrderList orderList = OrderList.builder().member(member).orderDate(twoMonthsAgo)
					.productQuantities(productQuantities).build();

			orderRepo.save(orderList);
		});
	}

}
