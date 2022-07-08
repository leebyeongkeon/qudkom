package com.qudkom.web.controller.board.post.comment;

import com.qudkom.web.aop.annoation.CheckUserEquals;
import com.qudkom.web.aop.annoation.PutUserAfterCheckNotEquals;
import com.qudkom.web.aop.annoation.PutUserAfterLoginCheck;
import com.qudkom.web.aop.annoation.PutUserNoAfterLoginCheck;
import com.qudkom.web.domain.dto.extended.board.post.comment.CommentLikeDto;
import com.qudkom.web.domain.dto.param.Paging;
import com.qudkom.web.domain.dto.param.bundle.CommentParamBundle;
import com.qudkom.web.domain.dto.util.ResponseDto;
import com.qudkom.web.domain.dto.view.handler.CommentPageHandler;
import com.qudkom.web.domain.vo.board.post.comment.Comment;
import com.qudkom.web.domain.dto.view.CommentViewDto;
import com.qudkom.web.exception.AlreadyVotedException;
import com.qudkom.web.exception.AsyncForbiddenException;
import com.qudkom.web.exception.AsyncUnauthorizedException;
import com.qudkom.web.service.board.post.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@Controller
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{bno}/{pno}")
    public ResponseEntity<CommentViewDto> getCommentList(
            @PathVariable Integer bno,
            @PathVariable Integer pno,
            @RequestParam(required = false, defaultValue = "0") Integer cp,
            @CookieValue(value="cps", required = false, defaultValue = ""+ CommentPageHandler.DEFAULT_SINGLE_PAGE_SIZE) Integer pageSize,
            @CookieValue(value="cpls", required = false, defaultValue = ""+ CommentPageHandler.DEFAULT_PAGE_LIST_SIZE) Integer listSize
    ) {
//        cp가 null이면 댓글의 마지막 페이지를 기본으로

        Comment comment=Comment.builder()
                .boardNo(bno).postNo(pno)
                .build();
        Paging paging=Paging.builder()
                .listSize(listSize)
                .pageSize(pageSize)
                .page(cp)
                .offset((cp-1)*pageSize)
                .build();
        CommentParamBundle commentParamBundle = CommentParamBundle.builder()
                .comment(comment)
                .usePage(true)
                .paging(paging)
                .build();
        return new ResponseEntity<>(commentService.getCommentCTE(commentParamBundle), HttpStatus.OK);
    }//댓글 새로고침(cp 0), 댓글 페이징 cp는 댓글 페이지

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{bno}/{pno}/{cno}")
    public ResponseEntity<CommentViewDto> getCommentListByCommentNo(
            @PathVariable Integer bno,
            @PathVariable Integer pno,
            @PathVariable Integer cno,
            @CookieValue(value="cps", required = false, defaultValue = ""+ CommentPageHandler.DEFAULT_SINGLE_PAGE_SIZE) Integer pageSize,
            @CookieValue(value="cpls", required = false, defaultValue = ""+ CommentPageHandler.DEFAULT_PAGE_LIST_SIZE) Integer listSize
    ) {
        //cp가 0이면 댓글 리스트로
        boolean page = cno == 0 ? true : false;
        //cno가 0인 잘못된 요청일 경우 댓글 마지막 페이지 요청과 같게 처리

        Comment comment=Comment.builder()
                .boardNo(bno).postNo(pno).commentNo(cno)
                .build();
        Paging paging=Paging.builder()
                .listSize(listSize)
                .pageSize(pageSize)
                .build();
        CommentParamBundle commentParamBundle = CommentParamBundle.builder()
                .comment(comment)
                .usePage(page)
                .paging(paging)
                .build();
        System.out.println(commentParamBundle);
        return new ResponseEntity<>(commentService.getCommentCTE(commentParamBundle),HttpStatus.OK);
    }

    @PutUserAfterLoginCheck
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/{bno}/{pno}")
    public ResponseEntity<CommentViewDto> writeComment(
            @RequestBody Comment comment,
            HttpServletRequest request,
            @CookieValue(value="cps", required = false, defaultValue = ""+ CommentPageHandler.DEFAULT_SINGLE_PAGE_SIZE) Integer pageSize,
            @CookieValue(value="cpls", required = false, defaultValue = ""+ CommentPageHandler.DEFAULT_PAGE_LIST_SIZE) Integer listSize
    ) throws SQLException, AsyncUnauthorizedException {
        System.out.println("댓글 작성");
        Paging paging=Paging.builder()
                .pageSize(pageSize)
                .listSize(listSize)
                .build();
        CommentParamBundle commentParamBundle = CommentParamBundle.builder()
                .paging(paging)
                .comment(comment)
                .usePage(false)
                .build();
        return new ResponseEntity<>(commentService.writeCommentCTE(commentParamBundle),HttpStatus.OK);
    }//댓글 작성 후 해당 댓글 페이지로

    @CheckUserEquals
    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH, value = "/{bno}/{pno}/{cno}")
    public ResponseEntity<CommentViewDto> editComment(
            @RequestBody Comment comment,
            HttpServletRequest request,
            @CookieValue(value="cps", required = false, defaultValue = ""+ CommentPageHandler.DEFAULT_SINGLE_PAGE_SIZE) Integer pageSize,
            @CookieValue(value="cpls", required = false, defaultValue = ""+ CommentPageHandler.DEFAULT_PAGE_LIST_SIZE) Integer listSize
    ) throws SQLException, AsyncUnauthorizedException, AsyncForbiddenException {
        System.out.println("댓글 수정");
        //게시판 글 댓글 유저 번호, 내용 5개 필드
        Paging paging=Paging.builder()
                .pageSize(pageSize)
                .listSize(listSize)
                .build();
        CommentParamBundle commentParamBundle = CommentParamBundle.builder()
                .paging(paging)
                .comment(comment)
                .usePage(false)
                .build();
        return new ResponseEntity<>(commentService.editCommentCTE(commentParamBundle),HttpStatus.OK);
    }

    @CheckUserEquals
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{bno}/{pno}/{cno}")
    public ResponseEntity<CommentViewDto> eraseComment(
            @RequestBody Comment comment,
            HttpServletRequest request,
            @CookieValue(value="cps", required = false, defaultValue = ""+ CommentPageHandler.DEFAULT_SINGLE_PAGE_SIZE) Integer pageSize,
            @CookieValue(value="cpls", required = false, defaultValue = ""+ CommentPageHandler.DEFAULT_PAGE_LIST_SIZE) Integer listSize
    ) throws SQLException, AsyncUnauthorizedException, AsyncForbiddenException {
        System.out.println("댓글 삭제");
        //프론트에서는 권한에 따라 보이거나 안보이도록하고 검사도, 백엔드에서도 세션으로 검사
        Paging paging=Paging.builder()
                .pageSize(pageSize)
                .listSize(listSize)
                .build();
        CommentParamBundle commentParamBundle = CommentParamBundle.builder()
                .paging(paging)
                .comment(comment)
                .build();
        return new ResponseEntity<>(commentService.eraseCommentCTE(commentParamBundle),HttpStatus.OK);
    }

    @PutUserAfterCheckNotEquals
    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH, value = "/{bno}/{pno}/{cno}/vote")
    public ResponseEntity<ResponseDto> voteComment(
            @RequestBody CommentLikeDto commentLikeDto,
            HttpServletRequest request
            ) throws SQLException, AlreadyVotedException, AsyncUnauthorizedException {
        System.out.println("댓글 추천 반대");
        //게시글 번호, 게시판 번호, 댓글 번호, 사용자 번호, 추천 반대 여부, 캔슬인지 아닌지
        Comment comment=commentService.voteComment(commentLikeDto);
        ResponseDto responseDto=ResponseDto.builder().responseObject(comment)
                .code(1)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    @PutUserNoAfterLoginCheck
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{bno}/{pno}/{cno}/vote")
    public ResponseEntity<ResponseDto> cancelVoteComment(
            @RequestBody CommentLikeDto commentLikeDto,
            HttpServletRequest request
    ) throws SQLException,AsyncUnauthorizedException {
        Comment comment=commentService.cancelVoteComment(commentLikeDto);
        ResponseDto responseDto=ResponseDto.builder().responseObject(comment)
//                .code(1)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{bno}/{pno}/{cno}/exists")
    public ResponseEntity<ResponseDto> checkCommentExists(
            @PathVariable(value = "bno") Integer boardNo,
            @PathVariable(value = "pno") Integer postNo,
            @PathVariable(value = "cno") Integer commentNo,
            @CookieValue(value="cps", required = false, defaultValue = ""+ CommentPageHandler.DEFAULT_SINGLE_PAGE_SIZE) Integer pageSize
            ){
        Comment comment=Comment.builder()
                .boardNo(boardNo)
                .postNo(postNo)
                .commentNo(commentNo)
                .build();
        Paging paging=Paging.builder()
                .pageSize(pageSize).build();
        CommentParamBundle commentParamBundle = CommentParamBundle.builder()
                .comment(comment)
                .paging(paging)
                .build();
        boolean exists=commentService.checkExists(comment);
        Integer page=0;
        if(exists){
            page=commentService.calcCommentPage(commentParamBundle);
        }
        int code=exists?1:0;
        ResponseDto responseDto=ResponseDto.builder()
                .responseObject(page)
                .code(code).build();
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }
}