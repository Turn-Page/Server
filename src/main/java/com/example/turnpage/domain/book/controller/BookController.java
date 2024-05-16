package com.example.turnpage.domain.book.controller;

import com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;
import com.example.turnpage.domain.book.dto.BookResponse.BookPageInfos;
import com.example.turnpage.domain.book.service.BookService;
import com.example.turnpage.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.turnpage.domain.book.dto.BookResponse.BookId;
import static com.example.turnpage.global.result.code.BookResultCode.FETCH_BESTSELLER;
import static com.example.turnpage.global.result.code.BookResultCode.SAVE_BOOK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
@Tag(name = "책 API", description = "책 API입니다.")
public class BookController {

    private final BookService bookService;

    @PostMapping()
    @Operation(summary = "책 정보 저장 API", description = " 책 정보 저장 API 입니다.")
    public ResultResponse<BookId> saveBook(SaveBookRequest request) {

        return ResultResponse.of(SAVE_BOOK.getResultCode(),  bookService.saveBook(request));
    }

    @GetMapping("/bestSeller")
    @Parameters(value = {
            @Parameter(name = "page", description = "page 시작은 0번부터입니다."),
            @Parameter(name = "size", description = "한 페이지에 보일 book 개수를 입력해주세요.")
    })
    @Operation(summary = "베스트 셀러 목록 조회 API", description = " 베스트 셀러 목록 조회 API 입니다.")
    public ResultResponse<BookPageInfos> fetchBestSeller(@PageableDefault(sort = "ranking", direction = Sort.Direction.ASC)
                                                              @Parameter(hidden = true) Pageable pageable) {
        return ResultResponse.of(FETCH_BESTSELLER.getResultCode(), bookService.fetchBestSeller(pageable));
    }

}
