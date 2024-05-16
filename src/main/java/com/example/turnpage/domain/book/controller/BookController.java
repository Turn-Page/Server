package com.example.turnpage.domain.book.controller;

import com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;
import com.example.turnpage.domain.book.service.BookService;
import com.example.turnpage.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.turnpage.domain.book.dto.BookResponse.BookId;
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

}
