<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="cPath" value="${pageContext.request.contextPath}"/>
<main id="main">
    <div class="content-container">
        <div class="board-cards">

        <c:forEach var="boardView" items="${boardViewDtoList}">
            <c:set var="boardDto" value="${boardView.boardDto}"/>
            <c:set var="boardPath" value="${cPath}/board/${boardDto.boardNo}"/>
            <div class="board-card">
                <div class="card-head">
                    <div class="board-name">${boardDto.boardName}</div>
                    <div class="board-more"><a href="${boardPath}">더보기</a></div>
                </div>
                <table>
                    <tbody>
                    <c:set var="now" value="<%=System.currentTimeMillis()%>" />

                    <c:forEach var="postItem" items="${boardView.postList}">
                        <c:set var="timeDiff" value="${(now - postItem.postDate.time) div 1000}"/>
                        <fmt:parseNumber type="number" integerOnly="true" var="day" value="${(timeDiff div (24*60*60)) lt 1?0:timeDiff div (24*60*60)}"/>
                        <c:choose>
                            <c:when test="${day gt 0}">
                                <c:set var="postTimeDiff" value="${day+='일 전'}"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="dayRemainer" value="${timeDiff mod (24*60*60)}"/>
                                <fmt:parseNumber type="number" integerOnly="true" var="hour" value="${dayRemainer div (60*60)}"/>
                                <c:choose>
                                    <c:when test="${hour gt 0}">
                                        <c:set var="postTimeDiff" value="${hour+='시간 전'}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="hourRemainder" value="${dayRemainer mod (60*60)}"/>
                                        <fmt:parseNumber type="number" integerOnly="true" var="min" value="${hourRemainder div 60}"/>
                                        <c:choose>
                                            <c:when test="${min gt 0}">
                                                <c:set var="postTimeDiff" value="${min+='분 전'}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <fmt:parseNumber type="number" integerOnly="true" var="sec" value="${hourRemainder mod 60}"/>
                                                <c:set var="postTimeDiff" value="${sec+='초 전'}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                        <tr class="post-item">
                        <td class="post-category">
                                ${postItem.categoryName}
                        </td>
                        <td class="post-title">
                            <div>
                                <a href="${boardPath}/${postItem.postNo}">${postItem.title}</a>
                            </div>
                        </td>
                        <td class="post-time">
                                ${postTimeDiff}
                        </td>
                    </tr>
                </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:forEach>
        </div>
    </div>
</main>