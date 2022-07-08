<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="cPath" value="${pageContext.request.contextPath}"/>
<fmt:formatDate var="postDate" value="${board.postDto.postDate}" type="date" pattern="yyyy-MM-dd HH:mm:ss"/>
<c:set var="userNo" value="${not empty sessionScope.user?sessionScope.user.userNo:0}"/>
<c:set var="boardNo" value="${board.boardDto.boardNo}"/>
<main id="main">
    <div class="content-container">
        <c:set var="pno" value="${0}"/>
        <c:if test="${not empty board.postDto}">
            <c:set var="post" value="${board.postDto}"/>
            <c:set var="pno" value="${post.postNo}"/>
        <div class="post-container">
            <div class="title-container">
                <div>
                    <span class="category">${post.categoryName}</span>
                    <span class="post-title">${post.title}</span>
                </div>
                <div>
                    <span class="written-time">${postDate}</span>
                </div>
            </div>
            <div class="writer-container">
                <div>
                    <i class="fas fa-user"></i>
                    <span class="post-writer">${post.nickname}</span>
                </div>
                <div>
                    <span class="post-hit">조회수 ${post.hit}</span>
                    <span class="post-vote">추천 ${post.likeCount}-${post.dislikeCount}</span>
                    <span>댓글수 ${post.commentCount}</span>
                </div>
            </div>
            <div class="post-wrapper">
                ${post.content}
            </div>
            <div class="vote-container">
                <div>
                    <c:set var="postDisabled" value="${(empty sessionScope.user or userNo eq post.userNo) ? 'disabled':''}"/>
                    <c:set var="postVoteUrl" value="'${cPath}/board/${post.boardNo}/${post.postNo}/vote'"/>
                    <button class="vote-good" onclick="votePost(${post.boardNo}, ${post.postNo}, ${userNo},true,${post.userNo},${postVoteUrl})"${postDisabled}>추천
                        <span class="like-count">${post.likeCount}</span>
                    </button>
                    <button class="vote-bad" onclick="votePost(${post.boardNo}, ${post.postNo}, ${userNo},false,${post.userNo},${postVoteUrl})" ${postDisabled}>반대
                        <span class="dislike-count">${post.dislikeCount}</span>
                    </button>
                    <button class="post-report" onclick="" ${postDisabled}>신고</button>
                </div>
            </div>
            <div class="post-bottom">
                <div>
                    <c:if test="${userNo eq post.userNo}">
                        <button class="post-edit" onclick="location.href='${cPath}/board/${board.boardDto.boardNo}/${post.postNo}/edit'">수정</button>
                        <button class="post-delete" onclick="erasePost(${post.boardNo},${post.postNo},${post.userNo}, '${cPath}/board/${post.boardNo}/${post.postNo}')">삭제</button>
                    </c:if>
                </div>
                <div>
                    <c:if test="${not empty sessionScope.user}">
                        <button onclick="location.href='${cPath}/board/${boardNo}/post'">글쓰기</button>
                    </c:if>
                    <button onclick="location.href='${cPath}/board/${boardNo}'">목록</button>
                </div>
            </div>
        </div>
        <div class="post-comment">
            <c:if test="${not empty board.commentViewDto}">
<%--            <c:set var="commentList" value="${board.commentViewDto.commentList}"/>--%>
            <c:set var="commentList" value="${board.commentViewDto.commentDtoList}"/>
            <c:set var="cph" value="${board.commentViewDto.commentPageHandler}"/>
            </c:if>

            <div class="comment-box">
                <ul class="comment-list">
<%--                    <c:set var="edited" value="${comment.status eq 1 or comment.status eq 3 ? '[댓글 수정]':''}"/>--%>
                    <c:forEach var="comment" items="${commentList}">
                        <c:set var="userStatus" value="${empty sessionScope.user? 0 : ''}"/>
                        <c:set var="loginStatus" value="${empty sessionScope.user?'not-logined':userNo eq comment.userNo?'same-user':'logined'}"/>
                        <c:set var="depth" value="${comment.depth ge 3 ? 3 : comment.depth}"/>
                        <c:set var="commentDisabled" value="${empty sessionScope.user or userNo eq comment.userNo?'disabled':''}"/>
                        <li class="comment-item depth${depth} cno${comment.commentNo}">
                            <c:choose>
                                <c:when test="${comment.status lt 4}">
                                    <fmt:formatDate var="commentDate" value="${comment.commentDate}" type="date" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    <div>
