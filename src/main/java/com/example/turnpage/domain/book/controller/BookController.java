package com.example.turnpage.domain.book.controller;

import com.example.turnpage.domain.book.client.BookSearchClient;
import com.example.turnpage.domain.book.dto.BookRequest.SaveBookRequest;
import com.example.turnpage.domain.book.dto.BookResponse.PagedBookInfo;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.turnpage.domain.book.dto.BookResponse.BookDetailInfo;
import static com.example.turnpage.domain.book.dto.BookResponse.BookId;
import static com.example.turnpage.global.result.code.BookResultCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
@Tag(name = "책 API", description = "책 API입니다.")
public class BookController {

    private final BookService bookService;
    private final BookSearchClient bookSearchClient;

    @PostMapping()
    @Operation(summary = "책 정보 저장 API", description = "책 정보 저장 API 입니다.")
    public ResultResponse<BookId> saveBook(SaveBookRequest request) {

        return ResultResponse.of(SAVE_BOOK,  bookService.saveBook(request));
    }

    @GetMapping("/bestSeller")
    @Parameters(value = {
            @Parameter(name = "page", description = "page 시작은 0번부터입니다."),
            @Parameter(name = "size", description = "한 페이지에 보일 book 개수를 입력해주세요.")
    })
    @Operation(summary = "베스트 셀러 목록 조회 API", description = "베스트 셀러 목록 조회 API 입니다.")
    public ResultResponse<PagedBookInfo> fetchBestSeller(@PageableDefault(sort = "ranking", direction = Sort.Direction.ASC)
                                                              @Parameter(hidden = true) Pageable pageable) {
        return ResultResponse.of(FETCH_BESTSELLER, bookService.fetchBestSeller(pageable));
    }

    @GetMapping("/{bookId}")
    @Parameters(value = {
            @Parameter(name = "bookId", description = "path variable로 bookId를 주세요."),
    })
    @Operation(summary = "책 정보 상세 조회 API", description = "책 정보 상세 조회 API 입니다.")
    public ResultResponse<BookDetailInfo> getBookDetailInfo(@PathVariable("bookId") Long bookId) {
        return ResultResponse.of(BOOK_INFO, bookService.getBookDetailInfo(bookId));
    }

    @GetMapping("/search")
    @Parameters(value = {
            @Parameter(name = "keyword", description = "파라미터로 keyword를 주세요."),
            @Parameter(name = "page", description = "page 시작은 0번부터입니다."),
            @Parameter(name = "size", description = "한 페이지에 보일 book 개수를 입력해주세요.")
    })
    @Operation(summary = "책 검색 API", description = "책 검색 API 입니다. 책 이름이나 작가로 책을 검색할 수 있습니다.")
    public ResultResponse<PagedBookInfo> searchBook(@RequestParam(name = "keyword") String keyword,
                                                    @PageableDefault @Parameter(hidden = true) Pageable pageable) {
        return ResultResponse.of(SEARCH_BOOK, bookService.searchBook(keyword, pageable));
    }

    @GetMapping("/openAPI/search")
    @Operation(summary = "알라딘 OPEN API에서 책 검색 API", description = "알라딘 OPEN API 에서 검색 API 입니다. 책 이름이나 작가로 책을 검색할 수 있습니다." +
            "검색 결과 10개까지 반환됩니다.")
    public ResultResponse<List<SaveBookRequest>> openAPISearchBook(@RequestParam(name = "keyword") String keyword) {
        return ResultResponse.of(SEARCH_BOOK, bookSearchClient.getSearchResult(keyword));
    }


}
