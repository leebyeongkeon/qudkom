package com.qudkom.web.domain.dto.extended.user;

import com.qudkom.web.domain.dto.util.MessageKey;
import com.qudkom.web.domain.vo.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

//가입 정보, 조회 정보 따로 정의하고 경험치를 레벨로 환산해서 조회 정보에 추가하기?
public class UserRegisterInfoDto extends User {
    public static final Integer[] ID_LENGTH={4,10};
    public static final Integer[] PASSWORD_LENGTH={8,20};
    public static final Integer[] NICKNAME_LENGTH={2,7};
    public static final String REQUIRED_MESSAGE="필수 항목입니다.";
    public static final String INVALID_LENGTH_MESSAGE = "부적절한 길이입니다.";
    public static final String INVALID_FORMAT_MESSAGE = "잘못된 형식입니다.";
    public static final String NOT_SAME_MESSAGE = "일치하지 않습니다.";
    public static final String NON_CERIFIED_MESSAGE = "인증되지 않았습니다.";

    public static final String REQUIRED_KEY="required.";
    public static final String INVALID_LENGTH_KEY="invalidLength.";
    public static final String INVALID_FORMAT_KEY="invalidFormat.";
    public static final String NOT_SAME_KEY="notSame.";
    public static final String NON_CERIFIED_KEY="nonCerified.";

    public static final String ID_FIELD_NAME="id";
    public static final String PASSWORD_FIELD_NAME="password";
    public static final String CHECKPWD_FIELD_NAME="checkPwd";
    public static final String NICKNAME_FIELD_NAME="nickname";
    public static final String EMAIL_FIELD_NAME="email";
    public static final String CERIFCODE_FIELD_NAME ="cerifCode";

    @Getter @Setter
    private String cerifCode;
    @Getter @Setter
    private String checkPwd;

    private boolean checkIdNotEmpty(){
        if(id==null || id.equals(""))
            return false;
        return true;
    }
    private boolean checkIdCerified(HttpSession session){
        String cerifiedId=(String) session.getAttribute(ID_FIELD_NAME);
        if(cerifiedId==null || !cerifiedId.equals(id))
            return false;
        return true;
    }
    private boolean checkPasswordNotEmpty(){
        if(password==null || password.equals(""))
            return false;
        return true;
    }
    private boolean checkNicknameNotEmpty(){
        if(nickname==null || nickname.equals(""))
            return false;
        return true;
    }
    private boolean checkEmailNotEmpty() {
        if(email==null || email.equals(""))
            return false;
        return true;
    }
    private boolean checkIdLength(){
        if(id.length()<ID_LENGTH[0] || id.length()>ID_LENGTH[1])
            return false;
        return true;
    }
    private boolean checkPasswordLength(){
        if(password.length()<PASSWORD_LENGTH[0] || password.length()>PASSWORD_LENGTH[1])
            return false;
        return true;
    }
    private boolean checkCheckPwdNotEmpty() {
        if(checkPwd==null || checkPwd.equals(""))
            return false;
        return true;
    }
    private boolean checkPasswordSame(){
        return checkPwd.equals(password);
    }
    private boolean checkNicknameLength(){
        if(nickname.length()<NICKNAME_LENGTH[0] || nickname.length()>NICKNAME_LENGTH[1])
            return false;
        return true;
    }
    private boolean checkNicknameCerified(HttpSession session){
        String cerifiedNickname=(String) session.getAttribute(NICKNAME_FIELD_NAME);
        if(cerifiedNickname==null || !cerifiedNickname.equals(nickname))
            return false;
        return true;
    }
    private boolean checkEmailFormat(){
        if(!email.contains("@"))
            return false;
        return true;
    }
    private boolean checkEmailCerified(HttpSession session){
        String cerifiedEmail=(String) session.getAttribute(EMAIL_FIELD_NAME);
        if(cerifiedEmail==null || !email.equals(cerifiedEmail))
            return false;
        return true;
    }
    private boolean checkCerifCodeNotEmpty(){
        if(cerifCode==null || cerifCode.equals(""))
            return false;
        return true;
    }
//    private boolean checkSessionCerifCode(HttpSession session){
//        String cerifCode=(String)session.getAttribute(CERIFCODE_FIELD_NAME);
//        if(cerifCode==null)
//            return false;
//        return true;
//    }
    public boolean checkCerifCodeSame(HttpSession session){
        String cerifCode=(String)session.getAttribute(CERIFCODE_FIELD_NAME);
        if(!cerifCode.equals(this.cerifCode))
            return false;
        return true;
    }

