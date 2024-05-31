package com.example.turnpage.domain.salePost.controller;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.salePost.dto.SalePostRequest.EditSalePostRequest;
import com.example.turnpage.domain.salePost.dto.SalePostRequest.SaveSalePostRequest;
import com.example.turnpage.domain.salePost.dto.SalePostResponse;
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

import static com.example.turnpage.global.result.code.SalePostResultCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/salePosts")
@Tag(name = "판매글 API", description = "판매글 API입니다.")
public class SalePostController {

    private final SalePostService salePostService;

    @Operation(summary = "판매글 저장 API", description = " 판매글 저장 API 입니다." )
    @PostMapping
    public ResultResponse<SalePostResponse.SalePostId> saveSalePost(@LoginMember Member member,
                                                                    @RequestBody @Valid SaveSalePostRequest request) {
        return ResultResponse.of(SAVE_SALE_POST, salePostService.saveSalePost(member,request));
    }

    @Operation(summary = "판매글 수정 API", description = " 판매글 수정 API 입니다. path variable로 수정하고자 하는 salePostId를 주세요.")
    @PatchMapping(value = "/{salePostId}")
    public ResultResponse<SalePostResponse.SalePostId> editSalePost(@LoginMember Member member,
                                                                    @PathVariable(value = "salePostId") Long salePostId,
                                                                    @RequestBody @Valid EditSalePostRequest request) {
        return ResultResponse.of(EDIT_SALE_POST, salePostService.editSalePost(member, salePostId, request));
    }

    @Operation(summary = "판매글 삭제 API", description = " 판매글 삭제 API 입니다. path variable로 삭제하고자 하는 salePostId를 주세요.")
    @DeleteMapping(value = "/{salePostId}")
    public ResultResponse<SalePostResponse.SalePostId> deleteSalePost(@LoginMember Member member,
                                                                    @PathVariable(value = "salePostId") Long salePostId) {
        return ResultResponse.of(DELETE_SALE_POST, salePostService.deleteSalePost(member, salePostId));
    }

    @Operation(summary = "판매 중인 도서 목록 조회 API", description = " 판매 중인 도서 목록 조회 API 입니다. page는 0부터 시작합니다. 생성일 내림차순으로 조회됩니다.")
    @Parameters(value = {
            @Parameter(name = "page", description = "page 시작은 0번부터입니다."),
            @Parameter(name = "size", description = "한 페이지에 보일 salePost 개수를 입력해주세요.")
    })
    @GetMapping
    public ResultResponse<PagedSalePostInfo> fetchSalePosts(@PageableDefault(sort = "created_at", direction = Sort.Direction.DESC)
                                                                                 @Parameter(hidden = true) Pageable pageable) {
        return ResultResponse.of(SALE_POST_LIST, salePostService.fetchSalePosts(pageable));
    }
}
