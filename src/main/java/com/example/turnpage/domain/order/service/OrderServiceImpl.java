package com.example.turnpage.domain.order.service;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.order.converter.OrderConverter;
import com.example.turnpage.domain.order.dto.OrderRequest.CreateOrderRequest;
import com.example.turnpage.domain.order.dto.OrderResponse.SimpleOrderInfo;
import com.example.turnpage.domain.order.entity.Order;
import com.example.turnpage.domain.order.repository.OrderRepository;
import com.example.turnpage.domain.salePost.entity.SalePost;
import com.example.turnpage.domain.salePost.repository.SalePostRepository;
import com.example.turnpage.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.turnpage.global.error.code.OrderErrorCode.NOT_ENOUGH_POINT_TO_ORDER;
import static com.example.turnpage.global.error.code.OrderErrorCode.SALEPOST_IS_ALREADY_SOLD;
import static com.example.turnpage.global.error.code.SalePostErrorCode.SALE_POST_NOT_FOUND;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final SalePostRepository salePostRepository;
    private final OrderConverter orderConverter;

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
        SalePost salePost = salePostRepository.findById(request.getSalePostId())
                        .orElseThrow(() -> new BusinessException(SALE_POST_NOT_FOUND));

        validateSalePostIsNotSold(salePost);
        validateMemberHasPointMoreThanPaymentAmount(member.getPoint(), salePost.getPrice());

        Order order = orderRepository.save(orderConverter.toEntity(member, salePost));

        salePost.setSold();
        member.minusPoint(salePost.getPrice());

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
}
