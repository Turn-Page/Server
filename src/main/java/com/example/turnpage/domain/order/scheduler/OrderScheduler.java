package com.example.turnpage.domain.order.scheduler;

import com.example.turnpage.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderScheduler {
    private final OrderService orderService;

    /**
     * 스케줄러 실행 주기: 매일 자정(00시 00분)
     * 스케줄러의 기능
     * - 주문번호의 시퀀스 번호를 날짜가 바뀔 때마다 1로 초기화
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void autoInitializeOrderNumberSequence() {
        System.out.println("======주문번호 시퀀스 초기화 스케줄러 실행======");
        this.orderService.resetOrderNumberSequence();
    }
}
