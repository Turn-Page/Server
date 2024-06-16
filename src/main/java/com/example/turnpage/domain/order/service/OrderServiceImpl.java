package com.example.turnpage.domain.order.service;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.service.MemberService;
import com.example.turnpage.domain.order.converter.OrderConverter;
import com.example.turnpage.domain.order.dto.OrderRequest.CreateOrderRequest;
import com.example.turnpage.domain.order.dto.OrderResponse.SimpleOrderInfo;
import com.example.turnpage.domain.order.entity.Order;
import com.example.turnpage.domain.order.repository.OrderRepository;
import com.example.turnpage.domain.salePost.entity.SalePost;
import com.example.turnpage.domain.salePost.repository.SalePostRepository;
import com.example.turnpage.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

import static com.example.turnpage.global.error.code.OrderErrorCode.NOT_ENOUGH_POINT_TO_ORDER;
import static com.example.turnpage.global.error.code.OrderErrorCode.SALEPOST_IS_ALREADY_SOLD;
import static com.example.turnpage.global.error.code.SalePostErrorCode.SALE_POST_NOT_FOUND;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final MemberService memberService;
    private final OrderRepository orderRepository;
    private final SalePostRepository salePostRepository;
    private final OrderConverter orderConverter;
    private AtomicInteger orderNumberSequence = new AtomicInteger(1);

    /**
     * createOrder()의 로직 순서
     * - 해당 판매 게시글이 유효한지 확인
     * - 해당 판매 게시글이 '판매 중'인지 확인
     * - 해당 판매 게시글의 상품 금액보다 많은 포인트를 회원이 가지고 있는지 확인
     * - 주문 엔티티 생성
     * - 회원 보유 포인트 - 상품 금액 연산을 진행해 회원 보유 포인트 update
     * - 판매 게시글 판매 완료로 상태 update
     * @param member
     * @param request
     * @return
     */
    @Transactional
    @Override
    public SimpleOrderInfo createOrder(Member member, CreateOrderRequest request) {
        Member persistedMember = memberService.findMember(member.getId());

        SalePost salePost = salePostRepository.findById(request.getSalePostId())
                        .orElseThrow(() -> new BusinessException(SALE_POST_NOT_FOUND));

        validateSalePostIsNotSold(salePost);
        validateMemberHasPointMoreThanPaymentAmount(persistedMember.getPoint(), salePost.getPrice());

        Order order = orderConverter.toEntity(persistedMember, salePost);
        order.generateOrderNumber(orderNumberSequence.getAndIncrement());
        orderRepository.save(order);

        salePost.setSold();
        persistedMember.minusPoint(salePost.getPrice());


        return orderConverter.toSimpleOrderInfo(order);
    }

    private void validateSalePostIsNotSold(SalePost salePost) {
        if (salePost.isSold() == true) {
            throw new BusinessException(SALEPOST_IS_ALREADY_SOLD);
        }
    }

    private void validateMemberHasPointMoreThanPaymentAmount(int memberPoint, int paymentAmount) {
        if (memberPoint < paymentAmount) {
            throw new BusinessException(NOT_ENOUGH_POINT_TO_ORDER);
        }
    }

    /**
     * 스케줄러 실행 주기: 매일 자정(00시 00분)
     * 스케줄러의 기능
     * - 주문번호의 시퀀스 번호를 날짜가 바뀔 때마다 1로 초기화
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void autoInitializeOrderNumberSequence() {
        System.out.println("======주문번호 시퀀스 초기화 스케줄러 실행======");
        this.orderNumberSequence.set(1);
    }
}
