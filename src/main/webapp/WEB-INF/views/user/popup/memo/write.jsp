<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="cPath" value="${pageContext.request.contextPath}"/>
<main id="main">
    <c:set var="memoPath" value="${cPath}/memo"/>
    <form class="memo-write" action="${memoPath}" method="post" onsubmit="return sendMemo(this)" method="post">
        <div class="memo-receive">
            <span>보낼사람: ${param.nickname}</span>
            <input type="hidden" name="counterpartUser" value="${param.uno}">
        </div>
        <input type="hidden" name="category" value="">
        <input type="hidden" name="path" value="${memoPath}">
        <div class="memo-title">
            <input type="text" name="title">
        </div>
        <div class="memo-content">
            <textarea name="content">
            </textarea>
        </div>
        <div class="memo-submit">
            <div>
            <input type="submit" value="임시저장" onclick="setCategory('temp')">
            <input type="submit" value="전송" onclick="setCategory('send')">
            </div>
        </div>
    </form>
<%--    <script>--%>
<%--        function setCategory(category){--%>
<%--            document.querySelector("form").category.value=category;--%>
<%--        }--%>
<%--        function sendMemo(form) {--%>
<%--            if (form.title.value.length === 0) {--%>
<%--                alert("제목을 입력해야 합니다.");--%>
<%--                return false;--%>
<%--            }--%>

<%--            else if(form.content.value.length===0){--%>
<%--                alert("내용을 입력해야 합니다.");--%>
<%--                return false;--%>
<%--            }--%>
<%--            form.action=form.action+form.category.value;--%>
<%--            console.log(form.action);--%>
<%--            return true;--%>
<%--        }--%>

<%--    </script>--%>
</main>
<script>
    window.addEventListener("load", function (){
        const form=document.querySelector("form");
        form.content.value=form.content.textContent.trim();
    });

</script>

