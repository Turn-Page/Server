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

import static com.example.turnpage.global.error.code.OrderErrorCode.NOT_ENOUGH_POINT_TO_ORDER;
import static com.example.turnpage.global.error.code.OrderErrorCode.SALEPOST_IS_ALREADY_SOLD;

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
     * createOrder()의 로직 순서
     * - 해당 판매 게시글이 유효한지 확인
     * - 해당 판매 게시글이 '판매 중'인지 확인
     * - 해당 판매 게시글의 상품 금액보다 많은 포인트를 회원이 가지고 있는지 확인
     * - 주문 엔티티 생성
     * - {회원 보유 포인트 - 상품 금액 연산}을 진행해 회원 보유 포인트 update
     * - 판매 게시글 판매 완료로 상태 update
     * @param member
     * @param request
     * @return
     */
    @Transactional
    @Override
    public SimpleOrderInfo createOrder(Member member, CreateOrderRequest request) {
        Member persistedMember = memberService.findMember(member.getId());
        SalePost salePost = salePostService.findSalePost(request.getSalePostId());

        validateSalePostIsNotSold(salePost);
        validateMemberHasPointMoreThanPaymentAmount(persistedMember.getPoint(), salePost.getPrice());

        String orderNumber = generateOrderNumber(orderNumberSequence.getAndIncrement());
        Order order = orderConverter.toEntity(persistedMember, salePost, orderNumber);
        orderRepository.save(order);

        salePost.setSold();
        persistedMember.minusPoint(salePost.getPrice());


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

    private void validateMemberHasPointMoreThanPaymentAmount(int memberPoint, int paymentAmount) {
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
