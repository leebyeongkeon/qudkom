<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="cPath" value="${pageContext.request.contextPath}"/>
<header id="header">
<c:if test="${not empty memoView.category}">
<div class="memo-link">
    <ul>
        <c:set var="memoLink" value="${cPath}/memo"/>
        <c:set var="save" value="${param.save ? '?save=true':''}"/>
        <c:set var="queryString" value="${memoView.category}${save}"/>
        <c:set var="liQueryString" value="receive"/>
        <c:set var="currentCategory" value="${liQueryString eq queryString and empty param.save ? ' current-category':''}"/>
        <li class="memo-link-item${currentCategory}">
            <a href="${memoLink}/${liQueryString}">수신쪽지</a>
        </li>
        <c:set var="liQueryString" value="receive?save=true"/>
        <c:set var="currentCategory" value="${liQueryString eq queryString ? ' current-category':''}"/>
        <li class="memo-link-item${currentCategory}">
            <a href="${memoLink}/${liQueryString}">수신보관</a>
        </li>
        <c:set var="liQueryString" value="send"/>
        <c:set var="currentCategory" value="${liQueryString eq queryString and empty param.save ? ' current-category':''}"/>
        <li class="memo-link-item${currentCategory}">
            <a href="${memoLink}/${liQueryString}">발신쪽지</a>
        </li>
        <c:set var="liQueryString" value="send?save=true"/>
        <c:set var="currentCategory" value="${liQueryString eq queryString ? ' current-category':''}"/>
        <li class="memo-link-item${currentCategory}">
            <a href="${memoLink}/${liQueryString}">발신보관</a>
        </li>
        <c:set var="liQueryString" value="temp"/>
        <c:set var="currentCategory" value="${liQueryString eq queryString ? ' current-category':''}"/>
        <li class="memo-link-item${currentCategory}">
            <a href="${memoLink}/${liQueryString}">임시저장</a>
        </li>
        <c:set var="liQueryString" value="users"/>
        <c:set var="currentCategory" value="${liQueryString eq queryString ? ' current-category':''}"/>
        <li class="memo-link-item${currentCategory}">
            <a href="${cPath}/memo/users">유저검색</a>
        </li>
    </ul>
</div>
</c:if>
</header>