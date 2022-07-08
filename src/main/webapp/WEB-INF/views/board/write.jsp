<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="cPath" value="${pageContext.request.contextPath}"/>
<main id="main">
    <div class="content-container">
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <div class="posting-box">
                    <form role="form" action="${cPath}/board/${boardNo}/post" class="posting-form" method="post"
<%--                          target="_blank"--%>
                          onsubmit="return uploadPost(this)"
                    >
                        <div>
                            <select name="postCategoryNo">
                                <c:forEach var="postCategory" items="${postCategoryList}">
                                    <c:set var="postCategoryNo" value="${postCategory.postCategoryNo}"/>
                                    <c:set var="selected" value="${postCategoryNo == 1?'selected':''}"/>
                                    <option value="${postCategoryNo}" ${selected}>${postCategory.categoryName}</option>
                                </c:forEach>
<%--                                <option value="1">잡담</option>--%>
<%--                                <option value="2">토론</option>--%>
                            </select>
                        </div>
                        <div>
                            <input type="text" name="title" id="" maxlength="100"/>
                        </div>
                        <input type="hidden" name="boardNo" value="${boardNo}">
                        <div id="content">
                        </div>
<%--                        <div class="image-upload">--%>

<%--                        </div>--%>
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
                    로그인 후에 이용해주세요
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
        setUpTiny("write");
    </script>
</main>
