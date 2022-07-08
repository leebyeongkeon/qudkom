<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<main id="main">
    <c:set var="cPath" value="${pageContext.request.contextPath}"/>
    <c:set var="memoLink" value="${cPath}/memo"/>
    <c:set var="category" value="${memoView.category}"/>
    <c:set var="counterpartUser" value="${category eq 'receive'?'보낸사람':category eq 'send'?'받은사람':'받을사람'}"/>
    <c:set var="memoDateLabel" value="${category eq 'receive'?'받은시간':category eq 'send'?'보낸시간':'저장시간'}"/>
    <c:set var="save" value="${param.save ? '?save=true':''}"/>
    <c:set var="mno" value="${0}"/>
    <c:if test="${not empty memoView.memoDto}">
        <c:set var="memo" value="${memoView.memoDto}"/>
        <c:set var="mno" value="${memo.memoNo}"/>
    <div class="memo-body">
        <div>
            <div class="memo-counterpart">
                <span class="memo-label">${counterpartUser}</span>
                <span class="memo-nickname">${memo.nickname}</span>
            </div>
            <div class="memo-date">
                <fmt:formatDate var="memoDate" value="${memo.memoDate}" type="date" pattern="yyyy-MM-dd HH:mm:ss"/>
                <span class="memo-label">${memoDateLabel}</span>
                <span class="memo-date">${memoDate}</span>
            </div>
            <div class="memo-title">
                <span class="memo-label">제목</span>
                <span class="memo-title">${memo.title}</span>
            </div>
            <div class="memo-content">
                <span class="memo-label">내용</span>
                <span class="memo-content">${memo.content}</span>
            </div>
        </div>
    </div>
        <c:if test="${category eq 'temp'}">
                <div class="memo-temp memo-button">
                    <div>
                    <button onclick="">수정</button>
                    <button onclick="">삭제</button>
                    <button onclick="">전송</button>
                    </div>
                <%--                    전송예약--%>
                </div>
            </c:if>
    </c:if>
<%--    <c:if test="${not empty memoView}">--%>
    <div class="memo-count">
        <c:choose>
            <c:when test="${category ne 'temp'}">
                <span class="">
                    전체: <span class="total-count">${memoView.totalCount}</span>개, 읽지 않은 쪽지: <span class="unread-count">${memoView.unreadCount}</span>개
                </span>
            </c:when>
            <c:otherwise>
                <span class="">
                    임시저장: <span class="total-count">${memoView.totalCount}</span>개
                </span>
            </c:otherwise>
        </c:choose>
    </div>
<%--    </c:if>--%>
    <div class="memo-list">
        <table class="memo-list-table">
            <thead>
            <tr>
                <th class="memo-check"><span onclick="checkAll()"><i class="fas fa-check"></i></span></th>
                <th class="memo-read"></th>
                <th class="memo-title">제목</th>
                <th class="memo-user">${counterpartUser}</th>
                <th class="memo-date">날짜</th>
            </tr>
            </thead>
            <tbody>

            <c:if test="${not empty memoView.memoList}">
<%--                <c:set var="memoList" value="${memoList}"/>--%>
                <c:forEach var="memoItem" items="${memoView.memoList}">
                    <c:choose>
                        <c:when test="${memoItem.memoNo eq mno}">
                            <c:set var="memoIcon" value="fas fa-long-arrow-alt-right"/>
                        </c:when>
                        <c:when test="${category eq 'temp'}">
                            <c:set var="memoIcon" value=""/>
                        </c:when>
                        <c:when test="${!memoItem.opened}">
                            <c:set var="memoIcon" value="fas fa-envelope"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="memoIcon" value="far fa-envelope-open"/>
                        </c:otherwise>
                    </c:choose>
                    <fmt:formatDate var="memoDate" value="${memoItem.memoDate}" type="date" pattern="yyyy-MM-dd"/>
                    <tr>
                        <td class="memo-check">
                            <input class="memo-check-input" type="checkbox" name="" value="">
                            <input type="hidden" value="${memoItem.memoNo}">
                        </td>
                        <td class="memo-read"><i class="${memoIcon}"></i></td>
                        <td class="memo-title"><a href="${memoLink}/${category}/${memoItem.memoNo}${save}">${memoItem.title}</a></td>
                        <td class="memo-user">${memoItem.nickname}</td>
                        <td class="memo-date">${memoDate}</td>
                    </tr>
                </c:forEach>
<%--                <tr>--%>
<%--                    <td class="memo-check">--%>
<%--                        <input type="checkbox" name="" value="">--%>
<%--                    </td>--%>
<%--                    <td class="memo-read"><i class="fas fa-envelope"></i></td>--%>
<%--                    <td class="memo-title"><a href="">title01</a></td>--%>
<%--                    <td class="memo-user">user01</td>--%>
<%--                    <td class="memo-date">2022-04-01</td>--%>
<%--                </tr>--%>
<%--                <tr>--%>
<%--                    <td class="memo-check">--%>
<%--                        <input type="checkbox" name="" value="">--%>
<%--                    </td>--%>
<%--                    <td class="memo-read"><i class="far fa-envelope-open"></i></td>--%>
<%--                    <td class="memo-title"><a href="">title01</a></td>--%>
<%--                    <td class="memo-user">user01</td>--%>
<%--                    <td class="memo-date">2022-04-01</td>--%>
<%--                </tr>--%>
            </c:if>
            </tbody>
        </table>
    </div>
    <div class="memo-button">
        <div>
            <div class="refresh">
                <div>
                    <button onclick="location.reload()">새로고침</button>
                </div>
            </div>
            <div class="batch-proc">
                <c:if test="${not empty memoView.memoList}">
                    <c:set var="batchUrl" value="'${memoLink}/batch'"/>
                    <div>
                        <c:if test="${(empty param.save or !param.save) and category ne 'temp'}">
                            <button onclick="batchProcess(${batchUrl},'PATCH','${category}','${save}')">
                                쪽지보관
                            </button>
                        </c:if>

                        <button onclick="batchProcess(${batchUrl},'DELETE','${category}','${save}')">
                            쪽지삭제
                        </button>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
    <div class="memo-pagination">
        <c:if test="${not empty memoView.memoPageHandler}">
            <c:set var="mph" value="${memoView.memoPageHandler}"/>
            <ul class="page-list">
                <c:if test="${mph.hasPrev}">
                    <li><a href="${memoLink}/${category}?mp=${mph.start-1}" class=""><</a></li>
                </c:if>
                <c:forEach var="page" items="${mph.pageList}">
                    <c:set var="currentPage" value="${mph.currentPage eq page ? 'current-page':''}"/>
                    <li><a href="${memoLink}/${category}?mp=${page}" class="${currentPage}">${page}</a></li>
                </c:forEach>
                <c:if test="${mph.hasNext}">
                    <li><a href="${memoLink}/${category}?mp=${mph.end+1}" class="">></a></li>
                </c:if>
            </ul>
        </c:if>
    </div>
</main>