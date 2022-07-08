package com.qudkom.web.controller.user.alarm;

import com.qudkom.web.aop.annoation.CheckAutoLogin;
import com.qudkom.web.aop.annoation.PutUserNoAfterLoginCheck;
import com.qudkom.web.aop.annoation.RequiredLogin;
import com.qudkom.web.domain.dto.extended.user.alarm.AlarmDto;
import com.qudkom.web.domain.dto.param.bundle.AlarmParamBundle;
import com.qudkom.web.domain.dto.param.Paging;
import com.qudkom.web.domain.dto.util.ResponseDto;
import com.qudkom.web.domain.dto.view.AlarmViewDto;
import com.qudkom.web.domain.dto.view.handler.AlarmPageHandler;
import com.qudkom.web.domain.vo.user.User;
import com.qudkom.web.domain.vo.user.alarm.Alarm;
import com.qudkom.web.exception.AsyncUnauthorizedException;
import com.qudkom.web.service.user.alarm.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/alarm")

public class AlarmController {
    @Autowired
    AlarmService alarmService;

    @CheckAutoLogin
    @RequiredLogin
    @PutUserNoAfterLoginCheck
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public String getAlarmPage(
            @CookieValue(value = "aps", required = false, defaultValue = ""+ AlarmPageHandler.DEFAULT_SINGLE_PAGE_SIZE) Integer pageSize,
            @CookieValue(value = "apls", required = false, defaultValue = ""+ AlarmPageHandler.DEFAULT_PAGE_LIST_SIZE) Integer listSize,
            @RequestParam(value = "ap", required = false, defaultValue = "1") Integer ap,
            AlarmDto alarm,
            HttpServletRequest request,
            HttpServletResponse response, Model model) throws AsyncUnauthorizedException {
        Paging paging = Paging.builder()
                .pageSize(pageSize)
                .offset((ap-1)*pageSize)
                .listSize(listSize)
                .page(ap)
                .build();
        AlarmParamBundle alarmParamBundle = AlarmParamBundle.builder()
                .paging(paging)
                .alarm(alarm)
                .build();
        AlarmViewDto alarmViewDto=alarmService.getAlarmView(alarmParamBundle);
        model.addAttribute("alarmView", alarmViewDto);
        return "alarm";
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET,value = "/new")
    public String getNewAlarmCount(HttpSession session){
        System.out.println("새 알람 개수 알아와");
        int newCount=0;
        if(session!=null){
            User user=(User)session.getAttribute("user");
            if(user!=null){
                newCount=alarmService.getNewAlarmCount(user.getUserNo());
            }
        }
        return String.valueOf(newCount);
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE,value = "/{ano}")
    public ResponseEntity<ResponseDto> eraseAlarm(@PathVariable(value = "ano") Integer alarmNo,
                                                  HttpSession session
    ){
        User user=(User)session.getAttribute("user");
        Alarm alarm= Alarm.builder()
                .userNo(user.getUserNo())
                .alarmNo(alarmNo)
                .build();
        int rowCnt=alarmService.eraseAlarm(alarm);
        ResponseDto responseDto=ResponseDto.builder()
                .code(rowCnt)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
