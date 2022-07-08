package com.qudkom.web.service.user;

import com.qudkom.web.domain.dto.extended.user.UserProfileInfoDto;
import com.qudkom.web.domain.dto.extended.user.UserRegisterInfoDto;
import com.qudkom.web.domain.dto.param.Search;
import com.qudkom.web.domain.dto.util.ResponseDto;
import com.qudkom.web.domain.vo.user.User;
import com.qudkom.web.exception.UserRegistException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

public interface UserService {
    ResponseDto userLogin(UserProfileInfoDto userProfileInfoDto, HttpServletRequest request, HttpServletResponse response);

//    void signUp(User user) throws SQLException;
    int signUp(User user) throws SQLException;

    ResponseDto editUserProfile(User user);

    ResponseDto signOut(User user);

//    ResponseDto checkId(String id, HttpSession session);
    boolean checkId(String id);

//    ResponseDto checkNickname(String nickname, HttpSession session);
    boolean checkNickname(String nickname);

    boolean checkEmail(String email);

//    ResponseDto cerifyMail(String cerifCode, HttpSession session);

//    ResponseDto sendMail(String email, HttpSession session);

    void validateUser(UserRegisterInfoDto userRegisterInfoDto, HttpSession session) throws UserRegistException;

    UserProfileInfoDto getUserProfile(int userNo);

    UserProfileInfoDto getUserProfile(String autoKey);

    void invalidateAutoKey(int userNo) throws Exception;

    int updateUserAutoKey(User user);

//    List<User> searchUser(QueryParamDto queryParamDto);
    List<User> searchUser(Search search);
}
