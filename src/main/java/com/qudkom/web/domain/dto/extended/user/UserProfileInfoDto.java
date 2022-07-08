package com.qudkom.web.domain.dto.extended.user;

import com.qudkom.web.domain.vo.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

@Data
public class UserProfileInfoDto extends User {
    public static final String USER_NO="userNo";
    public static final String NICKNAME="nickname";
    public static final String PROFILE="profile";
    public static final String EXP="exp";
    public static final String[] FIELD_NAMES={USER_NO,NICKNAME,PROFILE,EXP};



    private Integer level;
    private Integer remainderExp;
    private Integer postCount;
    private Integer commentCount;
    private Boolean autoLogin;
    private Integer newMemoCount;
    private Integer newAlarmCount;

//    private Integer 알림수
    public UserProfileInfoDto(){
    }

    public UserProfileInfoDto(User user) {
        super(user.getUserNo(), user.getId(), user.getPassword(), user.getNickname(), user.getProfile(), user.getExp(), user.getEmail(), user.getRegDate(), user.getGrade(), user.getStatus(), user.getPoint(), user.getAutoKey(), user.getAutoKeyExpiryDate());
        setLevelAndExp();
    }
    public void setLevelAndExp(){
        this.level=0;
        this.remainderExp=this.exp;
        int requiredExp=100;
        while(remainderExp>=requiredExp){
            this.remainderExp-=requiredExp;
            requiredExp+=50;
            this.level++;
        }
    }

    @Override
    public String toString() {
        return "UserProfileInfoDto{" +
                "level=" + level +
                ", remainderExp=" + remainderExp +
                ", postCount=" + postCount +
                ", commentCount=" + commentCount +
                ", autoLogin=" + autoLogin +
                ", userNo=" + userNo +
                ", id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profile='" + profile + '\'' +
                ", exp=" + exp +
                ", email='" + email + '\'' +
                ", regDate=" + regDate +
                ", grade=" + grade +
                ", status=" + status +
                ", point=" + point +
                ", autoKey='" + autoKey + '\'' +
                ", autoKeyExpiryDate=" + autoKeyExpiryDate +
                '}';
    }

//    public static Object getValueByFieldName(User user, String fieldName){
//        Object value=null;
//        Class clazz=user.getClass();
//        try {
//            Field f=clazz.getDeclaredField(fieldName);
//            value=f.get(user);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return value;

//        String methodName=new StringBuilder("get")
//                .append(Character.toUpperCase(fieldName.charAt(0)))
//                .append(fieldName.substring(1))
//                .toString();
//        try {
//            value=clazz.getDeclaredMethod(methodName).invoke(user);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//        return value;
//    }



}
