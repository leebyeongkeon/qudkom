<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="cPath" value="${pageContext.request.contextPath}"/>
<header id="header">
    <div class="content-container">
        <div class="header-content">
            <div class="logo-wrapper">
                <a href="${cPath eq ''?'/':cPath}">
                    <img src="${cPath}/images/google.png" alt="로고이미지">
                </a>
            </div>

            <!-- 비회원 -->
            <div class="user-box">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <script>
                            setServerName('${pageContext.request.serverName}');
                            socketInit();
                        </script>
                        <c:set var="user" value="${sessionScope.user}"/>
                        <div class="user-achieve">
                            <span class="user-level">${user.level}level</span>
                            <span class="user-point">${user.point}point</span>
                        </div>
                        <div class="login-menu">
                            <span class="user-nickname">${user.nickname}</span><span>님</span>
                            <button class="user-alarm" onclick="openNewPopUp('${cPath}/alarm/list','alarm',this)">알림
                                <c:if test="${user.newAlarmCount gt 0}">
                                    <span class="alarm-new new-count">${user.newAlarmCount}</span>
                                </c:if>
                            </button>
                            <button class="user-memo" onclick="openNewPopUp('${cPath}/memo/receive','memo',this)">쪽지
                                <c:if test="${user.newMemoCount gt 0}">
                                    <span class="memo-new new-count">${user.newMemoCount}</span>
                                </c:if>
                            </button>
                            <script>
                                function openNewPopUp(url, name, button) {
                                    const myWindow = window.open(url, name, "width=377, height=450, top=150, left=300, resizable=no");
                                    if(myWindow){
                                        const newCount=button.querySelector("span.new-count");
                                        if(newCount) newCount.remove();
                                    }
                                }
                            </script>
                            <button class="user-logout" onclick="logout('${cPath}/user/logout')">로그아웃</button>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <ul class="user-menu">
                            <li><a href="">아이디찾기</a></li>
                            <li><a href="">비밀번호찾기</a></li>
                            <li><a href="${cPath}/user/register">회원가입</a></li>
                        </ul>
                        <div class="login-form form-wrapper">
                            <form class="login-form" action="${cPath}/user/login" onsubmit="return login(this)">
                                <div class="input-wrapper">
                                    <input type="text" name="id" id="" placeholder="아이디">
                                </div>
                                <div class="input-wrapper">
                                    <input type="password" name="password" id="" placeholder="비밀번호">
                                </div>
                                <div class="auto-login">
                                    <input type="checkbox" name="autoLogin" id="auto-login">
                                    <label for="auto-login">자동로그인</label>
                                </div>
                                <div>
                                    <input type="submit" class="login-btn" value="로그인">
                                </div>
                            </form>
                        </div>
                    </c:otherwise>
                </c:choose>
<%--                <script src="<c:url value='/js/register.js?v=19'/>"></script>--%>
            </div>
            <!-- 비회원 -->
            <!-- 회원 -->
            <!-- <div class="user-box">
                <ul>
                    <li>이병건님</li>
                    <li>개인정보</li>
                    <li>작성글</li>
                    <li>댓글</li>
                    <li>알림</li>
                    <li>쪽지</li>
                </ul>
                <button>로그아웃</button>
            </div> -->
            <!-- 회원 -->
        </div>
    </div>
</header>