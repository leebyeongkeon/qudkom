<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<c:set var="cPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<head>
    <meta charset=UTF-8>
    <title>Title</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${cPath}/css/popup.css?v=26">
    <script src="${cPath}/js/common.js?v=10"></script>
    <script>
        setServerName('${pageContext.request.serverName}');
        initPath('${cPath}');
    </script>
    <script src="${cPath}/js/popup.js?v=41"></script>
    <script src="${cPath}/js/socket.js?51"></script>
    <script>
        function openMemoWriting(url, name) {
            const memoWriting = window.open(url, name, "width=377, height=450, top=150, left=300", "about:_blank");
            // window.close();
        }
    </script>
</head>
<%--<body onload="">--%>
<body onload="loadPopUp()" onresize="resizePopUP()">
<%--<body onload="parent.resizeTo(399,500)" onresize="parent.resizeTo(399,500)">--%>
<%--<body onload="parent.resizeTo(399,600); checkSession(${not empty sessionScope.user})" onresize="parent.resizeTo(399,600)">--%>
<%--<body onload="parent.resizeTo(415,600); checkSession(${not empty sessionScope.user})" onresize="parent.resizeTo(415,600)">--%>
    <tiles:insertAttribute name="header"/>
    <tiles:insertAttribute name="main"/>
    <script>
        function checkSession(logined){
            if(!logined){
                alert("로그인이 필요합니다.");
                window.close();
            }
        }
        // window.resizeTo(200,200);

        // function resizeWin() {
        //     myWindow.resizeTo(300, 300);
        // }
    </script>
</body>
</html>
