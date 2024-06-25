package com.example.turnpage.domain.order.service;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.service.MemberService;
import com.example.turnpage.domain.order.converter.OrderConverter;
import com.example.turnpage.domain.order.dto.OrderRequest.CreateOrderRequest;
import com.example.turnpage.domain.order.dto.OrderResponse.PagedOrderInfo;
import com.example.turnpage.domain.order.dto.OrderResponse.SimpleOrderInfo;
import com.example.turnpage.domain.order.entity.Order;
import com.example.turnpage.domain.order.repository.OrderRepository;
import com.example.turnpage.domain.salePost.entity.SalePost;
import com.example.turnpage.domain.salePost.service.SalePostService;
import com.example.turnpage.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.turnpage.global.error.code.OrderErrorCode.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final MemberService memberService;
    private final OrderRepository orderRepository;
    private final SalePostService salePostService;
    private final OrderConverter orderConverter;
    public final AtomicInteger orderNumberSequence = new AtomicInteger(1);

    /**
     * createOrder()의 로직
     * - 해당 판매 게시글이 유효한지 확인
     * - 해당 판매 게시글이 '판매 중'인지 확인
     * - 해당 판매 게시글의 상품 금액보다 많은 포인트를 회원이 가지고 있는지 확인
     * - 현재 주문 번호 시퀀스와 현재 시간을 바탕으로 주문 번호 생성
     * - 주문 엔티티 생성
     * - 판매 게시글 판매 완료로 상태 update
     * - {회원 보유 포인트 - 상품 금액 연산}을 진행해 회원 보유 포인트 update
     * - 판매자 회원의 포인트를 상품 금액만큼 increase
     *
     * @param member salePost를 구매하고자 하는 회원
     * @param request
     * @return
     */
    @Transactional
    @Override
    public SimpleOrderInfo createOrder(Member member, CreateOrderRequest request) {
        // 이후 회원의 포인트 변경사항이 DB에 반영되도록 하기 위해, 영속성 컨텍스트에 관리되도록 설정
        Member persistedMember = memberService.findMember(member.getId());
        SalePost salePost = salePostService.findSalePost(request.getSalePostId());
        final Member seller = salePost.getMember();

        validateSalePostIsNotSold(salePost);
        validateSalePostIsNotMine(seller, persistedMember);
        final int paymentAmount = salePost.getPrice();
        validateSufficientPoints(persistedMember.getPoint(), paymentAmount);

        String orderNumber = generateOrderNumber(orderNumberSequence.getAndIncrement());
        Order order = orderConverter.toEntity(persistedMember, salePost, orderNumber);
        orderRepository.save(order);

        salePost.setSold();
        persistedMember.subtractPoint(paymentAmount);
        seller.addPoint(paymentAmount);

        persistedMember.incrementPurchaseCount();
        seller.incrementSaleCount();

        return orderConverter.toSimpleOrderInfo(order);
    }

    @Override
    public PagedOrderInfo findMyOrderList(Member member, Pageable pageable) {
        Page<Order> orders = orderRepository.findByMemberId(member.getId(), pageable);

        return orderConverter.toPagedOrderInfo(orders);
    }

    @Override
    public void resetOrderNumberSequence() {
        this.orderNumberSequence.set(1);
    }

    private void validateSalePostIsNotSold(SalePost salePost) {
        if (salePost.isSold()) {
            throw new BusinessException(SALEPOST_IS_ALREADY_SOLD);
        }
    }

    private void validateSalePostIsNotMine(Member seller, Member buyer) {
        if (seller.getId().equals(buyer.getId())) {
            throw new BusinessException(CANNOT_ORDER_MY_SALEPOST);
        }
    }

    private void validateSufficientPoints(int memberPoint, int paymentAmount) {
        if (memberPoint < paymentAmount) {
            throw new BusinessException(NOT_ENOUGH_POINT_TO_ORDER);
        }
    }

    private String generateOrderNumber(int orderNumberSequence) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        return String.format("%s%04d", formattedDateTime, orderNumberSequence);
    }
}
