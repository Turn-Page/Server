package com.example.turnpage.domain.comment.service;

import com.example.turnpage.domain.book.entity.Book;
import com.example.turnpage.domain.book.service.BookService;
import com.example.turnpage.domain.comment.converter.CommentConverter;
import com.example.turnpage.domain.comment.dto.CommentRequest.WriteCommentRequest;
import com.example.turnpage.domain.comment.dto.CommentResponse.CommentId;
import com.example.turnpage.domain.comment.dto.CommentResponse.PagedCommentInfo;
import com.example.turnpage.domain.comment.entity.Comment;
import com.example.turnpage.domain.comment.repository.CommentRepository;
import com.example.turnpage.domain.member.entity.Member;
import com.example.turnpage.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.turnpage.global.error.code.CommentErrorCode.COMMENT_NOT_FOUND;
import static com.example.turnpage.global.error.code.CommentErrorCode.WRITER_ONLY_DELETE_COMMENT;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentServiceImpl implements CommentService {
    private final BookService bookService;
    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;

    @Transactional
    @Override
    public CommentId writeComment(Member member, WriteCommentRequest request) {
        Book book = bookService.findBook(request.getBookId());

        Comment comment = commentConverter.toEntity(member, book, request);
        commentRepository.save(comment);

        return commentConverter.toCommentId(comment.getId());
    }

    @Override
    public PagedCommentInfo findCommentListByBookId(Long bookId, Pageable pageable) {
        bookService.findBook(bookId);

        Page<Comment> commentList = commentRepository.findByBookId(bookId, pageable);

        return commentConverter.toPagedCommentInfo(commentList);
    }

    @Transactional
    @Override
    public CommentId deleteComment(Member member, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(COMMENT_NOT_FOUND));

        validateWriter(comment, member);
        comment.delete();

        return commentConverter.toCommentId(comment.getId());
    }

    private void validateWriter(Comment comment, Member member) {
        Long writerId = comment.getMember().getId();

        if (!writerId.equals(member.getId())) {
            throw new BusinessException(WRITER_ONLY_DELETE_COMMENT);
        }
    }
}
