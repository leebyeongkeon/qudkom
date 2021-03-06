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

    @Before("cut() && cutCheckUserEquals()")//????????? ??????, ????????? ??????
    public boolean checkUserEquals(JoinPoint jp) throws AsyncUnauthorizedException, AsyncForbiddenException {
        User user=loginCheck(jp);
        if(user==null) throw new AsyncUnauthorizedException();
        Integer userNo=extractUserNo(UserProfileInfoDto.USER_NO,jp.getArgs());
        if(userNo==null || user.getUserNo()!=userNo) {
//            System.out.println("????????? ?????? ??????");
            throw new AsyncForbiddenException();
        }
        System.out.println("?????? ?????? ??????");
        return true;
    }
    //??? ??? or ?????? ????????? ????????? ????????? ????????? ??? or ?????? ????????? ??????
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
    //putUserNo??????????????? ?????? ????????? ?????? ?????? ????????? ??????????????? ?????? ??????
    // NoSuchFieldException catch????????? ????????? ??? ?????????
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
//        else {//????????? ???????????? ??????.
            putUserInfo(user,jp.getArgs());
            // ??? ??? or ?????? ????????? ???????????? ????????? ????????? ????????? ????????? ?????????
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
            System.out.println("?????? ????????? ????????? ???????????? ??????/??????/????????? ??? ??????");
            throw new AsyncForbiddenException();
        }
        putUserNo(loginedUserNo,jp.getArgs());
        System.out.println("??????/??????/?????? ??????");
    }
    @Pointcut("@annotation(com.qudkom.web.aop.annoation.PutUserNoAfterLoginCheck)")
    private void cutPutUserNoAfterLoginCheck(){}
    @Before("cut() && cutPutUserNoAfterLoginCheck()")
    public void putUserNoAfterLoginCheck(JoinPoint jp) throws AsyncUnauthorizedException, ForbiddenException {
        User user=loginCheck(jp);
        if(user==null) throw new AsyncUnauthorizedException();
//        Integer writerUserNo=extractUserNo("writerUserNo",jp.getArgs());
//        if(user.getUserNo()==writerUserNo){
            //???????????? ???????????? ?????? ??????->?????? ????????? ???/????????? ?????? ????????? ??????????????? ????????? ?????? ????????? ?????? ??? ????????? ?????? ????????? ???
//            throw new ForbiddenException();
//        }else{
        System.out.println("??????/?????? ??????");
            putUserNo(user.getUserNo(),jp.getArgs());
//        }
    }//????????? ?????? ?????? ???????????? ????????? ????????? ????????? likeDto????????? writerUserNo??? ???????????? ?????? ????????? ????????? ?????? ??????

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
                // ??????????????? ??? ?????? ??????, ???????????? ????????????
                // ?????? ????????? ?????? ????????? ?????? ?????? ??????, ?????? ????????? ?????? ????????????
                UserProfileInfoDto user = userService.getUserProfile(autoKey);
                if (user != null) {
                    updateAutoKey(user, session, response);
                    sessionUser=user;
//                    session.setAttribute("user", user);
                }
            } else {//???????????? ??????????????? ??????,
                // ??????????????? ?????? ?????? ???????????? ?????? ????????? ??? ??????????????? ????????????

                updateAutoKey(sessionUser, session, response);
                //?????? ????????? ???????????? ?????? ???????????? ???????????? ???????????? ?????? ????????? ?????? ?????? ?????? ??????
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

    // ??????????????? ?????? - ??????????????? ??? ?????? && ?????? ?????? ?????? ?????? ?????? - ??????X
    // ????????? ?????? - ?????? ??????
    // ?????? ???????????? ?????? - ??????, ?????? ??? ?????? ??????
    // ?????? ????????? ????????? ?????? - ?????? ????????? ???, ????????? ?????? ?????? ???????????? ?????? ??????
    // ???, ?????? ?????? ??? ????????? ?????? ??? ????????? ?????? ?????? (????????? ?????? ???)
    // ???, ?????? ??????/?????? ??? ????????? ?????? ?????? (????????? ?????? ???)
    // ?????? ????????? ????????? ?????? ??????



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
                System.out.println("????????? ?????? aop");
            }
        }
    }
    private int getRemainSecondForTommorow() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tommorow = LocalDateTime.now().plusDays(1L).truncatedTo(ChronoUnit.DAYS);
        return (int) now.until(tommorow, ChronoUnit.SECONDS);
    }
}
