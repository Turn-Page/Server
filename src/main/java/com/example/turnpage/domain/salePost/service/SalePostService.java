package com.example.turnpage.domain.salePost.service;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.salePost.dto.SalePostRequest.SaveSalePostRequest;
import com.example.turnpage.domain.salePost.dto.SalePostResponse.SalePostId;
import com.example.turnpage.domain.salePost.entity.SalePost;

public interface SalePostService {
    SalePostId saveSalePost(Member loginMember, SaveSalePostRequest request);
    SalePost findSalePost(Long salePostId);
}
