package com.qudkom.web.dao.user;

import com.qudkom.web.domain.dto.extended.user.UserProfileInfoDto;
import com.qudkom.web.domain.dto.param.Search;
import com.qudkom.web.domain.vo.user.User;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private SqlSession session;
    private static final String namespace="com.qudkom.web.dao.user.UserMapper.";
    @Override
    public UserProfileInfoDto selectUserForLogin(String id){
        return session.selectOne(namespace+"selectUserForLogin", id);
    }
    @Override
    public User selectUserByUserNo(Integer userNo){
        return null;
    }
    @Override
    public int insertUser(User user){
        System.out.println("user가입 = " + user);
        return session.insert(namespace+"insert",user);
    }
    @Override
    public int updateUser(User user){
        return session.update(namespace+"update",user);
    }
    @Override
    public int deleteUser(User user){
        return session.delete(namespace+"delete",user);
    }

    @Override
    public boolean selectIdExists(String id) {
        return session.selectOne(namespace+"selectIdExists",id);
    }
    @Override
    public boolean selectNicknameExists(String nickname){
        return session.selectOne(namespace+"selectNicknameExists",nickname);
    }

    @Override
    public boolean selectEmailExists(String email) {
        return session.selectOne(namespace+"selectEmailExists",email);
    }

    @Override
    public UserProfileInfoDto selectUserByAutoKey(String autoKey) {
        return session.selectOne(namespace+"selectUserByAutoKey",autoKey);
    }
    @Override
    public int updateUserAutoKey(User user){
        return session.update(namespace+"updateUserAutoKey",user);
    }

    @Override
    public int updateUserAutoKeyInvalid(int userNo) {
        return session.update(namespace+"updateUserAutoKeyInvalid", userNo);
    }

    @Override
    public List<User> selectList(Search search) {
        return session.selectList(namespace+"selectList", search);
    }

//    @Override
//    public int insertUserMemoInfo(int userNo) {
//        return session.insert(namespace+"insertUserMemoInfo",userNo);
//    }

}
