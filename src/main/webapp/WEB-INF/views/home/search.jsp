<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="cPath" value="${pageContext.request.contextPath}"/>
<main id="main">
    <div class="content-container">
        <div>
            <ul class="search-result">
                <c:forEach var="postItem" items="${boardView.postList}">
                    <fmt:formatDate var="postDate" value="${postItem.postDate}" type="date" pattern="yyyy-MM-dd HH:mm"/>
                    <c:set var="postUrl" value="${cPath}/board/${postItem.boardNo}/${postItem.postNo}"/>
                    <li class="post-item" onclick="location.href='${postUrl}'">
                        <div>
                            <div class="post-title">
                                ${postItem.title}
                            </div>
                            <div class="post-content">
                                ${postItem.content}
                            </div>
                            <div class="post-info">
                                <span class="board-name">${postItem.boardName} 게시판</span>
                                <span class="post-date">${postDate}</span>
                            </div>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <div class="pagination">
            <ul class="">
                <c:set var="ph" value="${boardView.pageHandler}"/>
                <c:set var="searchUrl" value="${cPath}/search?w=${param.w}"/>
                <c:if test="${ph.hasPrev}">
                    <li class="page-item"><a href="${searchUrl}&p=1" class="page-link"><<</a></li>
                    <li class="page-item"><a href="${searchUrl}&p=${ph.start-1}" class="page-link"><</a></li>
                </c:if>
                <c:forEach var="p" items="${ph.pageList}" >
                    <c:choose>
                        <c:when test="${fn:length(ph.pageList) eq 1}">
                            <c:set var="pageListClass" value="${'page-item one-page'}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="pageListClass" value="${'page-item'}"/>
                        </c:otherwise>
                    </c:choose>
                    <c:set var="currentPage" value="${p eq ph.currentPage ? 'current-page':''}"/>
                    <li class="${pageListClass} ${currentPage}"><a href="${searchUrl}&p=${p}" class="page-link">${p}</a></li>
                </c:forEach>
                <c:if test="${ph.hasNext}">
                    <li class="page-item"><a href="${searchUrl}&p=${ph.end+1}" class="page-link">></a></li>
                    <li class="page-item"><a href="${searchUrl}&p=${ph.lastPage}" class="page-link">>></a></li>
                </c:if>
            </ul>
        </div>
    </div>
</main>