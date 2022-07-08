<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="cPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<head>
    <meta charset=UTF-8>
    <title>Title</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${cPath}/css/popup.css?v=28">
    <script src="${cPath}/js/common.js?v=10"></script>
    <script>
        initPath('${cPath}');
    </script>
    <script src="${cPath}/js/popup.js?v=41"></script>
</head>
<body onload="loadPopUp()" onresize="resizePopUP()">
    <main id="main">
        <div class="alarm-list">
            <table class="alarm-table">
                <thead>
                    <th class="alarm-class">분류</th>
                    <th class="alarm-post">게시글</th>
                    <th class="alarm-comment">댓글정보</th>
                    <th class="alarm-delete"><i class=""></i></th>
                </thead>
                <tbody>
<%--                <c:set var="" value="${alarmView.alarmList}"/>--%>
                    <c:set var="now" value="<%=new java.util.Date().getTime()%>" />

<%--                    <c:set var="now1" value="<%=new java.util.Date()%>" />--%>
<%--                    <c:set var="now2" value="${now1.time}" />--%>
<%--                    <fmt:formatDate value="${now1}" var="now1" type="date" pattern="MM-dd HH:mm:ss"/>--%>
<%--                    <c:out value="${now2}"/>--%>
<%--                    <fmt:formatDate value="${alarmView.alarmList[0].alarmDate}" var="alarmTime" type="date" pattern="MM-dd HH:mm:ss"/>--%>
<%--                    <c:set value="${alarmView.alarmList[0].alarmDate.time}" var="alarmTime"/>--%>
<%--                    <c:out value="${alarmTime}"/>--%>
<%--                    <c:out value="${(now2-alarmTime) div 1000}"/>--%>

                    <c:forEach var="alarm" items="${alarmView.alarmList}">
                        <c:set var="timeDiff" value="${(now - alarm.alarmDate.time) div 1000}"/>

<%--                        <fmt:parseNumber type="number" integerOnly="true" var="day" value="${timeDiff div (24*60*60)}"/>--%>
                        <fmt:parseNumber type="number" integerOnly="true" var="day" value="${(timeDiff div (24*60*60)) lt 1?0:timeDiff div (24*60*60)}"/>
                        <c:set var="dayRemainer" value="${timeDiff mod (24*60*60)}"/>

                        <fmt:parseNumber type="number" integerOnly="true" var="hour" value="${dayRemainer div (60*60)}"/>
                        <c:set var="hourRemainder" value="${dayRemainer mod (60*60)}"/>

                        <fmt:parseNumber type="number" integerOnly="true" var="min" value="${hourRemainder div 60}"/>
                        <fmt:parseNumber type="number" integerOnly="true" var="sec" value="${hourRemainder mod 60}"/>

                        <c:set var="day" value="${day gt 0 ? day+='일 ' : ''}"/>
                        <c:set var="hour" value="${hour gt 0 ? hour+='시간 ':''}"/>
                        <c:set var="min" value="${min gt 0 ? min+='분 ':''}"/>
                        <c:set var="sec" value="${sec gt 0 ? sec+='초 ':''}"/>
                        <c:set var="alarmTimeDiff" value="${day}${hour}${min}${sec}전"/>

                        <tr class="alarm-item">
                            <td class="alarm-info">
                                <div>
                                    <div class="alarm-type">댓글1</div>
                                    <div class="alarm-time">${alarmTimeDiff}</div>
                                </div>
                            </td>
                            <td class="alarm-post">
                                <div>
                                    <div class="alarm-title">${alarm.title}</div>
                                    <div class="alarm-board">${alarm.boardName}</div>
                                </div>
                            </td>
                            <td class="alarm-comment" onclick="">
                                <div>
                                    <div class="alarm-content" onclick="checkAlarm(${alarm.boardNo},${alarm.postNo},${alarm.commentNo})">${alarm.content}</div>
                                    <div class="alarm-nickname">${alarm.nickname}</div>
                                </div>
                            </td>
                            <td class="alarm-delete">
                                <i class="fas fa-times" onclick="eraseAlarm(this, ${alarm.alarmNo})"></i>
                            </td>
                        </tr>
                    </c:forEach>
<%--                    <script>--%>
<%--                        function confirmAlarm(url, boardNo, postNo, commentNo){--%>
<%--                            const xhr=new XMLHttpRequest();--%>
<%--                            xhr.open("GET",url, true);--%>
<%--                            xhr.responseType="json";--%>
<%--                            xhr.setRequestHeader('Content-Type', 'application/json');--%>
<%--                            xhr.send();--%>
<%--                            xhr.onload=()=>{--%>
<%--                                if(xhr.status===200){--%>

<%--                                }--%>
<%--                            }--%>
<%--                        }--%>
<%--                    </script>--%>
<%--                    <tr>--%>
<%--                        <td class="alarm-class">--%>
<%--                            <div>댓글2</div>--%>
<%--                            <div>시간차이2</div>--%>
<%--                        </td>--%>
<%--                        <td class="alarm-post">--%>
<%--                            <div>게시글제목2</div>--%>
<%--                            <div>게시판2</div>--%>
<%--                        </td>--%>
<%--                        <td class="alarm-comment">--%>
<%--                            <div>댓글내용2</div>--%>
<%--                            <div>작성자2</div>--%>
<%--                        </td>--%>
<%--                        <td class=""></td>--%>
<%--                    </tr>--%>
                </tbody>
            </table>
        </div>
        <div class="alarm-pagination">
            <c:if test="${not empty alarmView.alarmPageHandler}">
                <c:set var="aph" value="${alarmView.alarmPageHandler}"/>
            </c:if>
            <c:set var="alarmLink" value="${cPath}/alarm/list"/>
            <ul class="page-list">
                <c:if test="${aph.hasPrev}">
                    <li><a href="${alarmLink}?ap=${aph.start-1}" class=""><</a></li>
                </c:if>
                <c:forEach var="page" items="${aph.pageList}">
                    <c:set var="currentPage" value="${aph.currentPage eq page ? 'current-page':''}"/>
                    <li><a href="${alarmLink}?ap=${page}" class="${currentPage}">${page}</a></li>
                </c:forEach>
                <c:if test="${aph.hasNext}">
                    <li><a href="${alarmLink}?ap=${aph.end+1}" class="">></a></li>
                </c:if>
            </ul>
        </div>
    </main>
</body>
</html>
