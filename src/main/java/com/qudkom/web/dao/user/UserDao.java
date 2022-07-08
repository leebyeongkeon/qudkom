package com.qudkom.web.dao.user;

import com.qudkom.web.domain.dto.extended.user.UserProfileInfoDto;
import com.qudkom.web.domain.dto.param.Search;
import com.qudkom.web.domain.vo.user.User;

import java.util.List;

public interface UserDao {
    //    private SqlSession session;
//    User selectUserForLogin(String id);
    UserProfileInfoDto selectUserForLogin(String id);

    User selectUserByUserNo(Integer userNo);

    int insertUser(User user);

    int updateUser(User user);

    int deleteUser(User user);

    boolean selectIdExists(String id);

    boolean selectNicknameExists(String nickname);

    boolean selectEmailExists(String email);

    UserProfileInfoDto selectUserByAutoKey(String autoKey);

    int updateUserAutoKey(User user);

    int updateUserAutoKeyInvalid(int userNo);

    List<User> selectList(Search search);

//    int insertUserMemoInfo(int userNo);
}