<%--                                        <input type="hidden" value="${comment.commentNo}"/>--%>
                                        <div class="comment-writer-box comment-left">
                                            <div class="comment-writer">
                                                <i class="fas fa-user"></i>
                                                <span>${comment.nickname}</span>
                                            </div>
                                            <div class="${loginStatus}">
                                                <button class="comment-edit edit" onclick="openEditCommentForm(this, '${cPath}/comment/${comment.boardNo}/${comment.postNo}', ${userNo}, ${comment.boardNo}, ${comment.postNo}, ${comment.commentNo})">수정</button>
                                                <button class="comment-delete delete" onclick="eraseComment(this, '${cPath}/comment/${comment.boardNo}/${comment.postNo}', ${userNo},${comment.boardNo}, ${comment.postNo}, ${comment.commentNo}, ${comment.parentComment})">삭제</button>
                                            </div>
                                        </div>
                                        <div class="comment-content-box comment-middle">
                                            <div class="comment-content">
                                                <c:if test="${comment.depth gt 1}">
                                                    <span class="comment-parent" onclick="getCommentByNo('${cPath}/comment/${post.boardNo}/${post.postNo}',${not empty sessionScope.user},${userNo},${comment.boardNo},${comment.postNo},${comment.parentComment})">@${comment.parentCommentNickname}</span>
                                                </c:if>
                                            <span class="edited">
                                                <c:if test="${comment.status eq 1 or comment.status eq 3}">
                                                    [리플수정]
                                                </c:if>
                                            </span>
                                            <span class="comment-content">
                                                <c:out value="${comment.content}"/>
                                            </span>
                                            </div>
                                        </div>
                                        <div class="comment-info-box">
                                            <div class="comment-info">
                                                <div class="comment-time">
                                                    <span class="">${commentDate}</span>
                                                </div>
                                                <div class="comment-vote">
<%--                                                    <c:set var="" value="${<c:url/>}"/>--%>
                                                    <c:set var="commentVoteUrl" value="'${cPath}/comment/${boardNo}/${comment.postNo}/${comment.commentNo}/vote'"/>

                                                    <button class="comment-good" onclick="commentVote(this, ${commentVoteUrl}, ${boardNo},${comment.postNo},${comment.commentNo}, true, ${comment.userNo})" ${commentDisabled}>
                                                        <i class="fas fa-thumbs-up"></i><span class="like-count">${comment.likeCount}</span>
                                                    </button>
                                                    <button class="comment-bad" onclick="commentVote(this, ${commentVoteUrl}, ${boardNo},${comment.postNo},${comment.commentNo}, false, ${comment.userNo})" ${commentDisabled}>
                                                        <i class="fas fa-thumbs-down"></i><span class="dislike-count">${comment.dislikeCount}</span>
                                                    </button>
                                                </div>
                                                <div class="comment-re ${loginStatus}">
                                                    <button class="comment-report report">신고</button>
                                                    <button class="comment-reply reply" onclick="commentReply(this, '${cPath}/comment/${comment.boardNo}/${comment.postNo}', ${userNo}, ${comment.boardNo},${comment.postNo}, ${comment.commentNo},${comment.depth})">답글</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${comment.status eq 4}">
                                        <div class="erased-comment">
                                            삭제된 댓글입니다.
                                        </div>
                                    </c:if>
                                    <c:if test="${comment.status eq 6}">
                                        <div class="blinded-comment">
                                            관리자에 의해 가려진 댓글입니다.
                                        </div>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </li>
                    </c:forEach>
                </ul>
                <c:if test="${not empty param.cno}">
                    <script>
                        document.addEventListener('DOMContentLoaded',function (e){
                            moveToComment(${param.cno});
                        });
                    </script>
                </c:if>
            </div>
            <div class="comment-pagination">
                <ul>
                    <c:if test="${cph.hasPrev}">
<%--                    <li class="comment-page-item"><a onclick="commentPaging('<c:url value="/comment/${post.boardNo}/${post.postNo}"/>', ${not empty sessionScope.user}, ${userNo}, ${post.boardNo}, ${post.postNo}, ${cph.commentPageList[0]-1})" class="comment-page-link"><i--%>
                    <li class="comment-page-item"><a onclick="commentPaging('${cPath}/comment/${post.boardNo}/${post.postNo}', ${not empty sessionScope.user}, ${userNo}, ${post.boardNo}, ${post.postNo}, ${cph.start-1})" class="comment-page-link"><i
                            class="fas fa-angle-left"></i></a></li></c:if>
                    <c:forEach var="commentPage" items="${cph.pageList}">
                        <c:set var="currentPage" value="${cph.currentPage eq commentPage ? 'current-page':''}"/>
                    <li class="comment-page-item"><a onclick="commentPaging('${cPath}/comment/${post.boardNo}/${post.postNo}', ${not empty sessionScope.user}, ${userNo}, ${post.boardNo}, ${post.postNo}, ${commentPage})" class="comment-page-link ${currentPage}">${commentPage}</a></li>
                    </c:forEach>
                    <c:if test="${cph.hasNext}">
                    <li class="comment-page-item"><a onclick="commentPaging('${cPath}/comment/${post.boardNo}/${post.postNo}', ${not empty sessionScope.user}, ${userNo}, ${post.boardNo}, ${post.postNo}, ${cph.end+1})" class="comment-page-link"><i
