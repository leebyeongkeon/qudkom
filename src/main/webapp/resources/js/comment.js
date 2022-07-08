const extraOffset=50;
function commentPaging(url, logined, userNo, boardNo, postNo, commentPage){
    let xhr=new XMLHttpRequest();
    let action=`${url}?cp=${commentPage}`;
    xhr.open(REQUEST_METHOD.GET,action,true);
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.send();
    xhr.onload=()=>{
        if(xhr.status===RESPONSE_STATUS.SUCCESS){
            // reloadCommentList(url, logined, userNo, xhr.response.commentList);
            reloadCommentList(url, logined, userNo, xhr.response.commentDtoList);
            reloadCommentPageHandler(url, logined, userNo, boardNo, postNo, xhr.response.commentPageHandler);
            const ulElement=document.querySelector('ul.comment-list');
            window.scroll({top: ulElement.offsetTop-extraOffset, left: 0, behavior: 'smooth'});
        }else{
            console.log('에러');
        }
    }
}
function reloadCommentList(url, logined, userNo, commentList){
    const cmtList=document.querySelector('ul.comment-list');
    cmtList.innerHTML="";
    if(commentList.length===0) return;
    commentList.forEach(comment=>{
        const date=new Date(comment.commentDate);
        const year=date.getFullYear();
        const month=date.getMonth()+1<10?`0${date.getMonth()+1}`:`${date.getMonth()+1}`;
        const day=date.getDate()<10?`0${date.getDate()}`:`${date.getDate()}`;
        const hour=date.getHours()<10?`0${date.getHours()}`:`${date.getHours()}`;
        const min=date.getMinutes()<10?`0${date.getMinutes()}`:`${date.getMinutes()}`;
        const sec=date.getSeconds()<10?`0${date.getSeconds()}`:`${date.getSeconds()}`;
        const commentDate=`${year}-${month}-${day} ${hour}:${min}:${sec}`;
        let depth=comment.depth;
        if(comment.depth>3) depth=3;
        const loginStatus=!logined?'not-logined':userNo===comment.userNo?'same-user':'logined';
        // `<li class="comment-item depth${depth}">
        //     </li>`;
        const commentVoteUrl=`${url}/${comment.commentNo}/vote`;
        const commentDisabled=(logined && comment.userNo!=userNo)?'':'disabled';
        const commentItem=document.createElement("li");
        commentItem.className=`comment-item depth${depth} cno${comment.commentNo}`;
        let edited='';
        let parentComment=comment.depth>1?`<span class="comment-parent" onclick = "getCommentByNo('${url}',${logined},${userNo},${comment.boardNo},${comment.postNo},${comment.parentComment})">@${comment.parentCommentNickname}</span>`:'';
        if(comment.status==1 || comment.status==3) edited='[리플수정]';
        let liText='';
        liText =
            `<div>
                <div class="comment-writer-box comment-left">
                    <div class="comment-writer">
                        <i class="fas fa-user"></i>
                        <span>${comment.nickname}</span>
                    </div>
                    <div class="${loginStatus}">
                        <button class="comment-edit edit" onclick="openEditCommentForm(this, '${url}', ${userNo}, ${comment.boardNo}, ${comment.postNo}, ${comment.commentNo})">수정</button>
                        <button class="comment-delete delete" onclick="eraseComment(this, '${url}', ${userNo}, ${comment.boardNo}, ${comment.postNo}, ${comment.commentNo}, ${comment.parentComment})">삭제</button>
                    </div>
                </div>
                <div class="comment-content-box comment-middle">
                    <div class="comment-content">
                        ${parentComment}
                        <span class="edited">
                            ${edited}
                        </span>
                        <span class="comment-content">
                            ${comment.content}                        
                        </span>
                    </div>
                </div>
                <div class="comment-info-box">
                    <div class="comment-info">
                        <div class="comment-time">
                            <span class="">${commentDate}</span>
                        </div>
                        <div class="comment-vote">
                            <button class="comment-good" onclick="commentVote(this, '${commentVoteUrl}', ${comment.boardNo},${comment.postNo},${comment.commentNo}, true, ${comment.userNo})" ${commentDisabled}>
                                <i class="fas fa-thumbs-up"></i><span class="like-count">${comment.likeCount}</span>
                            </button>
                            <button class="comment-bad" onclick="commentVote(this, '${commentVoteUrl}', ${comment.boardNo},${comment.postNo},${comment.commentNo}, false, ${comment.userNo})" ${commentDisabled}>
                                <i class="fas fa-thumbs-down"></i><span class="dislike-count">${comment.dislikeCount}</span>
                            </button>
                        </div>
                        <div class="comment-re ${loginStatus}">
                            <button class="comment-report report">신고</button>
                            <button class="comment-reply reply" onclick="commentReply(this,'${url}',${userNo}, ${comment.boardNo},${comment.postNo},${comment.commentNo},${comment.depth})">답글</button>
                        </div>                                                          
                    </div>
                </div>
            </div>`;
        if(comment.status<4) {
        }
        else if(comment.status===4){
            liText=
                `<div class="erased-comment">
                    삭제된 댓글입니다.
                </div>`;
        }else if(comment.status===6){
            liText=
                `<div class="blinded-comment">
                    관리자에 의해 가려진 댓글입니다.
                </div>`;
        }
        commentItem.innerHTML=liText;
        // const li=document.createElement('li');
        // li.innerHTML=liText;
        // cmtList.appendChild(li);
        cmtList.appendChild(commentItem);

    });
}
function writeComment(button, url, userNo, boardNo, postNo, parentComment, depth){
    const contentElement=button.parentElement.parentElement.parentElement.querySelector("textarea");
    const content=contentElement.value;
    if(content.length===0) {
        alert('내용을 입력해주세요.');
        return;
    }
    const comment={
        boardNo: boardNo,
        postNo: postNo,
        content: content,
        parentComment: parentComment,
        // commentParentNickname: '',
        depth: depth+1
    };

    const xhr=new XMLHttpRequest();
    // xhr.responseType="json";
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.open(REQUEST_METHOD.POST,url,true);
    xhr.setRequestHeader(CONTENT_TYPE, REQUEST_CONTENT_TYPE.JSON);
    xhr.send(JSON.stringify(comment));
    xhr.onload=()=>{
        if(xhr.status===RESPONSE_STATUS.SUCCESS){
            console.log(xhr.response);

            reloadCommentList(url,true, userNo, xhr.response.commentDtoList);
            reloadCommentPageHandler(url, true, userNo, boardNo, postNo, xhr.response.commentPageHandler);
            contentElement.value='';
            moveToComment(xhr.response.newCommentNo);

            const targetUser=xhr.response.alarmTargetUser;
            if(targetUser!==0){
                sendAlarm(targetUser,"alarm");
            }
        }else if(xhr.status===401){
            alert(xhr.response.message);
            setTimeout(function (e){
                location.reload();
            },1000);
        }else{
            alert("에러");
        }
    }
}
function closeOpendCommentForm(){
    const commentForm = document.querySelector("div.comment-writing.reply");
    if(commentForm)
        commentForm.remove();
}
function reloadCommentPageHandler(url, logined, userNo, boardNo, postNo, cph){
    const cphUl=document.querySelector("div.comment-pagination>ul");
    cphUl.innerHTML='';
    const cphList=cph.pageList;
    // console.log(cph);
    if(cphList.length===0) return;
    let cphContent='';
    if(cph.hasPrev){
        const prev=
        `<li class="comment-page-item">
            <a class="comment-page-link" onclick="commentPaging('${url}',${logined},${userNo},${boardNo},${postNo},${cphList[0]-1})">
                <i class="fas fa-angle-left"></i>
            </a>
        </li>`;
        cphContent+=prev;
    }

    for(let i=0;i<cphList.length;i++){
        const currentPage=cphList[i]===cph.currentPage?'current-page':'';
        const commentPage=
            `<li class="comment-page-item">
            <a onclick="commentPaging('${url}',${logined},${userNo},${boardNo},${postNo},${cphList[i]})" class="comment-page-link ${currentPage}">${cphList[i]}</a>
        </li>`;
        cphContent+=commentPage;
    }
    if(cph.hasNext){
        const next=
            `<li class="comment-page-item">
                <a class="comment-page-link" onclick="commentPaging('${url}',${logined},${userNo},${boardNo},${postNo},${cphList[cphList.length-1]+1})">
                    <i class="fas fa-angle-right"></i>
                </a>
            </li>`;
        cphContent+=next;
    }
    cphUl.innerHTML=cphContent;
}
function commentReply(button, url, userNo, boardNo, postNo, commentNo, depth){
    closeOpendCommentForm();
    const submitFunc=`writeComment(this, '${url}', ${userNo}, ${boardNo}, ${postNo}, ${commentNo},${depth})`;
    const commentWriting = openCommentForm(submitFunc,"closeOpendCommentForm()");
    const li=button.parentElement.parentElement.parentElement.parentElement.parentElement;
    li.appendChild(commentWriting);
}
function openCommentForm(submitFunc, cancelFunc){
    const commentWriting=document.querySelector("div.comment-writing").cloneNode(true);
    commentWriting.querySelector("textarea").value='';
    commentWriting.className='comment-writing reply';
    const submitButton = commentWriting.querySelector("button");
    submitButton.setAttribute("onclick",submitFunc);
    const bottom=document.createElement("div");
    const cancelButton=document.createElement("button");
    bottom.className="cancel";
    cancelButton.innerText="취소";
    cancelButton.setAttribute("onclick",cancelFunc);
    bottom.appendChild(cancelButton);
    submitButton.parentElement.parentElement.appendChild(bottom);
    return commentWriting;

}
function openEditCommentForm(button, url, userNo, boardNo, postNo, commentNo){
    closeOpendCommentForm();

    const commentLi=button.parentElement.parentElement.parentElement.parentElement;
    let content=commentLi.querySelector("span.comment-content").textContent.trim();
    commentLi.firstElementChild.style.display="none";
    const submitFunc=`editComment(this, '${url}', ${userNo}, ${boardNo}, ${postNo}, ${commentNo})`;
    const cancelFunc='closeEditForm(this)';
    const commentWriting=openCommentForm(submitFunc, cancelFunc);
    commentWriting.querySelector("textarea").value=content;
    commentLi.appendChild(commentWriting);

}
function closeEditForm(button){
    const commentLi=button.parentElement.parentElement.parentElement.parentElement;
    closeOpendCommentForm();
    commentLi.firstElementChild.style.display="";
}

