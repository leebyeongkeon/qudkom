package com.qudkom.web.controller;

import com.qudkom.web.aop.annoation.CheckAutoLogin;
import com.qudkom.web.domain.dto.param.Paging;
import com.qudkom.web.domain.dto.param.Search;
import com.qudkom.web.domain.dto.param.bundle.BoardParamBundle;
import com.qudkom.web.domain.dto.view.BoardViewDto;
import com.qudkom.web.domain.dto.view.handler.PageHandler;
import com.qudkom.web.service.board.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("")
public class HomeController {
    @Autowired
    BoardService boardService;

    @CheckAutoLogin
    @RequestMapping(value={"","/"})
    public String getIndex(@CookieValue(value="ips", required = false, defaultValue = ""+ PageHandler.INDEX_BOARD_CARD_SIZE) Integer pageSize,
                           HttpServletRequest request, HttpServletResponse response,
                           Model model){
        Paging paging=Paging.builder()
                .pageSize(pageSize)
                .offset(0)
                .build();
        List<BoardViewDto> boardViewDtoList = boardService.getIndexView(paging);
        model.addAttribute("boardViewDtoList", boardViewDtoList);
        return "home.index";
    }
    @CheckAutoLogin
    @RequestMapping("/search")
    public String getSearchResult(@RequestParam(value = "w", defaultValue = "") String word,
                                  @RequestParam(value = "p", required = false, defaultValue = "1") Integer page,
                                  @CookieValue(value="ps", required = false, defaultValue = ""+ PageHandler.DEFAULT_SINGLE_PAGE_SIZE) Integer pageSize,
                                  @CookieValue(value="pls", required = false, defaultValue = ""+ PageHandler.DEFAULT_PAGE_LIST_SIZE) Integer listSize,
                                  HttpServletRequest request, HttpServletResponse response,
                                  Model model){
        if(!word.equals("")) {
            Paging paging=Paging.builder()
                    .page(page)
                    .listSize(listSize)
                    .pageSize(pageSize)
                    .offset((page-1)*pageSize)
                    .build();
            Search search = Search.builder()
                    .queries("title content".split(" "))
                    .words(word.split(" "))
//                    .word(word)
                    .build();
            BoardParamBundle boardParamBundle = BoardParamBundle.builder()
                    .paging(paging)
                    .search(search)
                    .build();
            BoardViewDto boardViewDto=boardService.getSearchResult(boardParamBundle);
            model.addAttribute("boardView",boardViewDto);
        }
        return "home.search";
    }
}
