package com.qudkom.web.controller.board;

import com.qudkom.web.aop.annoation.*;
import com.qudkom.web.domain.dto.extended.board.post.PostLikeDto;
import com.qudkom.web.domain.dto.extended.board.post.image.ImageDto;
import com.qudkom.web.domain.dto.param.FieldBundle;
import com.qudkom.web.domain.dto.param.Paging;
import com.qudkom.web.domain.dto.param.Search;
import com.qudkom.web.domain.dto.param.bundle.BoardParamBundle;
import com.qudkom.web.domain.dto.util.ResponseDto;
import com.qudkom.web.domain.dto.view.BoardViewDto;
import com.qudkom.web.domain.dto.view.handler.CommentPageHandler;
import com.qudkom.web.domain.dto.view.handler.PageHandler;
import com.qudkom.web.domain.vo.board.post.Post;
import com.qudkom.web.domain.vo.board.post.PostCategory;
import com.qudkom.web.domain.vo.board.post.image.Image;
import com.qudkom.web.exception.AlreadyVotedException;
import com.qudkom.web.exception.AsyncForbiddenException;
import com.qudkom.web.exception.AsyncUnauthorizedException;
import com.qudkom.web.exception.UnauthorizedException;
import com.qudkom.web.service.board.BoardService;
import javassist.NotFoundException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

//@MultipartConfig(
//        location = "C:/Users/qudrj/Downloads",
//        maxFileSize = 1024*1024*10,
//        fileSizeThreshold = 1024*1024*10,
//        maxRequestSize = 1024*1024*150
//)

@Controller
@RequestMapping("/board")
public class BoardController {
    @Autowired
    BoardService boardService;

    @Autowired
    ResourceLoader resourceLoader;


    @CheckAutoLogin
    @IncreasePostHit
    @RequestMapping(method = RequestMethod.GET, value = {"/{bno}", "/{bno}/{pno}"})
    public String getBoardAndPost(@PathVariable HashMap<String, String> map,
                                  @RequestParam(value = "p", required = false, defaultValue = "1") Integer page,
                                  @RequestParam(value = "cno", required = false, defaultValue = "0") Integer commentNo,//댓글 알림용, 추후 구현
                                  @RequestParam(value = "cp", required = false, defaultValue = "0") Integer commentPage,//댓글 알림용, 추후 구현
                                  @RequestParam(value = "q", required = false, defaultValue = "") String query,
                                  @RequestParam(value = "w", required = false, defaultValue = "") String word,
                                  @CookieValue(value = "vl", required = false, defaultValue = "") String vl,
                                  @CookieValue(value="ps", required = false, defaultValue = ""+ PageHandler.DEFAULT_SINGLE_PAGE_SIZE) Integer pageSize,
                                  @CookieValue(value="pls", required = false, defaultValue = ""+ PageHandler.DEFAULT_PAGE_LIST_SIZE) Integer listSize,
                                  @CookieValue(value="cps", required = false, defaultValue = ""+ CommentPageHandler.DEFAULT_SINGLE_PAGE_SIZE) Integer commentPageSize,
                                  @CookieValue(value="cpls", required = false, defaultValue = ""+ CommentPageHandler.DEFAULT_PAGE_LIST_SIZE) Integer commentPageListSize,
//                                  @CookieValue(value = "ak", required = false) String autoKey,
                                  HttpServletRequest request,
                                  HttpServletResponse response,
                                  Model model
    ) throws NotFoundException, AsyncUnauthorizedException, UnauthorizedException {
        //pageSize는 스프링을 시작할때 초기화 메서드에서 메뉴와 같이 저장해놓고 꺼내쓴다
        Paging paging=Paging.builder()
                .listSize(listSize)
                .pageSize(pageSize)
                .offset((page-1)*pageSize)
                .page(page)
                .build();
        Paging commentPaging=Paging.builder()
                .pageSize(commentPageSize)
                .listSize(commentPageListSize)
                .page(commentPage)
                .build();
        Search search=Search.builder()
                .word(word)
                .query(query)
                .build();
        if(!query.equals("") || !word.equals("")){
            search.setQueries(query.split(" "));
            search.setWords(word.split(" "));
        }
        FieldBundle fieldBundle=FieldBundle.builder()
                .boardNo(Integer.parseInt(map.get("bno")))
                .commentNo(commentNo)
                .build();
        BoardParamBundle boardParamBundle=BoardParamBundle.builder()
                .fieldBundle(fieldBundle)
                .search(search)
                .paging(paging)
                .commentPaging(commentPaging)
                .build();
        if(map.containsKey("pno")){
            fieldBundle.setPostNo(Integer.parseInt(map.get("pno")));
//            boardParamBundle.setVisitedList(vl);
//            boardParamBundle.setResponse(response);
        }
        BoardViewDto boardViewDto = boardService.getBoardView(boardParamBundle);
        model.addAttribute("board", boardViewDto);
        return "board.view";
    }
    @CheckAutoLogin
    @RequestMapping(method = RequestMethod.GET, value = "/{bno}/post")
    public String getPostWritingPage(@PathVariable Integer bno,
                                     HttpServletRequest request,
                                     HttpServletResponse response,
                                     Model model
    )throws AsyncUnauthorizedException{
        List<PostCategory> postCategoryList = boardService.getPostCategoryList(bno);

        model.addAttribute("postCategoryList",postCategoryList);
        model.addAttribute("boardNo",bno);
        return "board.write";
    }

