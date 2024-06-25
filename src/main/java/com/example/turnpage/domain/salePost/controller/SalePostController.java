package com.example.turnpage.domain.salePost.controller;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.salePost.dto.SalePostRequest.EditSalePostRequest;
import com.example.turnpage.domain.salePost.dto.SalePostRequest.SaveSalePostRequest;
import com.example.turnpage.domain.salePost.dto.SalePostResponse.PagedSalePostInfo;
import com.example.turnpage.domain.salePost.service.SalePostService;
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
import org.springframework.web.bind.annotation.*;

import static com.example.turnpage.domain.salePost.dto.SalePostResponse.SalePostDetailInfo;
import static com.example.turnpage.domain.salePost.dto.SalePostResponse.SalePostId;
import static com.example.turnpage.global.result.code.SalePostResultCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/salePosts")
@Tag(name = "판매글 API", description = "판매글 API입니다.")
public class SalePostController {

    private final SalePostService salePostService;

    @Operation(summary = "판매글 저장 API", description = " 판매글 저장 API 입니다.")
    @PostMapping
    public ResultResponse<SalePostId> saveSalePost(@LoginMember Member member,
                                                   @RequestBody @Valid SaveSalePostRequest request) {
        return ResultResponse.of(SAVE_SALE_POST, salePostService.saveSalePost(member, request));
    }

    @Operation(summary = "판매글 수정 API", description = " 판매글 수정 API 입니다. path variable로 수정하고자 하는 salePostId를 주세요.")
    @PatchMapping(value = "/{salePostId}")
    public ResultResponse<SalePostId> editSalePost(@LoginMember Member member,
                                                   @PathVariable(value = "salePostId") Long salePostId,
                                                   @RequestBody @Valid EditSalePostRequest request) {
        return ResultResponse.of(EDIT_SALE_POST, salePostService.editSalePost(member, salePostId, request));
    }

    @Operation(summary = "판매글 삭제 API", description = " 판매글 삭제 API 입니다. path variable로 삭제하고자 하는 salePostId를 주세요.")
    @DeleteMapping(value = "/{salePostId}")
    public ResultResponse<SalePostId> deleteSalePost(@LoginMember Member member,
                                                     @PathVariable(value = "salePostId") Long salePostId) {
        return ResultResponse.of(DELETE_SALE_POST, salePostService.deleteSalePost(member, salePostId));
    }

    @Operation(summary = "판매글 목록 조회 API", description = " 판매 중인 도서 목록 조회 API 입니다. page는 0부터 시작합니다. 생성일 내림차순으로 조회됩니다." +
            "total false 일 경우 판매 중인 도서만, true일 경우 판매완료된 도서도 함께 조회됩니다.")
    @Parameters(value = {
            @Parameter(name = "page", description = "page 시작은 0번부터입니다."),
            @Parameter(name = "size", description = "한 페이지에 보일 salePost 개수를 입력해주세요.")
    })
    @GetMapping
    public ResultResponse<PagedSalePostInfo> fetchSalePosts(@RequestParam(name = "total", defaultValue = "false") boolean total,
                                                            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
                                                            @Parameter(hidden = true) Pageable pageable) {
        return ResultResponse.of(SALE_POST_LIST, salePostService.fetchSalePosts(total, pageable));
    }

    @Operation(summary = "판매글 검색 API", description = " 판매 중인 도서 검색 API 입니다. page는 0부터 시작합니다. 생성일 내림차순으로 조회됩니다." +
            "total false 일 경우 판매 중인 도서만, true일 경우 판매완료된 도서도 함께 조회됩니다. keyword는 필수입니다.")
    @Parameters(value = {
            @Parameter(name = "page", description = "page 시작은 0번부터입니다."),
            @Parameter(name = "size", description = "한 페이지에 보일 salePost 개수를 입력해주세요.")
    })
    @GetMapping("/search")
    public ResultResponse<PagedSalePostInfo> searchSalePost(@RequestParam(name = "total", defaultValue = "false") boolean total,
                                                            @RequestParam(name = "keyword") String keyword,
                                                            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
                                                            @Parameter(hidden = true) Pageable pageable) {
        return ResultResponse.of(SEARCH_SALE_POST, salePostService.searchSalePost(total, keyword, pageable));
    }

    @Operation(summary = "판매글 상세 조회 API", description = " 판매글 상세 조회 API 입니다. path variable로 salePostId를 주세요.")
    @GetMapping("/{salePostId}")
    public ResultResponse<SalePostDetailInfo> getSalePostDetailInfo(@LoginMember Member member, @PathVariable(value = "salePostId") Long salePostId) {
        return ResultResponse.of(SALE_POST_DETAIL, salePostService.getSalePostDetailInfo(member, salePostId));
    }

    @Operation(summary = "내 판매글 목록 조회 API", description = " 로그인 멤버가 작성한 판매글목록 조회 API 입니다. page는 0부터 시작합니다. 생성일 내림차순으로 조회됩니다." +
            "total false 일 경우 판매 중인 도서만, true일 경우 판매완료된 도서도 함께 조회됩니다.")
    @Parameters(value = {
            @Parameter(name = "page", description = "page 시작은 0번부터입니다."),
            @Parameter(name = "size", description = "한 페이지에 보일 salePost 개수를 입력해주세요.")
    })
    @GetMapping("/my")
    public ResultResponse<PagedSalePostInfo> fetchMySalePosts(@LoginMember Member member,
                                                              @RequestParam(name = "total", defaultValue = "false") boolean total,
                                                              @RequestParam
                                                              @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
                                                              @Parameter(hidden = true) Pageable pageable) {
        return ResultResponse.of(SALE_POST_LIST, salePostService.fetchMySalePosts(member, total, pageable));
    }

}
