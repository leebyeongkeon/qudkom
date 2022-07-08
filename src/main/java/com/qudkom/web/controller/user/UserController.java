package com.qudkom.web.controller.user;

import com.qudkom.web.domain.dto.extended.user.UserProfileInfoDto;
import com.qudkom.web.domain.dto.extended.user.UserRegisterInfoDto;
import com.qudkom.web.domain.dto.util.ResponseDto;
import com.qudkom.web.domain.vo.user.User;
import com.qudkom.web.exception.ForbiddenException;
import com.qudkom.web.exception.UnauthorizedException;
import com.qudkom.web.exception.UserRegistException;
import com.qudkom.web.service.user.UserService;
import com.qudkom.web.service.user.alarm.AlarmService;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    AlarmService alarmService;
    @Autowired
    MailSender mailSender;

    @RequestMapping(method = RequestMethod.GET, value = "/register")
    public String getRegisterPage() {
        return "user.register";
    }//회원가입 페이지

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<ResponseDto> signUp(HttpServletRequest request,
                                              @ModelAttribute UserRegisterInfoDto userRegisterInfoDto) throws SQLException, UserRegistException {
        userService.validateUser(userRegisterInfoDto, request.getSession());
        int rowCnt = userService.signUp(userRegisterInfoDto);
        ResponseDto responseDto = ResponseDto.builder()
                .code(rowCnt)
                .message("성공적으로 가입되었습니다.")
                .url(request.getContextPath())
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }//회원가입하기

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<ResponseDto> logIn(@ModelAttribute UserProfileInfoDto userProfileInfoDto,
//                                             HttpSession session,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {
        System.out.println("userProfileInfoDto = " + userProfileInfoDto);
        ResponseDto responseDto = userService.userLogin(userProfileInfoDto, request, response);
//        ResponseDto responseDto = userService.userLogin(userProfileInfoDto, session, response);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }//로그인하기

    //    @CheckAutoLogin
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/logout")
    public ResponseEntity<ResponseDto> logOut(@CookieValue(value = "ak", required = false) String autoKey,
//                                              HttpSession session,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {
        HttpSession session=request.getSession();
        String contextPath=request.getContextPath();
        User user = (User) session.getAttribute("user");
        if (autoKey != null && user != null) {
            System.out.println("autoKey = " + autoKey);
            userService.invalidateAutoKey(user.getUserNo());
            Cookie cookie = new Cookie("ak", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        session.invalidate();
        ResponseDto responseDto = ResponseDto.builder().message("서버에서 로그아웃되었습니다.").code(1).build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }//로그아웃

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/id")
    public ResponseEntity<ResponseDto> checkId(@RequestParam String id, HttpSession session) {
        String message="";
        int code=userService.checkId(id)?1:0;
        if(code==1){
            message="이미 존재하는 아이디입니다.";
        }else{
            message="사용 가능한 아이디입니다.";
            session.setAttribute(UserRegisterInfoDto.ID_FIELD_NAME,id);
        }
        ResponseDto responseDto = ResponseDto.builder()
                .code(code).message(message)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/nickname")
    public ResponseEntity<ResponseDto> checkNickname(@RequestParam String nickname, HttpSession session) {
        int code=userService.checkNickname(nickname)?1:0;
        String message="";
        if(code==1){
            message="이미 존재하는 닉네임입니다.";
        }else{
            message = "사용 가능한 닉네임입니다.";
            session.setAttribute(UserRegisterInfoDto.NICKNAME_FIELD_NAME,nickname);
        }
        ResponseDto responseDto = ResponseDto.builder()
                .code(code)
                .message(message)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/email/send")
    public ResponseEntity<ResponseDto> sendMail(@RequestParam String email, HttpSession session) {
        int code=userService.checkEmail(email)?1:0;
        String message="";
        if(code==1){
            message="이미 존재하는 메일입니다.";
        }else{
            String generatedString = RandomStringUtils.randomAlphanumeric(10);
            SimpleMailMessage simpleMailMessage = setMailMessage(email, generatedString, session);
            mailSender.send(simpleMailMessage);

            message = "메일을 전송했습니다.";

            session.setAttribute(UserRegisterInfoDto.EMAIL_FIELD_NAME,email);
            session.setAttribute(UserRegisterInfoDto.CERIFCODE_FIELD_NAME,generatedString);

        }
        ResponseDto responseDto=ResponseDto.builder().message(message).code(code).build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    private SimpleMailMessage setMailMessage(String email, String generatedString, HttpSession session){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("인증 코드");
        String content = session.getMaxInactiveInterval()/60+"분간 유효합니다.\n\n" + generatedString;
        simpleMailMessage.setText(content);
        return simpleMailMessage;
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/email/cerif")
    public ResponseEntity<ResponseDto> cerifyMail(@RequestParam String cerifCode, HttpSession session) {
        String getCode=(String)session.getAttribute(UserRegisterInfoDto.CERIFCODE_FIELD_NAME);
        int code=0;
        String message="";
        if(cerifCode.equals(getCode)){
            code=1;
            message="메일 인증에 성공했습니다.";
        }else{
            code=0;
            message="메일 인증에 실패했습니다.";
        }
        ResponseDto responseDto= ResponseDto.builder().code(code).message(message).build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/profile")
    public String getProfile(HttpSession session, Model model) throws UnauthorizedException {
        return "user.profile";
    }//회원정보 조회

    @RequestMapping(method = RequestMethod.PATCH, value = "/profile")
    public ResponseEntity<ResponseDto> editProfile(User user, HttpSession session) throws ForbiddenException, UnauthorizedException {
        ResponseDto responseDto = userService.editUserProfile(user);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }//회원정보 수정

    @RequestMapping(method = RequestMethod.DELETE, value = "/signout")
    public ResponseEntity<ResponseDto> signOut(User user, HttpSession session) throws ForbiddenException, UnauthorizedException {
        //비밀번호 전송받고, 세션에서 아이디 꺼내서
        ResponseDto responseDto = userService.signOut(user);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }//회원탈퇴

    @RequestMapping(method = RequestMethod.GET, value = "/{uno}")
    public String getUserInfo(@PathVariable Integer uno) {
        return "user.";
    }//다른 사람의 정보를 조회할때, 다른 사람에게 보여지는 사용자 본인의 프로필도 확인 가능

    @RequestMapping(method = RequestMethod.GET, value = "/forgot/id")
    public String forgotId() {
        return "user.";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/forgot/id")
    public String findId() {
        return "";
    }//이메일을 받아서 해당 이메일로 아이디 알려주기 or 아이디 바로 보여주기

    @RequestMapping(method = RequestMethod.GET, value = "/forgot/pwd")
    public String forgotPwd() {
        return "user.";
    }//아이디를 적고 아이디에 등록된 이메일로 코드 전송 후에 인증받기

    // or 재설정 페이지를 이메일로 안내
    @RequestMapping(method = RequestMethod.POST, value = "/forgot/pwd")
    public String findPwd() {
        return "";
    }
    //이메일 전송, 이메일로 인증 후 변경 페이지 안내
    //1. 임시 비밀번호 발급(db반영은 X) 후에 세션에 저장, 이메일로 전송하고
    //값 인증한 후에 임시 비밀번호 인증 후에 비밀번호 변경
    //2. 그냥 이메일 인증 후에 비밀번호 변경이 가능한 페이지로 안내
    //변경 페이지로 접근할 때는 세션으로 인증 여부 검사
}