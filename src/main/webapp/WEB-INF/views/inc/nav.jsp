<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="cPath" value="${pageContext.request.contextPath}"/>
<div id="gnb">
    <div class="content-container">
        <nav class="gnb">
            <ul class="nav nav-bar">
                <c:forEach var="menu" items="${menuList}">
                    <li class="dropdown">
                        <a class="">${menu.boardCategory.categoryName}</a>
                        <div>
                            <ul class="">
                                <c:forEach var="board" items="${menu.boardList}">
                                    <li><a class="dropdown-item" href="${cPath}/board/${board.boardNo}">${board.boardName}</a></li>
<%--                                    <li><a class="dropdown-item" href="">서브메뉴2</a></li>--%>
<%--                                    <li><a class="dropdown-item" href="">서브메뉴3</a></li>--%>
                                </c:forEach>

                            </ul>
                        </div>
                    </li>
                </c:forEach>

<%--                <li class="dropdown">--%>
<%--                    <a class="" href="">상위메뉴2</a>--%>
<%--                    <div>--%>
<%--                        <ul class="">--%>
<%--                            <li><a class="dropdown-item" href="">서브메뉴4</a></li>--%>
<%--                            <li><a class="dropdown-item" href="">서브메뉴5</a></li>--%>
<%--                            <li><a class="dropdown-item" href="">서브메뉴6</a></li>--%>
<%--                        </ul>--%>
<%--                    </div>--%>
<%--                </li>--%>
<%--                <li class="dropdown">--%>
<%--                    <a class="" href="">상위메뉴3</a>--%>
<%--                    <div>--%>
<%--                        <ul class="">--%>
<%--                            <li><a class="dropdown-item" href="">서브메뉴7</a></li>--%>
<%--                            <li><a class="dropdown-item" href="">서브메뉴8</a></li>--%>
<%--                            <li><a class="dropdown-item" href="">서브메뉴9</a></li>--%>
<%--                        </ul>--%>
<%--                    </div>--%>
<%--                </li>--%>
<%--                <li class="dropdown">--%>
<%--                    <a class="" href="">상위메뉴3</a>--%>
<%--                    <div>--%>
<%--                        <ul class="">--%>
<%--                            <li><a class="dropdown-item" href="">서브메뉴7</a></li>--%>
<%--                            <li><a class="dropdown-item" href="">서브메뉴8</a></li>--%>
<%--                            <li><a class="dropdown-item" href="">서브메뉴9</a></li>--%>
<%--                        </ul>--%>
<%--                    </div>--%>
<%--                </li>--%>
<%--                <li class="dropdown">--%>
<%--                    <a class="" href="">상위메뉴3</a>--%>
<%--                    <div>--%>
<%--                        <ul class="">--%>
<%--                            <li><a class="dropdown-item" href="">서브메뉴7</a></li>--%>
<%--                            <li><a class="dropdown-item" href="">서브메뉴8</a></li>--%>
<%--                            <li><a class="dropdown-item" href="">서브메뉴9</a></li>--%>
<%--                        </ul>--%>
<%--                    </div>--%>
<%--                </li>--%>
            </ul>
        </nav>
        <div class="search search-top form-wrapper">
            <form action="${cPath}/search" method="get">
                <!-- <select name="query">
                    <option value="" selected>제목</option>
                    <option value="">내용</option>
                    <option value="">제목+내용</option>
                    <option value="">작성자</option>
                </select> -->
                <input type="text" class="" name="w" id="">
                <button type="submit" class="">
                    <i class="fas fa-search"></i>
                </button>
                <!-- <input type="submit" value=""> -->
            </form>
        </div>
    </div>
</div>