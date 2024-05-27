package com.example.turnpage.domain.salePost.controller;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.salePost.dto.SalePostRequest;
import com.example.turnpage.domain.salePost.dto.SalePostRequest.EditSalePostRequest;
import com.example.turnpage.domain.salePost.dto.SalePostRequest.SaveSalePostRequest;
import com.example.turnpage.domain.salePost.dto.SalePostResponse;
import com.example.turnpage.domain.salePost.service.SalePostService;
import com.example.turnpage.global.config.security.LoginMember;
import com.example.turnpage.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
        return ResultResponse.of(SAVE_SALE_POST.getResultCode(), salePostService.saveSalePost(member,request));
    }

    @Operation(summary = "판매글 수정 API", description = " 판매글 수정 API 입니다. path variable로 수정하고자 하는 salePostId를 주세요.")
    @PatchMapping(value = "/{salePostId}")
    public ResultResponse<SalePostResponse.SalePostId> editSalePost(@LoginMember Member member,
                                                                    @PathVariable(value = "salePostId") Long salePostId,
                                                                    @RequestBody @Valid EditSalePostRequest request) {
        return ResultResponse.of(EDIT_SALE_POST.getResultCode(), salePostService.editSalePost(member, salePostId, request));
    }

    @Operation(summary = "판매글 삭제 API", description = " 판매글 삭제 API 입니다. path variable로 삭제하고자 하는 salePostId를 주세요.")
    @DeleteMapping(value = "/{salePostId}")
    public ResultResponse<SalePostResponse.SalePostId> deleteSalePost(@LoginMember Member member,
                                                                    @PathVariable(value = "salePostId") Long salePostId) {
        return ResultResponse.of(DELETE_SALE_POST.getResultCode(), salePostService.deleteSalePost(member, salePostId));
    }
}
