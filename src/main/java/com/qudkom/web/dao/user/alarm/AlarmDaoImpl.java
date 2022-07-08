package com.qudkom.web.dao.user.alarm;

import com.qudkom.web.domain.dto.extended.user.alarm.AlarmDto;
import com.qudkom.web.domain.dto.param.bundle.AlarmParamBundle;
import com.qudkom.web.domain.vo.user.alarm.Alarm;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AlarmDaoImpl implements AlarmDao {
    @Autowired
    private SqlSession session;
    private static final String namespace="com.qudkom.web.dao.user.AlarmMapper.";
    @Override
    public List<AlarmDto> selectList(AlarmParamBundle alarmParamBundle){
        return session.selectList(namespace+"selectList", alarmParamBundle);
    }

    @Override
    public int selectCount(int userNo) {
        return session.selectOne(namespace+"selectCount",userNo);
    }

    @Override
    public int selectNewCount(int userNo){
        return session.selectOne(namespace+"selectNewCount", userNo);
    }

    @Override
    public int insert(Alarm alarm) {
        return session.insert(namespace+"insert",alarm);
    }

    @Override
    public int updateList(int userNo){
        return session.update(namespace+"updateList", userNo);
    }
    @Override
    public int update(Alarm alarm){
        return session.update(namespace+"update",alarm);
    }
    @Override
    public int delete(Alarm alarm){
        return session.delete(namespace+"delete",alarm);
    }
    @Override
    public int deleteAll(int userNo){
        return session.delete(namespace+"deleteAll", userNo);
    }
}