    @PutUserAfterLoginCheck
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/{bno}/post")
    public ResponseEntity<ResponseDto> uploadPost(@ModelAttribute Post post,
                                                  HttpServletRequest request
    ) throws SQLException, AsyncUnauthorizedException {
        //세션 체크
        String cPath=request.getContextPath();
        System.out.println("post.getContent() = " + post.getContent());
        int rowCnt = boardService.uploadPost(post);
//        return "redirect:/board/"+post.getBoardNo()+"/"+post.getPostNo();
        ResponseDto responseDto = ResponseDto.builder()
                .url(cPath+"/board/"+post.getBoardNo()+"/"+post.getPostNo())
                .message("성공적으로 업로드되었습니다.")
                .code(1)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

    @ResponseBody
    @RequestMapping(method=RequestMethod.POST , value = "/image"
            , consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Image> uploadImage(
            @RequestParam("file") MultipartFile file
    ){
        long t = System.currentTimeMillis();
        int r = (int)(Math.random()*1000000);
        String fileType=file.getContentType();
        String extension=fileType.substring(fileType.indexOf("/")+1);

        String fileNameIncludePath = new StringBuilder()
                .append(ImageDto.TEMP_REAL_PATH)
                .append(t).append(r)
                .append(".")
                .append(extension)
                .toString();

        File temp=new File(fileNameIncludePath);
//        System.out.println("temp.getAbsolutePath() = " + temp.getAbsolutePath());
        try {
            FileUtils.writeByteArrayToFile(temp, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //수정 시에 추가되는 이미지들은 똑같이 처리하고
        //게시물의 이미지 테이블을 조회하고 수정된 이미지 테이블의 이미지들과 비교하여
        //지워진 이미지들은 db에서 삭제하고 저장된 디렉토리에서도 삭제하도록 한다

        Image image=new Image();
        image.setFilename(temp.getName());
//        System.out.println("temp.getName() = " + temp.getName());
//        image.setFilename("");
        return new ResponseEntity<>(image, HttpStatus.OK);
    }


    @CheckAutoLogin
    @RequestMapping(method = RequestMethod.GET, value = "/{bno}/{pno}/edit")
    public String getPostEditingPage(@PathVariable Integer bno,
                                     @PathVariable Integer pno,
                                     HttpServletRequest request,
                                     HttpServletResponse response,
                                     Model model
    ) throws AsyncUnauthorizedException {
        //세션 체크
        Post post = Post.builder()
                .boardNo(bno)
                .postNo(pno)
//                .userNo()
                .build();
        post=boardService.getPost(post);
        List<PostCategory> postCategoryList=boardService.getPostCategoryList(bno);
        model.addAttribute("postCategoryList",postCategoryList);
        model.addAttribute("post", post);
        return "board.edit";
    }
    @CheckUserEquals
    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH, value = "/{bno}/{pno}")
    public ResponseEntity<ResponseDto> editPost(@RequestBody Post post,
                                                HttpServletRequest request

    ) throws SQLException, AsyncForbiddenException, AsyncUnauthorizedException {
        //세션 체크
        String cPath=request.getContextPath();
        int rowCnt = boardService.editPost(post);
        ResponseDto responseDto = ResponseDto.builder()
                .url(cPath+"/board/"+post.getBoardNo()+"/"+post.getPostNo())
//                .message("성공적으로 수정되었습니다.")
//                .code(1)
                .build();
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }
    @CheckUserEquals
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{bno}/{pno}")
    public ResponseEntity<ResponseDto> erasePost(@RequestBody Post post,
//                                                 HttpSession session
                                                 HttpServletRequest request

    ) throws SQLException, AsyncForbiddenException, AsyncUnauthorizedException {
//        세션 체크
        int rowCnt = boardService.erasePost(post);
        String cPath=request.getContextPath();
        ResponseDto responseDto = ResponseDto.builder()
                .url(cPath+"/board/"+post.getBoardNo())
//                .message("성공적으로 삭제되었습니다.")
//                .code(1)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//        return "redirect:/board/" + bno;
    }
    @PutUserAfterCheckNotEquals
    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH, value = "/{bno}/{pno}/vote")
    public ResponseEntity<ResponseDto> votePost(@RequestBody PostLikeDto postLikeDto,
//                                         HttpSession session
                                                HttpServletRequest request

    ) throws SQLException, AlreadyVotedException, AsyncUnauthorizedException {
        Post post=boardService.votePost(postLikeDto);
        ResponseDto responseDto=ResponseDto.builder().responseObject(post)
                .code(1)
                .build();
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }
    @PutUserNoAfterLoginCheck
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value="/{bno}/{pno}/vote")
    public ResponseEntity<ResponseDto> cancelVotePost(@RequestBody PostLikeDto postLikeDto,
//                               HttpSession session
                                                      HttpServletRequest request

    ) throws SQLException, AsyncUnauthorizedException {
        Post post=boardService.cancelVotePost(postLikeDto);
        ResponseDto responseDto=ResponseDto.builder().responseObject(post)
//                .code(1)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}