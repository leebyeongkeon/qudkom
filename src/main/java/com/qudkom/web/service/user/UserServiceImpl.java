package com.qudkom.web.service.user;

import com.qudkom.web.dao.board.post.PostDao;
import com.qudkom.web.dao.board.post.comment.CommentDao;
import com.qudkom.web.dao.user.alarm.AlarmDao;
import com.qudkom.web.dao.user.UserDao;
import com.qudkom.web.dao.user.memo.MemoDao;
import com.qudkom.web.domain.dto.extended.user.UserProfileInfoDto;
import com.qudkom.web.domain.dto.extended.user.UserRegisterInfoDto;
import com.qudkom.web.domain.dto.param.Search;
import com.qudkom.web.domain.dto.util.MessageKey;
import com.qudkom.web.domain.dto.util.ResponseDto;
import com.qudkom.web.domain.vo.user.User;
import com.qudkom.web.exception.UserRegistException;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    PostDao postDao;
    @Autowired
    CommentDao commentDao;
//    @Autowired
//    MailSender mailSender;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MemoDao memoDao;
    @Autowired
    AlarmDao alarmDao;

    @Override
    public ResponseDto userLogin(UserProfileInfoDto userProfileInfoDto, HttpServletRequest request, HttpServletResponse response){
        UserProfileInfoDto selectedUser = userDao.selectUserForLogin(userProfileInfoDto.getId());
        ResponseDto responseDto;
        boolean logined=false;
        if(selectedUser==null){//계정이 존재하지 않는 경우
            logined=false;
        }
        else {
            boolean passwordSame=passwordEncoder.matches(userProfileInfoDto.getPassword().toUpperCase(),selectedUser.getPassword());
            if(passwordSame){
                logined=true;
            }
            else{
                logined=false;
            }
        }
        if(logined){
            if(userProfileInfoDto.getAutoLogin()){
                setAutoLogin(selectedUser, request, response);
            }
            selectedUser.setPassword(null);
            int userNo=selectedUser.getUserNo();
            int newMemoCount=memoDao.selectNewCount(userNo);
            int newAlarmCount=alarmDao.selectNewCount(userNo);
            selectedUser.setNewMemoCount(newMemoCount);
            selectedUser.setNewAlarmCount(newAlarmCount);

            HttpSession session=request.getSession();
            session.setAttribute("user",selectedUser);
            responseDto=ResponseDto.builder().code(1).message("로그인 성공").url("/")
                    .build();
        }else{
            responseDto = ResponseDto.builder()
                    .code(0).message("아이디 또는 비밀번호가 일치하지 않습니다.")
                    .build();
        }
        return responseDto;

    }
    @Override
    public int signUp(User user) throws SQLException {
        user.setPassword(passwordEncoder.encode(user.getPassword().toUpperCase()));
        int rowCnt=userDao.insertUser(user);
        if(rowCnt==0) throw new SQLException();//id, nickname, email 유니크 키
        return rowCnt;
    }
    @Override
    public ResponseDto editUserProfile(User user){
        ResponseDto responseDto;
        int rowCnt=userDao.updateUser(user);
        if(rowCnt==1){
            responseDto=ResponseDto.builder()
                    .message("정보가 수정되었습니다.").code(1)
                    .build();
        }else{
            responseDto=ResponseDto.builder()
                    .message("비밀번호가 일치하지 않습니다.").code(0)
                    .build();
        }
        return responseDto;
    }
    @Override
    public ResponseDto signOut(User user){
        int rowCnt=userDao.deleteUser(user);
        ResponseDto responseDto;
        if(rowCnt==1){
            responseDto=ResponseDto.builder()
                    .code(1).message("탈퇴가 완료되었습니다.")
                    .url("/").build();
        }else{
            responseDto=ResponseDto.builder()
                    .code(0).message("비밀번호가 일치하지 않습니다.")
                    .build();
        }
        return responseDto;
    }
    @Override
    public boolean checkId(String id){
        return userDao.selectIdExists(id);
    }
    @Override
    public boolean checkNickname(String nickname){
        return userDao.selectNicknameExists(nickname);
    }
    @Override
    public boolean checkEmail(String email){
        return userDao.selectEmailExists(email);
    }