<%--                    <li class="comment-page-item"><a onclick="commentPaging('<c:url value="/comment/${post.boardNo}/${post.postNo}"/>', ${not empty sessionScope.user}, ${userNo}, ${post.boardNo}, ${post.postNo}, ${cph.commentPageList[cph.commentPageList.size()-1]+1})" class="comment-page-link"><i--%>
                            class="fas fa-angle-right"></i></a></li></c:if>
                </ul>
            </div>


            <div class="comment-refresh">
                <button onclick="refreshComment('${cPath}/comment/${boardNo}/${post.postNo}',${not empty sessionScope.user},${userNo},${boardNo},${post.postNo})">
                    <i class="fas fa-redo"></i>
                    <span>새 댓글 10개</span>
                </button>
            </div>
            <div class="comment-writing">
                <c:if test="${not empty sessionScope.user}">
                <div class="comment-left"></div>
                <div class="comment-middle">
                    <textarea name="content" id="" cols="" rows=""></textarea>
                </div>
                <div class="comment-right">
                    <div class="">
                        <button onclick="writeComment(this, '${cPath}/comment/${post.boardNo}/${post.postNo}', ${userNo}, ${post.boardNo},${post.postNo},null, 0)">완료</button>
                    </div>
                </div>
                </c:if>
            </div>
        </div>
        </c:if>
        <div class="post-category">
            <ul>
            <c:forEach var="postCategory" items="${board.postCategoryList}">
                <li>
                    <a href="">
                    [${postCategory.categoryName}]
                    </a>
                </li>
            </c:forEach>
            </ul>
        </div>
<%--        <div class="board-guide">--%>
<%--            <a href="">[게시판운영원칙]</a>--%>
<%--            <a href="">[이용안내]</a>--%>
<%--        </div>--%>

        <table class="post-list">
            <thead>
                        <tr>
                            <th>번호</th>
<%--                            <th>--%>
<%--                                <select>--%>
<%--                                    분류--%>
<%--                                    <option value="">잡담</option>--%>
<%--                                    <option value="">스포</option>--%>
<%--                                    <option value="">토론</option>--%>
<%--                                </select>--%>
<%--                            </th>--%>
                            <th>제목</th>
                            <th>작성자</th>
                            <th>작성일</th>
                            <th>추천</th>
                            <th>조회수</th>
                        </tr>
                        </thead>
            <c:if test="${not empty board.postList}">
            <tbody>
            <c:set var="questionMarkUsed" value="${false}"/>
            <c:if test="${not empty param.p}">
                <c:set var="varPrefix" value="${questionMarkUsed?'&':'?'}"/>
                <c:set var="p" value="${varPrefix}p=${param.p}"/>
                <c:set var="questionMarkUsed" value="${true}"/>
            </c:if>
            <c:if test="${not empty param.q}">
<%--                <c:set var="query" value="&query=${param.query}"/>--%>
                <c:set var="varPrefix" value="${questionMarkUsed?'&':'?'}"/>
                <c:set var="q" value="${varPrefix}q=${param.q}"/>
                <c:set var="questionMarkUsed" value="${true}"/>
            </c:if>
            <c:if test="${not empty param.w}">
<%--                <c:set var="word" value="&word=${param.word}"/>--%>
                <c:set var="varPrefix" value="${questionMarkUsed?'&':'?'}"/>
                <c:set var="w" value="${varPrefix}w=${param.w}"/>
                <c:set var="questionMarkUsed" value="${true}"/>
            </c:if>
            <c:forEach var="postItem" items="${board.postList}">
                <c:set var="currentPost" value="${postItem.postNo eq pno?'current-post':''}"/>
            <tr class="${currentPost}">
                <td>
                    <c:choose>
                        <c:when test="${currentPost ne ''}">
                            <i class="fas fa-long-arrow-alt-right"></i>                        </c:when>
                        <c:otherwise>
                            ${postItem.postNo}
                        </c:otherwise>
                    </c:choose>
                </td>
