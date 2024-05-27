package com.example.turnpage.domain.salePost.controller;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.salePost.dto.SalePostRequest.SaveSalePostRequest;
import com.example.turnpage.domain.salePost.dto.SalePostResponse;
import com.example.turnpage.domain.salePost.service.SalePostService;
import com.example.turnpage.global.config.security.LoginMember;
import com.example.turnpage.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.turnpage.global.result.code.SalePostResultCode.SAVE_SALE_POST;

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
}