function editComment(button, url, userNo, boardNo, postNo, commentNo){
    const contentElement=button.parentElement.parentElement.parentElement.querySelector("textarea");
    const content=contentElement.value;
    if(content.length===0) {
        alert('내용을 입력해주세요.');
        return;
    }
    const comment={
        boardNo: boardNo,
        postNo: postNo,
        commentNo: commentNo,
        userNo: userNo,
        content: content
    };
    const acton=`${url}/${commentNo}`;
    const xhr=new XMLHttpRequest();
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.open(REQUEST_METHOD.PATCH,acton,true);
    xhr.setRequestHeader(CONTENT_TYPE, REQUEST_CONTENT_TYPE.JSON);
    xhr.send(JSON.stringify(comment));
    xhr.onload=()=>{
        if(xhr.status===RESPONSE_STATUS.SUCCESS){
            reloadCommentList(url, true, userNo, xhr.response.commentDtoList);
            reloadCommentPageHandler(url, true, userNo, boardNo, postNo, xhr.response.commentPageHandler);
            contentElement.value='';
            moveToComment(commentNo);
            // const li=document.querySelector('ul.comment-list').querySelector(`li.cno${commentNo}`);
            // window.scroll({top: li.offsetTop, left: 0, behavior: 'smooth'});
        }else if(xhr.status===RESPONSE_STATUS.UNAUTHORIZED || xhr.status===RESPONSE_STATUS.FORBIDDEN){
            alert(xhr.response.message);
            setTimeout(function (e) {
                location.reload();
            }, 1000);
        }
        else{
            alert("에러");
        }
    }
}
function eraseComment(button, url, userNo, boardNo, postNo, commentNo, parentComment){
    const confirmErase = confirm("게시물을 삭제하시겠습니까?")
    if(!confirmErase) return;
    const comment={
        boardNo: boardNo,
        postNo: postNo,
        commentNo: commentNo,
        userNo: userNo,
        parentComment: parentComment
        // ,
        // likeCount: likeCount,
        // dislikeCount: dislikeCount
    };
    const acton=`${url}/${commentNo}`;
    const xhr=new XMLHttpRequest();
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.open(REQUEST_METHOD.DELETE,acton,true);
    xhr.setRequestHeader(CONTENT_TYPE, REQUEST_CONTENT_TYPE.JSON);
    xhr.send(JSON.stringify(comment));
    xhr.onload=()=>{
        if(xhr.status===RESPONSE_STATUS.SUCCESS){
            //자식 댓글이 있는 경우, 없는 경우로 나눠서
            reloadCommentList(url,true, userNo, xhr.response.commentDtoList);
            reloadCommentPageHandler(url, true, userNo, boardNo, postNo, xhr.response.commentPageHandler);
            console.log(xhr.response.completelyErased);
            if(!xhr.response.completelyErased){
                moveToComment(commentNo);
                // const li=document.querySelector('ul.comment-list').querySelector(`li.cno${commentNo}`);
                // window.scroll({top: li.offsetTop, left: 0, behavior: 'smooth'});
            }else{
                const ulElement=document.querySelector('ul.comment-list');
                window.scroll({top: ulElement.offsetTop-extraOffset, left: 0, behavior: 'smooth'});
            }
        }else if(xhr.status===RESPONSE_STATUS.UNAUTHORIZED || xhr.status===RESPONSE_STATUS.FORBIDDEN){
            alert(xhr.response.message);
            setTimeout(function (e) {
                location.reload();
            }, 1000);
        }
        else{
            alert("에러");
        }
    }
}
function commentVote(button, url, boardNo,postNo,commentNo,isLike,writerUserNo){
    const commentLikeDto={
        boardNo: boardNo,
        postNo: postNo,
        commentNo: commentNo,
        writerUserNo: writerUserNo,
        isLike: isLike
    };
    const commentVote=button.parentElement;
    const likeCount=commentVote.querySelector(".like-count");
    const dislikeCount=commentVote.querySelector(".dislike-count");

    console.log(commentLikeDto);
    console.log(url);
    const xhr=new XMLHttpRequest();
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.open(REQUEST_METHOD.PATCH,url,true);
    xhr.setRequestHeader(CONTENT_TYPE, REQUEST_CONTENT_TYPE.JSON);
    xhr.send(JSON.stringify(commentLikeDto));
    xhr.onload=()=>{
        if(xhr.status===RESPONSE_STATUS.SUCCESS) {
            if(xhr.response.code===1){
                // console.log(xhr.response);
                likeCount.innerHTML = xhr.response.responseObject.likeCount;
                dislikeCount.innerHTML = xhr.response.responseObject.dislikeCount;
            }
            else if(xhr.response.code===0){
                const confirmCancel=confirm(xhr.response.message);
                if(confirmCancel){
                    cancelVoteComment(likeCount,dislikeCount, url, boardNo, postNo, commentNo, writerUserNo);
                }
            }
        }
        else if(xhr.status===RESPONSE_STATUS.UNAUTHORIZED){
            alert(xhr.response.message);
            setTimeout(function (e) {
                location.reload();
            }, 1000);
        }
        else{
            alert("에러");
        }
    }
}
function cancelVoteComment(likeCount,dislikeCount, url, boardNo, postNo, commentNo, writerUserNo){
    const commentLikeDto={
        boardNo: boardNo,
        postNo: postNo,
        commentNo: commentNo,
        writerUserNo: writerUserNo
        // isLike: isLike
    };
    const xhr=new XMLHttpRequest();
    xhr.open(REQUEST_METHOD.DELETE,url,true);
    xhr.setRequestHeader(CONTENT_TYPE, REQUEST_CONTENT_TYPE.JSON);
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.send(JSON.stringify(commentLikeDto));
    xhr.onload=()=> {
        if (xhr.status === RESPONSE_STATUS.SUCCESS) {
            // if (xhr.response.code === 1) {
                likeCount.innerHTML=xhr.response.responseObject.likeCount;
                dislikeCount.innerHTML=xhr.response.responseObject.dislikeCount;
            // }
        }
        else if(xhr.status===RESPONSE_STATUS.UNAUTHORIZED){
            alert(xhr.response.message);
            setTimeout(function (e) {
                location.reload();
            }, 1000);
        }
        else{
            alert("에러");
        }
    }
}
function refreshComment(url, logined, userNo, boardNo, postNo){
    console.log(logined);
    console.log(userNo);
    const xhr=new XMLHttpRequest();
    xhr.open(REQUEST_METHOD.GET,url,true);
    // xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.send();
    xhr.onload=()=> {
        console.log(xhr.status);
        if (xhr.status === RESPONSE_STATUS.SUCCESS) {
            reloadCommentList(url,logined,userNo,xhr.response.commentDtoList);
            reloadCommentPageHandler(url, logined,userNo,boardNo, postNo, xhr.response.commentPageHandler);
            const ulElement=document.querySelector('ul.comment-list');
            window.scroll({top: ulElement.offsetTop-extraOffset, left: 0, behavior: 'smooth'});

        }else{
            alert("에러");
        }
    }
}
function getCommentByNo(url, logined, userNo, boardNo, postNo, commentNo){
    if(moveToComment(commentNo)) return;

    const action=`${url}/${commentNo}`;
    const xhr=new XMLHttpRequest();
    xhr.open(REQUEST_METHOD.GET,action,true);
    // xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.send();
    xhr.onload=()=> {
        console.log(xhr.status);
        if (xhr.status === RESPONSE_STATUS.SUCCESS) {
            reloadCommentList(url,logined,userNo,xhr.response.commentDtoList);
            reloadCommentPageHandler(url, logined,userNo,boardNo, postNo, xhr.response.commentPageHandler);
            // if (xhr.response.code === 1) {
            // }
            moveToComment(commentNo);

        }else{
            alert("에러");
        }
    }
}
function moveToComment(commentNo){
    const cmtList=document.querySelector('ul.comment-list');
    const li=cmtList.querySelector(`li.comment-item.cno${commentNo}`);
    if(li){
        window.scroll({top: li.offsetTop-extraOffset, left: 0, behavior: 'smooth'});
        changeAround(li);
        return true;
    }
    return false;
}

let targetLi;
let func;

function changeAround(element){
    if(func){
        clearTimeout(func);
    }
    if(targetLi){
        removeShadow();
    }
    targetLi=element;
    func=setTimeout(darken,0,500);
    func=setTimeout(lighten, 500, 1000, 2000);
}
function removeShadow() {
    const li=targetLi;
    li.style.removeProperty("box-shadow");
    li.style.removeProperty("z-index");
}
function lighten(b, z){
    const li=targetLi;
    li.style.transition=`box-shadow ease ${b}ms 0s, z-index ease ${z}ms 0s`;
    li.style.boxShadow="";
    li.style.zIndex="0";
}
function darken(ms){
    const li=targetLi;
    li.style.zIndex="1";
    li.style.transition=`box-shadow ease ${ms}ms 0s, z-index ease 0ms 0s`;
    li.style.boxShadow="0 0 40px 15px #bcbcbc";
}