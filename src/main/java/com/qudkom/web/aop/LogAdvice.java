package com.qudkom.web.aop;

import com.qudkom.web.domain.dto.extended.user.UserProfileInfoDto;
import com.qudkom.web.domain.dto.extended.user.memo.MemoDto;
import com.qudkom.web.domain.dto.param.bundle.MemoParamBundle;
import com.qudkom.web.domain.vo.board.post.Post;
import com.qudkom.web.domain.vo.user.User;
import com.qudkom.web.exception.AsyncForbiddenException;
import com.qudkom.web.exception.AsyncUnauthorizedException;
import com.qudkom.web.exception.ForbiddenException;
import com.qudkom.web.exception.UnauthorizedException;
import com.qudkom.web.service.board.BoardService;
import com.qudkom.web.service.user.UserService;
import com.qudkom.web.service.user.alarm.AlarmService;
import com.qudkom.web.service.user.memo.MemoService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Aspect
//@Log4j
@Slf4j
@Component
public class LogAdvice {
    @Autowired
    BoardService boardService;
    @Autowired
    UserService userService;
    @Autowired
    MemoService memoService;
    @Autowired
    AlarmService alarmService;

    @Pointcut("execution(* com.qudkom.web.controller..*.*(..))")
    private void cut(){}
    @Pointcut("@annotation(com.qudkom.web.aop.annoation.LoginCheck)")
    private void cutLoginCheck(){}
    @Before("cut() && cutLoginCheck()")
    public UserProfileInfoDto loginCheck(JoinPoint jp) {

        UserProfileInfoDto user=null;
        for(Object obj: jp.getArgs()) {
            if (obj instanceof HttpServletRequest) {
                HttpServletRequest request = (HttpServletRequest) obj;
                HttpSession session = request.getSession();
                user = (UserProfileInfoDto) session.getAttribute("user");
                break;
            }
        }
        return user;
    }
    private Integer extractUserNo(String userNo,Object...objs) {
        Integer i = null;
        for (Object obj : objs)
            try {
                Class clazz = Class.forName(obj.getClass().getName());
                Field f = clazz.getDeclaredField(userNo);
                f.setAccessible(true);
                i = (Integer) f.get(obj);
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        return i;
    }

    @Pointcut("@annotation(com.qudkom.web.aop.annoation.CheckUserEquals)")
    private void cutCheckUserEquals(){}

    @Before("cut() && cutCheckUserEquals()")//로그인 체크, 사용자 번호
    public boolean checkUserEquals(JoinPoint jp) throws AsyncUnauthorizedException, AsyncForbiddenException {
        User user=loginCheck(jp);
        if(user==null) throw new AsyncUnauthorizedException();
        Integer userNo=extractUserNo(UserProfileInfoDto.USER_NO,jp.getArgs());
        if(userNo==null || user.getUserNo()!=userNo) {
//            System.out.println("잘못된 세션 접근");
            throw new AsyncForbiddenException();
        }
        System.out.println("동일 유저 확인");
        return true;
    }
    //새 글 or 댓글 작성시 사용자 프로필 정보를 글 or 댓글 객체에 설정
    private void putUserInfo(UserProfileInfoDto user, Object...objs){
        for(Object obj:objs){
            System.out.println(obj);
            try {
                String className=obj.getClass().getName();
                int count=0;
                for(String fieldName: UserProfileInfoDto.FIELD_NAMES){
                    try {
                        Class clazz=user.getClass().getSuperclass();

                        Field field=clazz.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        Object value=field.get(user);

                        Class targetClazz=Class.forName(className);
                        Field targetField = targetClazz.getDeclaredField(fieldName);

                        targetField.setAccessible(true);
                        targetField.set(obj, value);
                    }catch(NoSuchFieldException e){
                        e.printStackTrace();
                        continue;
                    } catch(IllegalAccessException e){
                        e.printStackTrace();
                        continue;
                    }
                    count++;
                }
                if(UserProfileInfoDto.FIELD_NAMES.length==count){
                    System.out.println(obj);
                    break;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    private void putUserNo(Integer userNo, Object...objs){
        for(Object obj:objs) {
            System.out.println(obj);
            try {
                String className = obj.getClass().getSuperclass().getName();
                Class clazz=Class.forName(className);

                Field f=clazz.getDeclaredField(UserProfileInfoDto.USER_NO);
                f.setAccessible(true);
                f.set(obj,userNo);
                System.out.println("obj = " + obj);
                break;
            }catch (NoSuchFieldException e) {
                e.printStackTrace();
                
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    //putUserNo메서드에서 부모 객체가 아닌 자식 객체를 사용하도록 바꿀 경우
    // NoSuchFieldException catch절에서 호출할 수 있도록
    private void putUserNoToSuper(Integer userNo, Object obj){
        String className = obj.getClass().getSuperclass().getName();
        try {
            Class clazz=Class.forName(className);
            Field f=clazz.getDeclaredField(UserProfileInfoDto.USER_NO);
            f.setAccessible(true);
            f.set(obj,userNo);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Pointcut("@annotation(com.qudkom.web.aop.annoation.PutUserAfterLoginCheck)")
    private void cutPutUserAfterLoginCheck(){}
    @Before("cut() && cutPutUserAfterLoginCheck()")
    public void putUserAfterLoginCheck(JoinPoint jp) throws AsyncUnauthorizedException, ForbiddenException {
        UserProfileInfoDto user=loginCheck(jp);
        if(user==null)
            throw new AsyncUnauthorizedException();
//        else {//로그인 되어있는 상태.
            putUserInfo(user,jp.getArgs());
            // 새 글 or 댓글 작성시 세션에서 사용자 정보를 꺼내서 객체에 넣는다
//        }
    }
    @Pointcut("@annotation(com.qudkom.web.aop.annoation.PutUserAfterCheckNotEquals)")
    private void cutPutUserAfterCheckNotEquals(){}
    @Before("cut() && cutPutUserAfterCheckNotEquals()")
    public void putUserAfterCheckNotEquals(JoinPoint jp) throws AsyncUnauthorizedException, AsyncForbiddenException {
        User user=loginCheck(jp);
        if(user==null) throw new AsyncUnauthorizedException();
        Integer loginedUserNo=user.getUserNo();
        Integer writerUserNo=extractUserNo("writerUserNo",jp.getArgs());
        System.out.println("user = " + user);
        System.out.println("writerUserNo = " + writerUserNo);
        if(writerUserNo==null || loginedUserNo==writerUserNo) {
            System.out.println("자기 자신의 글이나 댓글에는 추천/반대/신고할 수 없음");
            throw new AsyncForbiddenException();
        }
        putUserNo(loginedUserNo,jp.getArgs());
        System.out.println("추천/반대/신고 가능");
    }
    @Pointcut("@annotation(com.qudkom.web.aop.annoation.PutUserNoAfterLoginCheck)")
    private void cutPutUserNoAfterLoginCheck(){}
    @Before("cut() && cutPutUserNoAfterLoginCheck()")
    public void putUserNoAfterLoginCheck(JoinPoint jp) throws AsyncUnauthorizedException, ForbiddenException {
        User user=loginCheck(jp);
        if(user==null) throw new AsyncUnauthorizedException();
//        Integer writerUserNo=extractUserNo("writerUserNo",jp.getArgs());
//        if(user.getUserNo()==writerUserNo){
            //추천인과 작성자가 같은 경우->자기 자신의 글/댓글을 추천 못하게 막아놨는데 취소도 막을 필요는 없을 것 같아서 하지 않기로 함
//            throw new ForbiddenException();
//        }else{
        System.out.println("추천/반대 취소");
            putUserNo(user.getUserNo(),jp.getArgs());
//        }
    }//로그인 체크 후에 세션에서 뽑아낸 사용자 번호를 likeDto객체의 writerUserNo와 비교하여 같지 앟은지 체크한 후에 세팅

    @Pointcut("@annotation(com.qudkom.web.aop.annoation.PutUserNoToMemo)")
    private void cutPutUserNoToMemo(){}
    @Before("cut() && cutPutUserNoToMemo()")
    public void putUserNoToMemo(JoinPoint jp) throws UnauthorizedException {
        User user = loginCheck(jp);
        if(user==null) throw new UnauthorizedException();
        String category=null;
        MemoDto memoDto=null;
        MemoParamBundle memoParamBundle =null;
        for(Object obj:jp.getArgs()){
            if (obj instanceof MemoParamBundle) {
                memoParamBundle = (MemoParamBundle) obj;
            }
            else if(obj instanceof MemoDto){
                memoDto=(MemoDto)obj;
            }
            else if(obj instanceof HttpServletRequest){
                HttpServletRequest request = (HttpServletRequest)obj;
                String[] pathVariables=request.getRequestURI().split("/");
                for(String path:pathVariables){
                    if(path.equals(MemoParamBundle.SEND_CATEGORY) ||
                            path.equals(MemoParamBundle.RECEIVE_CATEGORY)||
                            path.equals(MemoParamBundle.TEMP_CATEGORY)) {
                        category=path;
                        break;
                    }
                }
            }
        }
//        if(memoDto==null)
//            memoDto=new MemoDto();
        memoDto.setUserNo(user.getUserNo());
        if(memoParamBundle !=null && category!=null){
            memoParamBundle.setCategory(category);
            memoParamBundle.setMemoDto(memoDto);
        }
        System.out.println("category = " + category);
        System.out.println("memo = " + memoDto);
    }
    @Pointcut("@annotation(com.qudkom.web.aop.annoation.RequiredLogin)")
    private void cutRequiredLogin(){}

    @Before("cut() && cutRequiredLogin()")
    public void requiredLogin(JoinPoint jp) throws UnauthorizedException {
        User user=loginCheck(jp);
        if(user==null) throw new UnauthorizedException();
    }
    @Order(0)
    @Pointcut("@annotation(com.qudkom.web.aop.annoation.CheckAutoLogin)")
    private void cutAutoLoginCheck(){
    }

    @Before("cut() && cutAutoLoginCheck()")
    public void autoLoginCheck(JoinPoint jp) {

        HttpServletRequest request=null;
        HttpServletResponse response=null;
        for(Object obj:jp.getArgs()){
            if(obj instanceof HttpServletRequest){
                request=(HttpServletRequest) obj;
            }else if(obj instanceof HttpServletResponse){
                response=(HttpServletResponse)obj;
            }
        }
        HttpSession session=null;
        UserProfileInfoDto sessionUser=null;
        String autoKey=null;

        if(request!=null){
            Cookie[] cookies = request.getCookies();
            if(cookies==null) return;
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("ak")){
                    autoKey=cookie.getValue();
                    break;
                }
            }
            session = request.getSession();
            sessionUser=(UserProfileInfoDto) session.getAttribute("user");
        }

        if(autoKey!=null && response!=null) {
            if (sessionUser == null) {
                // 자동로그인 키 새로 발급, 만료시간 업데이트
                // 자동 로그인 키가 유효한 경우 세션 설정, 자동 로그인 키도 업데이트
                UserProfileInfoDto user = userService.getUserProfile(autoKey);
                if (user != null) {
                    updateAutoKey(user, session, response);
                    sessionUser=user;
//                    session.setAttribute("user", user);
                }
            } else {//로그인이 되어있었던 경우,
                // 자동로그인 키도 있을 경우에는 자동 로그인 키 만료기간을 업데이트

                updateAutoKey(sessionUser, session, response);
                //자동 로그인 설정되어 있는 상태에서 사이트에 접속해서 세션 설정은 되어 있지 않은 경우
            }
        }

        if(sessionUser!=null){
            int userNo=sessionUser.getUserNo();
            int newMemoCount=memoService.getNewMemoCount(userNo);
            int newAlarmCount=alarmService.getNewAlarmCount(userNo);
            sessionUser.setNewMemoCount(newMemoCount);
            sessionUser.setNewAlarmCount(newAlarmCount);
            sessionUser.setLevelAndExp();
            session.setAttribute("user",sessionUser);
        }
    }
    void updateAutoKey(User user, HttpSession session, HttpServletResponse response){
        String newAutoKey=session.getId();
        int remainedValidTime=60*60*24*7;
        user.setAutoKey(newAutoKey);

        Date expiryDate=new Date();
        expiryDate.setTime(expiryDate.getTime()+remainedValidTime*1000);
        user.setAutoKeyExpiryDate(expiryDate);

        userService.updateUserAutoKey(user);
        Cookie cookie=new Cookie("ak",user.getAutoKey());
        cookie.setPath("/");
        cookie.setMaxAge(remainedValidTime);
        response.addCookie(cookie);
    }

    // 자동로그인 체크 - 자동로그인 키 유효 && 세션 없을 경우 세션 생성 - 예외X
    // 로그인 체크 - 세션 체크
    // 동일 유저인지 체크 - 수정, 삭제 시 권한 검사
    // 동일 유저가 아닌지 체크 - 자기 자신의 글, 댓글에 추천 혹은 반대하는 경우 방지
    // 글, 댓글 작성 시 로그인 체크 후 사용자 정보 주입 (로그인 체크 후)
    // 글, 댓글 추천/반대 시 사용자 번호 주입 (로그인 체크 후)
    // 메모 객체에 사용자 번호 주입



    @Pointcut("@annotation(com.qudkom.web.aop.annoation.IncreasePostHit)")
    private void cutIncreasePostHit() {
    }

    @After("cut() && cutIncreasePostHit()")
    public void increasePostHit(JoinPoint jp) {
        HttpServletRequest request = null;
        HashMap<String, String> map = null;
        Cookie[] cookies = null;
        Integer bno = null;
        Integer pno = null;
        String visited = null;

        HttpServletResponse response = null;
        for (Object obj : jp.getArgs()) {
            if (obj instanceof HashMap) {
                map = (HashMap<String, String>) obj;
                System.out.println("map = " + map);
                if(map.containsKey("bno")) {
                    bno = Integer.parseInt(map.get("bno"));
                    System.out.println("bno: "+bno);
                }
                if (map.containsKey("pno")) {
                    pno = Integer.parseInt(map.get("pno"));
                    System.out.println("pno: "+pno);
                }
            } else if (obj instanceof HttpServletRequest) {
                request = (HttpServletRequest) obj;
                cookies = request.getCookies();
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("vl")) {
                        visited = cookie.getValue();
                        break;
                    }
                }
            } else if (obj instanceof HttpServletResponse) {
                response = (HttpServletResponse) obj;
            }
        }

        if (response != null && visited != null && bno != null && pno != null) {
            String view = new StringBuilder()
                    .append(bno)
                    .append('-').append(pno)
                    .toString();
            if (!visited.contains(view)) {
//                String contextPath=request.getContextPath();
                visited = visited.equals("") ? view : visited + "/" + view;
                Cookie cookie = new Cookie("vl", visited);
                cookie.setPath("/");
                cookie.setMaxAge(getRemainSecondForTommorow());
//            response.setLocale(Locale.KOREA);
                response.addCookie(cookie);
                Post post=Post.builder()
                        .boardNo(bno)
                        .postNo(pno)
                        .build();
                boardService.increasePostHit(post);
                System.out.println("조회수 증가 aop");
            }
        }
    }
    private int getRemainSecondForTommorow() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tommorow = LocalDateTime.now().plusDays(1L).truncatedTo(ChronoUnit.DAYS);
        return (int) now.until(tommorow, ChronoUnit.SECONDS);
    }
}
