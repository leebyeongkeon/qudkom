<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="cPath" value="${pageContext.request.contextPath}"/>
<main id="main">
    <div class="content-container">
    <div class="reg-box-wrapper">
        <div class="reg-box">
            <h4>회원가입</h4>
<%--            <form:form modelAttribute="userInfoDto" cssClass="reg-form" onsubmit="return submitRegister(this)">--%>
            <form action="${cPath}/user/register" class="reg-form" onsubmit="return submitRegister(this)">
                <div>
                    <div>
                        <label for="reg-id">
                            아이디
                        </label>
                    </div>
                    <div>
                        <input type="text" name="id" id="reg-id" placeholder="숫자, 알파벳 조합 4~10자"
                               maxlength="10" minlength="2" required>
                        <button type="button" onclick="checkId(this)">중복검사</button>
                    </div>
                    <span class=""></span>
                </div>
<%--                <form:errors path="id" cssClass="error-message"/>--%>
                <div>
                    <div>
                        <label for="reg-pwd">
                            비밀번호
                        </label>
                    </div>
                    <div>
                        <input type="password" name="password" id="reg-pwd" placeholder="특수문자, 숫자, 알파벳 조합 8~15자" maxlength="15" minlength="8" required>
                    </div>
                    <span class=""></span>

                </div>
<%--                <form:errors path="password" cssClass="error-message"/>--%>
                <div>
                    <div>
                        <label for="check-pwd">
                            비밀번호 확인
                        </label>
                    </div>
                    <div>
                        <input type="password" name="checkPwd" id="check-pwd" placeholder="다시 입력해주세요" maxlength="15" minlength="8" required>
                    </div>
                    <span class=""></span>

                </div>
<%--                <form:errors path="checkPwd" cssClass="error-message"/>--%>
                <div>
                    <div>
                        <label for="reg-nick">
                            닉네임<br>
                        </label>
                    </div>
                    <div>
                        <input type="text" name="nickname" id="reg-nick" placeholder="2~6자" required>
                        <button type="button" onclick="checkNickname(this)">중복검사</button>
                    </div>
                    <span class=""></span>

                </div>
<%--                <form:errors path="nickname" cssClass="error-message"/>--%>
                <div>
                    <div>
                        <label for="reg-email">
                            이메일 주소
                        </label>
                    </div>
                    <div>
                        <input type="email" name="email" id="reg-email" placeholder="name@example.com" required>
                        <button type="button" onclick="sendMail(this)">코드전송</button>
                    </div>
                    <span class=""></span>

                </div>
<%--                <form:errors path="email" cssClass="error-message"/>--%>
                <div>
                    <div>
                        <label for="cerifi-email">
                            이메일 인증코드
                        </label>
                    </div>
                    <div>
                        <input type="text" name="cerifCode" id="cerifi-email" required>
                        <button type="button" onclick="cerifyMail(this)">코드확인</button>
                    </div>
                    <span class=""></span>

                </div>
<%--                <form:errors path="cerifCode" cssClass="error-message"/>--%>
                <div>
                    <div>
                        <button>취소</button>
                        <button type="submit">완료</button>
                    </div>
                </div>
            </form>
<%--            </form:form>--%>
        </div>
    </div>
    </div>
<%--    <script src="<c:url value='/js/register.js?v=07'/>"></script>--%>
</main>