    @Autowired
    MessageSourceAccessor mac;

    public List<MessageKey> validate(HttpSession session){
        List<MessageKey> list=new ArrayList<>();
        MessageKey messageKey;
        // 아이디 검사 - 전송받은 아이디가 비어있거나, 길이가 부적절하거나,
        // 미리 인증받지 않은 아이디이거나, 인증받은 아이디와 다를 경우
        if(!checkIdNotEmpty()) {
            messageKey=MessageKey.builder()
                    .key(REQUIRED_KEY)
                    .field(ID_FIELD_NAME)
                    .defalutMsg(REQUIRED_MESSAGE)
                    .params(null)
                    .build();
            list.add(messageKey);
        }
        else if(!checkIdLength()){
            messageKey=MessageKey.builder()
                    .key(INVALID_LENGTH_KEY)
                    .field(ID_FIELD_NAME)
                    .defalutMsg(INVALID_LENGTH_MESSAGE)
                    .params(ID_LENGTH)
                    .build();
            list.add(messageKey);
        }
        else if(!checkIdCerified(session)){
            messageKey=MessageKey.builder()
                    .key(NON_CERIFIED_KEY)
                    .field(ID_FIELD_NAME)
                    .defalutMsg(NON_CERIFIED_MESSAGE)
                    .params(null)
                    .build();
            list.add(messageKey);
        }

        // 비밀번호 검사 - 전송받은 비밀번호가 비어있거나, 길이가 부적절할 경우
        if(!checkPasswordNotEmpty()){
            messageKey=MessageKey.builder()
                    .key(REQUIRED_KEY)
                    .field(PASSWORD_FIELD_NAME)
                    .defalutMsg(REQUIRED_MESSAGE)
                    .params(null)
                    .build();
            list.add(messageKey);
        }
        else if(!checkPasswordLength()){
            messageKey=MessageKey.builder()
                    .key(INVALID_LENGTH_KEY)
                    .field(PASSWORD_FIELD_NAME)
                    .defalutMsg(INVALID_LENGTH_MESSAGE)
                    .params(PASSWORD_LENGTH)
                    .build();
            list.add(messageKey);
        }
        // 비번확인 검사 - 전송받은 비밀번호 확인 필드가 비어있거나, 비밀번호 필드와 일치하지 않을 경우
        if(!checkCheckPwdNotEmpty()){
            messageKey=MessageKey.builder()
                    .key(REQUIRED_KEY)
                    .field(CHECKPWD_FIELD_NAME)
                    .defalutMsg(REQUIRED_MESSAGE)
                    .params(null)
                    .build();
            list.add(messageKey);
        }
        else if(!checkPasswordSame()){
            messageKey=MessageKey.builder()
                    .key(NOT_SAME_KEY)
                    .field(CHECKPWD_FIELD_NAME)
                    .defalutMsg(NOT_SAME_MESSAGE)
                    .params(null)
                    .build();
            list.add(messageKey);
        }
        // 닉네임 검사 - 전송받은 닉네임이 비어있거나, 길이가 부적절하거나,
        // 미리 인증받지 않은 닉네임이거나, 인증받은 닉네임과 다를 경우
        if(!checkNicknameNotEmpty()){
            messageKey=MessageKey.builder()
                    .key(REQUIRED_KEY)
                    .field(NICKNAME_FIELD_NAME)
                    .defalutMsg(REQUIRED_MESSAGE)
                    .params(null)
                    .build();
            list.add(messageKey);
        }
        else if(!checkNicknameLength()){
            messageKey=MessageKey.builder()
                    .key(INVALID_LENGTH_KEY)
                    .field(NICKNAME_FIELD_NAME)
                    .defalutMsg(INVALID_LENGTH_MESSAGE)
                    .params(NICKNAME_LENGTH)
                    .build();
            list.add(messageKey);
        }
        else if(!checkNicknameCerified(session)){
            messageKey=MessageKey.builder()
                    .key(NON_CERIFIED_KEY)
                    .field(NICKNAME_FIELD_NAME)
                    .defalutMsg(NON_CERIFIED_MESSAGE)
                    .params(null)
                    .build();
            list.add(messageKey);
        }
        // 이메일 검사 - 전송받은 이메일이 비어있거나, 형식이 부적절하거나,
        // 인증코드를 보내지 않았거나, 인증코드를 보냈던 이메일과 다를 경우
        if(!checkEmailNotEmpty()){
            messageKey=MessageKey.builder()
                    .key(REQUIRED_KEY)
                    .field(EMAIL_FIELD_NAME)
                    .defalutMsg(REQUIRED_MESSAGE)
                    .params(null)
                    .build();
            list.add(messageKey);
        }
        else if(!checkEmailFormat()){
            messageKey=MessageKey.builder()
                    .key(INVALID_FORMAT_KEY)
                    .field(EMAIL_FIELD_NAME)
                    .defalutMsg(INVALID_FORMAT_MESSAGE)
                    .params(null)
                    .build();
            list.add(messageKey);
        }
        else if(!checkEmailCerified(session)){
            messageKey=MessageKey.builder()
                    .key(NON_CERIFIED_KEY)
                    .field(EMAIL_FIELD_NAME)
                    .defalutMsg(NON_CERIFIED_MESSAGE)
                    .params(null)
                    .build();
            list.add(messageKey);
        }
        // 인증코드 검사 - 전송받은 인증코드가 비어있거나
        // 미리 인증을 하지 않았거나, 인증받았던 코드와 다를 경우
        if(!checkCerifCodeNotEmpty()){
            messageKey=MessageKey.builder()
                    .key(REQUIRED_KEY)
                    .field(CERIFCODE_FIELD_NAME)
                    .defalutMsg(REQUIRED_MESSAGE)
                    .params(null)
                    .build();
            list.add(messageKey);
        }
//        else if(!checkSessionCerifCode(session)){
//            messageKey=MessageKey.builder()
//                    .key(NON_CERIFIED_KEY)
//                    .field(CERIFCODE_FIELD_NAME)
//                    .defalutMsg(NON_CERIFIED_MESSAGE)
//                    .params(null)
//                    .build();
//            list.add(messageKey);
//        }
        else if(!checkCerifCodeSame(session)){
            messageKey=MessageKey.builder()
                    .key(NOT_SAME_KEY)
                    .field(CERIFCODE_FIELD_NAME)
                    .defalutMsg(NOT_SAME_MESSAGE)
                    .params(null)
                    .build();
            list.add(messageKey);
        }
        return list;
    }

    @Override
    public String toString() {
        return "UserInfoDto{" +
                "cerifCode='" + cerifCode + '\'' +
                ", checkPwd='" + checkPwd + '\'' +
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
                '}';
    }
//    public static User toUser(UserInfoDto userInfoDto){
//        return User.builder().
//                userNo(userInfoDto.getUserNo())
//                .id(userInfoDto.getId())
//                .password(userInfoDto.getPassword())
//                .nickname(userInfoDto.getNickname())
//                .regDate(userInfoDto.getRegDate())
//                .grade(userInfoDto.getGrade())
//                .exp(userInfoDto.getExp())
//                .status(userInfoDto.getStatus())
//                .email(userInfoDto.getEmail())
//                .point(userInfoDto.getPoint())
//                .profile(userInfoDto.getProfile())
//                .build();
//    }
}
