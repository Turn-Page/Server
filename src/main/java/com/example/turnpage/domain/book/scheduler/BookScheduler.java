package com.example.turnpage.domain.book.scheduler;

import com.example.turnpage.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookScheduler {
    private final BookService bookService;
    @Scheduled(cron = "0 0 4 * * MON") //초 분 시 일 월 요일
    //TEST 용
   // @Scheduled(cron = "10 * * * * *") //초 분 시 일 월 요일
    public void saveBestSeller() {
        try {
            bookService.saveBestSeller();
            log.info("베스트셀러 저장이 완료되었습니다.");
        } catch (Exception e) {
            log.info("Batch 시스템이 예기치 않게 종료되었습니다. Message: {}", e.getMessage());
        }


    }
}