//    @Override
//    public ResponseDto sendMail(String toEmail, HttpSession session){
//        String message="";
//        int code=0;
//        if(this.checkEmail(toEmail)){
//            code=1;
//            message="이미 존재하는 메일입니다.";
//        }
//        else {
//            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//            simpleMailMessage.setTo(toEmail);
//            simpleMailMessage.setSubject("인증 코드");
//            String generatedString = RandomStringUtils.randomAlphanumeric(10);
//            String content = session.getMaxInactiveInterval()/60+"분간 유효합니다.\n\n" + generatedString;
//            simpleMailMessage.setText(content);
//
//            mailSender.send(simpleMailMessage);
//
//            code = 0;
//            message = "메일을 전송했습니다.";
//
//            session.setAttribute(UserRegisterInfoDto.EMAIL_FIELD_NAME,toEmail);
//            session.setAttribute(UserRegisterInfoDto.CERIFCODE_FIELD_NAME,generatedString);
//
//        }
//        ResponseDto responseDto=ResponseDto.builder().message(message).code(code).build();
//        return responseDto;
//    }

    @Override
    public void validateUser(UserRegisterInfoDto userRegisterInfoDto, HttpSession session) throws UserRegistException {
        List<MessageKey> list = userRegisterInfoDto.validate(session);
        if(list.size()>0)
            throw new UserRegistException(list);
    }

    @Override
    public UserProfileInfoDto getUserProfile(int userNo) {
        User user=userDao.selectUserByUserNo(userNo);

        UserProfileInfoDto userProfileInfoDto=new UserProfileInfoDto(user);

        int postCount=postDao.selectCountByUserNo(userNo);
        int commentCount=commentDao.selectCountByUserNo(userNo);
        int newMemoCount=memoDao.selectNewCount(userNo);
        int newAlarmCount=alarmDao.selectNewCount(userNo);

        userProfileInfoDto.setPostCount(postCount);
        userProfileInfoDto.setCommentCount(commentCount);
        userProfileInfoDto.setNewMemoCount(newMemoCount);
        userProfileInfoDto.setNewAlarmCount(newAlarmCount);

        return userProfileInfoDto;
    }

//    @Override
    public void setAutoLogin(User user, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session=request.getSession();
        int remainedValidTime=60*60*24*7;
        String sessionId = session.getId();
        String autoKey=sessionId;
        user.setAutoKey(autoKey);

        Date expiryDate=new Date();
        expiryDate.setTime(expiryDate.getTime()+remainedValidTime*1000);
        user.setAutoKeyExpiryDate(expiryDate);

        System.out.println("user = " + user);
        int rowCnt = userDao.updateUserAutoKey(user);
        if(rowCnt==1){
//            String contextPath=request.getContextPath();
            System.out.println("자동 로그인 키 업데이트");
            Cookie cookie=new Cookie("ak",autoKey);
            cookie.setPath("/");
            cookie.setMaxAge(remainedValidTime);
            response.addCookie(cookie);
        }
    }

    @Override
    public UserProfileInfoDto getUserProfile(String autoKey) {
        UserProfileInfoDto user = userDao.selectUserByAutoKey(autoKey);
        return user;
    }

    @Override
    public void invalidateAutoKey(int userNo) throws Exception {
        int rowCnt=userDao.updateUserAutoKeyInvalid(userNo);
        if(rowCnt==0) throw new Exception();
    }

    public String generateAutoKey(User user){

        return "";
    }

    @Override
    public int updateUserAutoKey(User user){
        return userDao.updateUserAutoKey(user);
    }

    @Override
    public List<User> searchUser(Search Search) {
        return userDao.selectList(Search);
    }
}
