<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="cPath" value="${pageContext.request.contextPath}"/>
<main id="main">
    <div class="user-list">
        <ul>
            <c:forEach var="user" items="${memoView.userList}">
                <li>
                    <span onclick="openMemoWriting('${cPath}/memo/write?uno=${user.userNo}&nickname=${user.nickname}','memoWriting')">${user.nickname}(${user.id})</span>
<%--                    <a href="${pageContext.request.contextPath}/memo/write?uno=${user.userNo}&nickname=${user.nickname}">--%>
<%--                            ${user.nickname}(${user.id})--%>
<%--                    </a>--%>
                </li>
            </c:forEach>
        </ul>
    </div>
    <div class="search search-user">
        <form action="${cPath}/memo/users">
            <select name="query">
                <option value="nickname" ${param.query eq 'nickname'?'selected':''}>닉네임</option>
                <option value="id" ${param.query eq 'id'?'selected':''}>아이디</option>
            </select>
            <input type="text" name="word" value="${param.word}">
            <button>
                <i class="fas fa-search"></i>
            </button>
        </form>
    </div>
</main>