<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="cPath" value="${pageContext.request.contextPath}"/>
<main id="main">
    <div class="content-container">
        <c:choose>
            <c:when test="${not empty sessionScope.user and sessionScope.user.userNo eq post.userNo}">
                <div class="posting-box">
                    <form action="${cPath}/board/${post.boardNo}/${post.postNo}" class="posting-form" onsubmit="return editPost(this)">
<%--                        <c:set var="post" value="${post}"/>--%>
<%--                        <input type="hidden" name="boardNo" value="${requestScope.boardNo}"/>--%>
                        <div>
                            <select name="postCategoryNo">
                                <c:forEach var="postCategory" items="${postCategoryList}">
                                    <c:set var="postCategoryNo" value="${postCategory.postCategoryNo}"/>
                                    <c:set var="selected" value="${postCategoryNo == post.postCategoryNo}?'selected':''}"/>
                                    <option value="${postCategoryNo}" ${selected}>${postCategory.categoryName}</option>
                                </c:forEach>
<%--                                <option value="1">잡담</option>--%>
<%--                                <option value="2">토론</option>--%>
                            </select>
                        </div>
                        <div>
                            <input type="text" name="title" id="" maxlength="100" value="<c:out value="${post.title}"/>"/>
                        </div>
                        <input type="hidden" name="boardNo" value="${post.boardNo}">
                        <input type="hidden" name="postNo" value="${post.postNo}">
                        <input type="hidden" name="userNo" value="${post.userNo}">
<%--                        <input type="hidden" name="_method" value="PATCH">--%>
                        <div id="content">
                        </div>
                        <div>
                            <input type="button" value="취소하기" onclick="history.back()">
<%--                            <button type="button" onclick="history.back()">취소하기</button>--%>
                            <input type="submit" value="작성완료"/>
                        </div>
                    </form>
                </div>
            </c:when>
            <c:otherwise>
                <div>
                    잘못된 접근입니다.
                </div>
            </c:otherwise>
        </c:choose>
<%--        <div class="posting-box">--%>
<%--            <form action="" class="posting-form">--%>
<%--                <div>--%>
<%--                    <select name="" id="">--%>
<%--                        <option value="">잡담</option>--%>
<%--                        <option value="">토론</option>--%>
<%--                        <option value=""></option>--%>
<%--                    </select>--%>
<%--                </div>--%>
<%--                <div>--%>
<%--                    <input type="text" name="title" id="" maxlength="50"/>--%>
<%--                </div>--%>
<%--                <div id="editor">--%>
<%--                </div>--%>
<%--                <div>--%>
<%--                    <input type="submit" value="취소하기">--%>
<%--                    <input type="submit" value="작성완료">--%>
<%--                </div>--%>
<%--            </form>--%>
<%--        </div>--%>
    </div>
    <script src='https://cdn.tiny.cloud/1/wtcppmq06alok5ur9weohv8j9s9v9pdl5x3nf7xslroadyhm/tinymce/5/tinymce.min.js'
            referrerpolicy="origin"></script>
<%--    <script src="<c:url value='/js/tinymce.js?v=80'/>"></script>--%>
    <script src="${cPath}/js/tinymce.js?v=80"></script>
    <script>
        setUpTiny("edit",'${post.content}');
    </script>
<%--    <script src="<c:url value='/js/post.js?v=13'/>"></script>--%>
</main>
