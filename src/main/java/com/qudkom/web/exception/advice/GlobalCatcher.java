package com.qudkom.web.exception.advice;

import com.qudkom.web.domain.dto.util.LoadedError;
import com.qudkom.web.domain.dto.util.MessageKey;
import com.qudkom.web.domain.dto.util.ResponseDto;
import com.qudkom.web.exception.*;
import javassist.NotFoundException;
import javassist.tools.web.BadHttpRequest;
import lombok.experimental.PackagePrivate;
import org.omg.CORBA.BAD_PARAM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.management.BadAttributeValueExpException;
import java.sql.SQLException;
import java.sql.SQLTransactionRollbackException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalCatcher {
    @Autowired
    MessageSourceAccessor mac;

    @ResponseStatus(HttpStatus.MULTI_STATUS)
    @ResponseBody
    @ExceptionHandler(value = UserRegistException.class)
    public ResponseDto exceptionHandler(UserRegistException e){
        e.printStackTrace();
        List<MessageKey> keys=e.getMessageKeys();
        List<LoadedError> errors=new ArrayList<>();
        for(MessageKey messageKey:keys){
            LoadedError error=new LoadedError();
            error.setMessage(mac.getMessage(messageKey.getKey()+messageKey.getField(),messageKey.getParams(),messageKey.getDefalutMsg()));
            error.setField(messageKey.getField());
            errors.add(error);
        }
        ResponseDto responseDto = ResponseDto.builder()
                .code(0)
                .message("가입에 실패했습니다.")
//                .message(mac.getMessage(e.getClass().getName()+e.getMessage()))
                .errors(errors)
                .hasErrors(true)
                .build();
        return responseDto;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(value = SQLException.class)
    public ResponseDto exceptionHandler(SQLException e){
        e.printStackTrace();
        ResponseDto responseDto = ResponseDto.builder()
                .code(0)
                .build();
        return responseDto;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BadHttpRequest.class)
    public ModelAndView exceptionHandler(BadHttpRequest e){
        e.printStackTrace();
        ModelAndView mv=new ModelAndView("error.badRequest");
        ResponseDto responseDto=ResponseDto.builder()
                .code(0)
                .message("잘못된 요청입니다.")
                .build();
        mv.addObject("responseDto",responseDto);
        return mv;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = UnauthorizedException.class)
    public ModelAndView exceptionHandler(UnauthorizedException e){
        e.printStackTrace();
        ModelAndView mv=new ModelAndView("error.unauthorized");
        ResponseDto responseDto=ResponseDto.builder()
//                .code(0)
                .message("로그인 후에 이용해주세요.")
                .build();
        mv.addObject("responseDto",responseDto);
        //unAuth, forbidden, SQL예외 시 - refresh, 메시지 팝업, redirect
        return mv;
    }
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(value = AsyncUnauthorizedException.class)
    public ResponseDto exceptionHandler(AsyncUnauthorizedException e){
        e.printStackTrace();
        ResponseDto responseDto=ResponseDto.builder()
                .code(0)
                .message("로그인 후에 이용해주세요.")
                .build();
        //unAuth, forbidden, SQL예외 시 - refresh, 메시지 팝업, redirect
        return responseDto;
    }
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(value = AsyncForbiddenException.class)
    public ResponseDto exceptionHandler(AsyncForbiddenException e){
        e.printStackTrace();
        ResponseDto responseDto=ResponseDto.builder()
                .code(0)
                .message("권한이 없습니다.")
                .build();
        return responseDto;
    }
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = ForbiddenException.class)
    public ModelAndView exceptionHandler(ForbiddenException e){
        e.printStackTrace();
        ModelAndView mv=new ModelAndView("error.forbidden");
        ResponseDto responseDto=ResponseDto.builder()
                .code(0)
                .message("권한이 없습니다.")
                .build();
        mv.addObject("responseDto",responseDto);
        return mv;
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NotFoundException.class)
    public ModelAndView exceptionHandler(NotFoundException e){
        e.printStackTrace();
        ModelAndView mv=new ModelAndView("error.notFound");
        ResponseDto responseDto=ResponseDto.builder()
                .code(0)
                .message("해당 페이지가 존재하지 않습니다.")
                .build();
        mv.addObject(responseDto);
        return mv;
    }
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(value = Exception.class)
//    public ModelAndView exceptionHandler(Exception e){
//        ModelAndView mv=new ModelAndView("error.internalServerError");
//        ResponseDto responseDto=ResponseDto.builder()
//                .code(0)
//                .message("서버 에러입니다.")
//                .build();
//        mv.addObject("responseDto",responseDto);
//        return mv;
//    }

//    @ResponseBody
//    @ExceptionHandler(value = RefreshRequiredException.class)
//    public ResponseEntity<ResponseDto> exceptionHandler(RefreshRequiredException e) {
//        e.printStackTrace();
//        ResponseDto responseDto=ResponseDto.builder()
//                .code(2)
//                .message("페이지를 새로고침합니다.")
//                .build();
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }

    @ResponseBody
    @ExceptionHandler(value=AlreadyVotedException.class)
    public ResponseEntity<ResponseDto> exceptionHandler(AlreadyVotedException e){
        e.printStackTrace();
        ResponseDto responseDto=ResponseDto.builder()
                .code(0)
                .message("추천 혹은 반대를 취소하시겠습니까?")
                .build();
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }
}
