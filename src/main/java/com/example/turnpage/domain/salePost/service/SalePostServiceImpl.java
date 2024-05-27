package com.example.turnpage.domain.salePost.service;

import com.example.turnpage.domain.book.entity.Book;
import com.example.turnpage.domain.book.service.BookService;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.domain.member.service.MemberService;
import com.example.turnpage.domain.salePost.converter.SalePostConverter;
import com.example.turnpage.domain.salePost.dto.SalePostRequest.EditSalePostRequest;
import com.example.turnpage.domain.salePost.dto.SalePostRequest.SaveSalePostRequest;
import com.example.turnpage.domain.salePost.dto.SalePostResponse.SalePostId;
import com.example.turnpage.domain.salePost.entity.Grade;
import com.example.turnpage.domain.salePost.entity.SalePost;
import com.example.turnpage.domain.salePost.repository.SalePostRepository;
import com.example.turnpage.global.error.BusinessException;
import com.example.turnpage.global.error.domain.SalePostErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SalePostServiceImpl implements SalePostService {
    private final SalePostConverter salePostConverter;
    private final SalePostRepository salePostRepository;
    private final MemberService memberService;
    private final BookService bookService;

    @Override
    @Transactional
    public SalePostId saveSalePost(Member loginMember, SaveSalePostRequest request) {
        Member member = memberService.findMember(loginMember.getId());

        //책 저장
        //1. request.bookInfo.itemId의 값을 가진 book이 db에 있는지 확인
        //2-1. 있으면 해당 책을 불러와 판매글의 book과 매핑
        //2-2. 없으면 해당 책의 bookInfo를 저장
        Book book = bookService.findBookByItemId(request.getBookInfo().getItemId())
                .orElseGet(() -> bookService.saveBookInfo(request.getBookInfo()));

        //판매글 저장
        SalePost salePost = salePostRepository.save(salePostConverter.toEntity(member, book,
                request.getTitle(), request.getDescription(), Grade.toGrade(request.getGrade()), request.getPrice()));

        return new SalePostId(salePost.getId());
    }

    @Override
    @Transactional
    public SalePostId editSalePost(Member loginMember, Long salePostId, EditSalePostRequest request) {
        Member member = memberService.findMember(loginMember.getId());
        SalePost salePost = findSalePost(salePostId);

        salePost.update(request.getTitle(), request.getDescription(),
                Grade.toGrade(request.getGrade()), request.getPrice());

        return new SalePostId(salePost.getId());
    }

    @Override
    @Transactional
    public SalePostId deleteSalePost(Member loginMember, Long salePostId) {
        Member member = memberService.findMember(loginMember.getId());
        SalePost salePost = findSalePost(salePostId);

        if(!member.getId().equals(salePost.getMember().getId()))
            throw new BusinessException(SalePostErrorCode.NO_AUTHORIZATION_SALE_POST);

        salePostRepository.deleteById(salePost.getId());

        return new SalePostId(salePost.getId());
    }



    @Override
    public SalePost findSalePost(Long salePostId) {
        return salePostRepository.findById(salePostId).orElseThrow(
                () -> new BusinessException(SalePostErrorCode.SALE_POST_NOT_FOUND));
    }

}
