<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%--<html>--%>
<%--<head>--%>
<%--    <title>Bad Request</title>--%>
<%--</head>--%>
<%--<body>--%>
    <main id="main">
        <div class="content-container">
            <c:choose>
                <c:when test="${not empty responseDto}">
                    <c:out value="${responseDto.message}"/>
                </c:when>
                <c:otherwise>
                    Bad Request
                </c:otherwise>
            </c:choose>
        </div>
    </main>
<%--</body>--%>
<%--</html>--%>
