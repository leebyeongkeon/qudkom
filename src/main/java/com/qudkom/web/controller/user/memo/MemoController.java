package com.qudkom.web.controller.user.memo;

import com.qudkom.web.aop.annoation.CheckAutoLogin;
import com.qudkom.web.aop.annoation.PutUserNoToMemo;
import com.qudkom.web.aop.annoation.RequiredLogin;
import com.qudkom.web.domain.dto.extended.user.memo.MemoDto;
import com.qudkom.web.domain.dto.param.bundle.MemoParamBundle;
import com.qudkom.web.domain.dto.param.Paging;
import com.qudkom.web.domain.dto.param.Search;
import com.qudkom.web.domain.dto.util.ResponseDto;
import com.qudkom.web.domain.dto.view.MemoViewDto;
import com.qudkom.web.domain.dto.view.handler.MemoPageHandler;
import com.qudkom.web.domain.vo.user.User;
import com.qudkom.web.exception.UnauthorizedException;
import com.qudkom.web.service.user.UserService;
import com.qudkom.web.service.user.memo.MemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/memo")
public class MemoController {
    @Autowired
    MemoService memoService;
    @Autowired
    UserService userService;

    @CheckAutoLogin
    @PutUserNoToMemo
    @RequestMapping(method = RequestMethod.GET, value = {"/{cate}/{mno}","/{cate}"})
    public String getMemoView(@PathVariable Map<String, String> map,
                              @RequestParam(required = false, defaultValue = "false") Boolean save,
                              @RequestParam(value="mp", required = false, defaultValue = "1") Integer mp,
                              @CookieValue(value = "mps", required = false, defaultValue = ""+MemoPageHandler.DEFAULT_SINGLE_PAGE_SIZE) Integer pageSize,
                              @CookieValue(value = "mpls", required = false, defaultValue = ""+MemoPageHandler.DEFAULT_PAGE_LIST_SIZE) Integer listSize,
                              MemoDto memoDto,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              Model model) throws SQLException, UnauthorizedException {
        String category=(String)map.get("cate");
        if(map.containsKey("mno")) {
            Integer mno = Integer.parseInt(map.get("mno"));
            if(mno!=null){
                memoDto.setMemoNo(mno);
            }
        }
        if(!category.equals(MemoParamBundle.TEMP_CATEGORY)){
            memoDto.setSave(save);
            memoDto.setDelete(false);
        }
        Paging paging= Paging.builder()
                .page(mp)
                .pageSize(pageSize)
                .listSize(listSize)
                .offset((mp-1)*pageSize)
                .build();
        MemoParamBundle memoParamBundle = MemoParamBundle.builder()
                .category(category)
                .paging(paging)
                .memoDto(memoDto)
                .build();
        MemoViewDto memoViewDto = memoService.getMemoView(memoParamBundle);
        model.addAttribute("memoView",memoViewDto);
        return "memo.view";
    }

    @CheckAutoLogin
    @RequiredLogin
    @RequestMapping(method = RequestMethod.GET, value = "/write")
    public String getMemoWritingPage(HttpServletRequest request,
                                     HttpServletResponse response) throws UnauthorizedException {
        return "memo.write";
    }

    @PutUserNoToMemo
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value="/{cate}")
    public ResponseEntity<ResponseDto> sendMemo(@ModelAttribute MemoDto memoDto,
                                                @PathVariable(value = "cate") String category,
                                                HttpServletRequest request
    ){
        int rowCnt=0;
        if(category.equals(MemoParamBundle.SEND_CATEGORY)){
            rowCnt=memoService.sendMemo(memoDto);
        }else if(category.equals(MemoParamBundle.TEMP_CATEGORY)){
            rowCnt=memoService.saveTemp(memoDto);
        }
        String url=request.getRequestURI()+"/"+memoDto.getMemoNo();
        ResponseDto responseDto=ResponseDto.builder()
//                .code(1)
                .url(url)
                .build();
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }
    @PutUserNoToMemo
    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH, value = "/batch/{cate}")
    public ResponseEntity<ResponseDto> batchSave(
                                                @RequestBody MemoParamBundle memoParamBundle,
                                                 @PathVariable(value = "cate") String category,
                                                 MemoDto memoDto,
                                                 HttpServletRequest request
    ) throws SQLException, UnauthorizedException {
//        System.out.println("memoParamBundle = " + memoParamBundle);
        memoService.moveMemoList(memoParamBundle);

        ResponseDto responseDto = ResponseDto.builder()
//                .code(1)
                .url(request.getContextPath()+"/memo/"+category)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    @PutUserNoToMemo
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/batch/{cate}")
    public ResponseEntity<ResponseDto> batchDelete(@RequestBody MemoParamBundle memoParamBundle,
                                                   @PathVariable(value = "cate") String category,
                                                   MemoDto memoDto,
                                                   HttpServletRequest request
    ) throws SQLException,UnauthorizedException {
        memoService.eraseMemoList(memoParamBundle);
        ResponseDto responseDto = ResponseDto.builder()
//                .code(1)
                .url(request.getContextPath()+"/memo/"+category)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    @CheckAutoLogin
    @RequiredLogin
    @RequestMapping(method = RequestMethod.GET, value="/users")
    public String getUserList(@RequestParam(required = false, defaultValue = "") String query,
                              @RequestParam(required = false, defaultValue = "") String word,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              Model model) throws UnauthorizedException{
        MemoViewDto memoViewDto=new MemoViewDto();
        memoViewDto.setCategory("users");
        if(!query.equals("") && !word.equals("")){
            Search search= Search.builder().word(word).query(query).build();
            List<User> list=userService.searchUser(search);
            memoViewDto.setUserList(list);
        }
        model.addAttribute("memoView", memoViewDto);
        return "memo.user";
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET,value = "/new")
    public String getNewMemoCount(HttpSession session){
        int newCount=0;
        if(session!=null){
            User user=(User)session.getAttribute("user");
            if(user!=null){
                newCount=memoService.getNewMemoCount(user.getUserNo());
            }
        }
        return String.valueOf(newCount);
    }
}
