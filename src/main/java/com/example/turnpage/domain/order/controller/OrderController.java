package com.example.turnpage.domain.order.controller;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.order.dto.OrderRequest.CreateOrderRequest;
import com.example.turnpage.domain.order.dto.OrderResponse;
import com.example.turnpage.domain.order.dto.OrderResponse.PagedOrderInfo;
import com.example.turnpage.domain.order.dto.OrderResponse.SimpleOrderInfo;
import com.example.turnpage.domain.order.service.OrderService;
import com.example.turnpage.global.config.security.LoginMember;
import com.example.turnpage.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.turnpage.global.result.code.OrderResultCode.CREATE_ORDER;
import static com.example.turnpage.global.result.code.OrderResultCode.MY_ORDER_LIST;

@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "주문 API", description = "주문 관련 API입니다.")
@RestController
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "새 주문 등록 API", description = "상품을 구매하기 위한 주문 API입니다.")
    public ResultResponse<SimpleOrderInfo> createOrder(@LoginMember Member member, @RequestBody @Valid CreateOrderRequest request) {
        return ResultResponse.of(CREATE_ORDER, orderService.createOrder(member, request));
    }

    @GetMapping("/my")
    @Operation(summary = "내 주문 내역 목록 조회 API", description = "내 주문 내역 목록 목록 조회 API입니다. 주문 시간 기준 내림차순으로 조회합니다.")
    @Parameters(value = {
            @Parameter(name = "page", description = "조회할 페이지를 입력해 주세요.(0번부터 시작)"),
            @Parameter(name = "size", description = "한 페이지에 나타낼 주문 내역 개수를 입력해주세요.")
    })
    public ResultResponse<PagedOrderInfo> getMyOrderList(@LoginMember Member member,
                                                         @PageableDefault(sort = "orderedAt",
                                                   direction = Sort.Direction.DESC)
                                           @Parameter(hidden = true) Pageable pageable) {
        return ResultResponse.of(MY_ORDER_LIST, orderService.findMyOrderList(member, pageable));
    }
}