<%--                <td>${postItem.categoryName}</td>--%>
                <c:choose>
                    <c:when test="${postItem.commentCount gt 0}">
                        <c:set var="commentCount" value="<span class='comment-count'>[${postItem.commentCount}]</span>"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="commentCount" value="${''}"/>
                    </c:otherwise>
                </c:choose>

                <td><a
                        href="${cPath}/board/${postItem.boardNo}/${postItem.postNo}${p}${q}${w}">${postItem.title}${commentCount}</a>
                </td>

                <td>${postItem.nickname}</td>
                <fmt:formatDate var="postItemDate" value="${postItem.postDate}" pattern="yyyy-MM-dd"/>
                <td>${postItemDate}</td>
                <td>${postItem.likeCount}-${postItem.dislikeCount}</td>
                <td>${postItem.hit}</td>
            </tr>
<%--            <tr>--%>
<%--                <td>10</td>--%>
<%--                <td>잡담</td>--%>
<%--                <td><a href="">dd</a></td>--%>
<%--                <td>user134343</td>--%>
<%--                <td>2022-01-10</td>--%>
<%--                <td>2200-1200</td>--%>
<%--                <td>2200000</td>--%>
<%--            </tr>--%>
            </c:forEach>
            </tbody>
            </c:if>
        </table>
        <div class="list-bottom">
            <div>
                <button onclick="location.reload()">새로고침</button>
            <c:if test="${not empty board.pageHandler}">
                <c:if test="${not empty param.q}">
                    <c:set var="q" value="&q=${param.q}"/>
                </c:if>
                <c:set var="ph" value="${board.pageHandler}"/>
<%--                <c:set var="boardNo" value="${board.boardDto.boardNo}"/>--%>
                <c:if test="${ph.start lt ph.currentPage or ph.hasPrev}">
                    <button onclick="location.href='${cPath}/board/${boardNo}?p=${ph.currentPage-1}${q}${w}'">이전페이지</button>
                </c:if>
                <c:if test="${ph.end gt ph.currentPage or ph.hasNext}">
                    <button onclick="location.href='${cPath}/board/${boardNo}?p=${ph.currentPage+1}${q}${w}'">다음페이지</button>
                </c:if>
            </c:if>
            </div>
            <div>
                <c:if test="${not empty sessionScope.user}">
                    <button onclick="location.href='${cPath}/board/${boardNo}/post'">글쓰기</button>
<%--                <a href="<c:url value="/board/${board.boardDto.boardNo}/post"/>">글쓰기</a>--%>
                </c:if>
<%--                <button onclick="">글쓰기</button>--%>
            </div>
        </div>
        <c:if test="${not empty board.pageHandler}">
<%--            <c:set var="ph" value="${board.pageHandler}"/>--%>
<%--            <c:set var="boardNo" value="${board.boardDto.boardNo}"/>--%>
        <div class="pagination">
            <ul class="">
                <c:if test="${ph.hasPrev}">
                    <li class="page-item"><a href="${cPath}/board/${boardNo}?p=1${q}${w}" class="page-link"><<</a></li>
                    <li class="page-item"><a href="${cPath}/board/${boardNo}?p=${ph.start-1}${q}${w}" class="page-link"><</a></li>
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
                <li class="${pageListClass} ${currentPage}"><a href="${cPath}/board/${boardNo}?p=${p}${q}${w}" class="page-link">${p}</a></li>
<%--                <li class="page-item"><a href="" class="page-link">2</a></li>--%>
<%--                <li class="page-item"><a href="" class="page-link">3</a></li>--%>
                </c:forEach>
                <c:if test="${ph.hasNext}">
                    <li class="page-item"><a href="${cPath}/board/${boardNo}?p=${ph.end+1}${q}${w}" class="page-link">></a></li>
                    <li class="page-item"><a href="${cPath}/board/${boardNo}?p=${ph.lastPage}${q}${w}" class="page-link">>></a></li>
                </c:if>
            </ul>
        </div>
        </c:if>
        <div class="search-bottom">
            <div class="search search-bottom form-wrapper">
                <form method="get" action="${cPath}/board/${boardNo}" onsubmit="return searchInBoard(this)">
                    <select name="q" id="" class="">
                        <option value="title" ${param.q eq 'title'?'selected':''}>제목</option>
                        <option value="content" ${param.q eq 'content'?'selected':''}>내용</option>
                        <option value="title content" ${empty param.q or param.q eq 'title content'?'selected':''}>제목+내용</option>
                        <option value="">작성자</option>
                    </select>
                    <input type="text" name="w" id="" value="${param.w}">
                    <button type="submit" class="">
                        <i class="fas fa-search"></i>
                    </button>
                </form>
                <script>
                    function searchInBoard(form){
                        let word=form.querySelector('input[name="w"]').value;
                        if(word.length<2) {
                            confirm("2자 이상 입력하세요");
                            return false;
                        }
                        return true;
                    }
                </script>
            </div>
        </div>
    </div>
</main>
