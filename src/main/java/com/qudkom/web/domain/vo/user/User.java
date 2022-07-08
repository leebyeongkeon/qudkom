package com.qudkom.web.domain.vo.user;

import lombok.*;

import java.util.Date;
import java.util.Objects;

@NoArgsConstructor
//@Getter@Setter
@Data
public class User {

    protected Integer userNo;
    protected String id;
    protected String password;

    protected String nickname;
    protected String profile;
    protected Integer exp;

    protected String email;
    protected Date regDate;
    protected Integer grade;
    protected Integer status;
    protected Integer point;

    protected String autoKey;
    protected Date autoKeyExpiryDate;

    @Builder
    public User(Integer userNo, String id, String password, String nickname, String profile, Integer exp, String email, Date regDate, Integer grade, Integer status, Integer point, String autoKey, Date autoKeyExpiryDate) {
        this.userNo = userNo;
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.profile = profile;
        this.exp = exp;
        this.email = email;
        this.regDate = regDate;
        this.grade = grade;
        this.status = status;
        this.point = point;
        this.autoKey = autoKey;
        this.autoKeyExpiryDate = autoKeyExpiryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userNo.equals(user.userNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userNo);
    }

    @Override
    public String toString() {
        return "User{" +
                "userNo=" + userNo +
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

//    public User toUserEntity(UserDto userDto){
//        return User.builder()
//                .userNo(userDto.getUserNo())
//                .password(userDto.getPassword())
//                .nickname(userDto.getNickname())
//                .exp(userDto.getExp())
//                .profile(userDto.getProfile())
//                .status(userDto.getStatus())
//                .email(userDto.getEmail())
//                .grade(userDto.getGrade())
//                .regDate(userDto.getRegDate())
//                .id(userDto.getId())
//                .build();
//    }
}
