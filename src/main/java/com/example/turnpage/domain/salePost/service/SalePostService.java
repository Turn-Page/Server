package com.example.turnpage.domain.salePost.service;

import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.salePost.dto.SalePostRequest.EditSalePostRequest;
import com.example.turnpage.domain.salePost.dto.SalePostRequest.SaveSalePostRequest;
import com.example.turnpage.domain.salePost.dto.SalePostResponse.PagedSalePostList;
import com.example.turnpage.domain.salePost.dto.SalePostResponse.SalePostId;
import com.example.turnpage.domain.salePost.entity.SalePost;
import org.springframework.data.domain.Pageable;


public interface SalePostService {
    SalePostId saveSalePost(Member loginMember, SaveSalePostRequest request);
    SalePostId editSalePost(Member loginMember, Long salePostId, EditSalePostRequest request);
    SalePostId deleteSalePost(Member loginMember, Long salePostId);
    SalePost findSalePost(Long salePostId);
    PagedSalePostList fetchSalePosts(Pageable pageable);
}
