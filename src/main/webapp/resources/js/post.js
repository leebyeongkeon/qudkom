const TEMP_PREFIX="/temp/";
const ORIGINAL_FILENAME='data-original-filename';
function uploadPost(form){
    const editor=tinymce.activeEditor;
    setOriginalFilename(editor);

    const content=editor.getContent();
    const data=new FormData(form);
    data.set("content", content);
    // return false;
    const xhr=new XMLHttpRequest();
    xhr.open(REQUEST_METHOD.POST,form.action,true);
    xhr.setRequestHeader(CONTENT_TYPE, REQUEST_CONTENT_TYPE.URL_ENCODED);
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.send(serialize(data));

    xhr.onload=()=>{
        if(xhr.status===RESPONSE_STATUS.SUCCESS){
            // if(xhr.response.code===1)
            // window.open(xhr.response.url, "_blank");
            location.href=xhr.response.url;
        }else{
            if(xhr.status===RESPONSE_STATUS.UNAUTHORIZED || xhr.status===RESPONSE_STATUS.FORBIDDEN){
                alert(xhr.response.message);
                setTimeout(function (e){
                    history.back();
                }, 1000);
            }else{
                alert("에러");
            }
        }
        // console.log(xhr.response);
    }
    return false;
}
function editPost(form){
    const editor=tinymce.activeEditor;
    setOriginalFilename(editor)
    const content=editor.getContent();

    const data=new FormData(form);
    data.set("content",content);
    const xhr=new XMLHttpRequest();
    xhr.open(REQUEST_METHOD.PATCH,form.action,true);
    xhr.setRequestHeader(CONTENT_TYPE, REQUEST_CONTENT_TYPE.JSON);
    xhr.onload=()=>{
        console.log(xhr.status);
        console.log(xhr.response);
        if(xhr.status===RESPONSE_STATUS.SUCCESS){
            location.href=xhr.response.url;
        }else if(xhr.status===RESPONSE_STATUS.UNAUTHORIZED || xhr.status===RESPONSE_STATUS.FORBIDDEN){
            alert(xhr.response.message);
            setTimeout(function (e){
                history.back();},1000);
        }else{
            alert("에러");
        }
    }
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.send(JSON.stringify(formToJson(data)));
    return false;
}
function setOriginalFilename(editor){
    const imgElements=editor.contentDocument.querySelectorAll("img")

    imgElements.forEach((img)=>{
        const src=img.getAttribute("src");
        const filename=src.substring(src.indexOf(TEMP_PREFIX)+TEMP_PREFIX.length,src.length);
        if(!img.getAttribute(ORIGINAL_FILENAME))
            img.setAttribute(ORIGINAL_FILENAME, imageNames[filename]);
    });
}
function erasePost(boardNo, postNo, userNo, url){
    const data={
        boardNo: boardNo,
        postNo: postNo,
        userNo: userNo
    };
    const confirmErase = confirm("게시물을 삭제하시겠습니까?")
    if(!confirmErase) return;
    const xhr=new XMLHttpRequest();
    xhr.open(REQUEST_METHOD.DELETE,url,true);
    xhr.setRequestHeader(CONTENT_TYPE, REQUEST_CONTENT_TYPE.JSON);
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.send(JSON.stringify(data));
    xhr.onload=()=>{
        if(xhr.status===RESPONSE_STATUS.SUCCESS){
            location.href=xhr.response.url;
        }else if(xhr.status===RESPONSE_STATUS.UNAUTHORIZED || xhr.status===RESPONSE_STATUS.FORBIDDEN){
            alert(xhr.response.message);
            setTimeout(function (e){
                location.reload();
                },1000);
        }else{
            alert("에러");
        }
    }
}
function votePost(boardNo, postNo, userNo, isLike, writerUserNo, url){
    const postLikeDto={
        boardNo: boardNo,
        postNo: postNo,
        isLike: isLike,
        writerUserNo: writerUserNo
    };
    //postLikeDto객체
    console.log(postLikeDto);
    const xhr=new XMLHttpRequest();
    xhr.open(REQUEST_METHOD.PATCH,url,true);
    xhr.setRequestHeader(CONTENT_TYPE, REQUEST_CONTENT_TYPE.JSON);
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.send(JSON.stringify(postLikeDto));
    xhr.onload=()=>{
        if(xhr.status===RESPONSE_STATUS.SUCCESS){
            if(xhr.response.code===1){
                updatePostLikeView(xhr.response.responseObject);
            }else if(xhr.response.code===0){
                const confirmCancel=confirm(xhr.response.message);
                console.log(confirmCancel);
                if(confirmCancel){
                    cancelVotePost(boardNo,postNo,userNo,writerUserNo,url);
                }
            }
        }
        else if(xhr.status===RESPONSE_STATUS.UNAUTHORIZED){
            alert(xhr.response.message);
            setTimeout(function (e){
                location.reload();
            },1000);
        }else{
            alert("에러");
        }
    }
}
function updatePostLikeView(post){
    const voteContainer=document.querySelector("div.vote-container");
    const likeCount=voteContainer.querySelector(".like-count");
    const dislikeCount=voteContainer.querySelector(".dislike-count");

    likeCount.textContent=post.likeCount;
    dislikeCount.textContent=post.dislikeCount;
}
function cancelVotePost(boardNo,postNo,userNo,writerUserNo,url){
    const postLikeDto={
        boardNo: boardNo,
        postNo: postNo,
        // userNo: userNo,
        writerUserNo: writerUserNo
    };
    const xhr=new XMLHttpRequest();
    xhr.open(REQUEST_METHOD.DELETE,url,true);
    xhr.setRequestHeader(CONTENT_TYPE, REQUEST_CONTENT_TYPE.JSON);
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.send(JSON.stringify(postLikeDto));
    xhr.onload=()=> {
        console.log(xhr.status);
        if (xhr.status === RESPONSE_STATUS.SUCCESS) {
            updatePostLikeView(xhr.response.responseObject);
        }else if(xhr.status===RESPONSE_STATUS.UNAUTHORIZED){
            alert(xhr.response.message);
            setTimeout(function (e){
                location.reload();
            },1000);
        }
        else{
            alert("에러");
        }
    }
}

document.addEventListener("DOMContentLoaded",function (e){
    const postWrapper=e.target.querySelector(".post-wrapper");
    if(postWrapper){
        console.log(postWrapper);
        postWrapper.querySelectorAll("img")
            .forEach((img)=>{
                img.onclick = (e)=>{
                    const a = document.createElement("a");
                    a.href = e.target.src;
                    a.download = e.target.getAttribute(ORIGINAL_FILENAME);
                    a.click();
                }
        });
    }
});