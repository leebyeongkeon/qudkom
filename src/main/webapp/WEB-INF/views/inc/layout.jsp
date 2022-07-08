<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<c:set var="cPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset=UTF-8>
    <title>Title</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" rel="stylesheet">
<%--    <link rel="stylesheet" href="<c:url value='/css/style.css?v=46'/>">--%>
    <link rel="stylesheet" href="${cPath}/css/style.css?v=46">
<%--    <script src="<c:url value='/js/common.js?v=09'/>"></script>--%>
    <script src="${cPath}/js/common.js?v=10"></script>
    <script>
        initPath('${cPath}');
    </script>
    <script src="${cPath}/js/post.js?v=90"></script>
    <script src="${cPath}/js/comment.js?v=11"></script>
    <script src="${cPath}/js/register.js?v=23"></script>
    <script src="${cPath}/js/socket.js?v=51"></script>

</head>
<body>
    <tiles:insertAttribute name="header"/>
    <tiles:insertAttribute name="nav"/>
    <tiles:insertAttribute name="ad"/>
    <tiles:insertAttribute name="main"/>
    <tiles:insertAttribute name="footer"/>
</body>
</html>
