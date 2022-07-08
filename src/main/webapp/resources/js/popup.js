function batchProcess(url, method, category, save){
    let checkedList;
    checkedList = document.querySelector(".memo-list-table").querySelectorAll("input[type='checkbox']:checked");
    if(checkedList.length===0) return;
    if(method==='DELETE'){
        // console.log(checkedList.length);
        if(!confirm("삭제하시겠습니까?")) {
            return;
        }
    }
    const memoNoList=[];
    const memoParamBundle={
        // category: category,
        memoNoList: memoNoList
    };
    checkedList.forEach(checkedItem=>memoNoList.push(parseInt(checkedItem.nextElementSibling.value)));
    console.log(memoParamBundle);
    let xhr=new XMLHttpRequest();
    xhr.open(method,`${url}/${category}`,true);
    // xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.setRequestHeader(CONTENT_TYPE, REQUEST_CONTENT_TYPE.JSON);
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.send(JSON.stringify(memoParamBundle));
    xhr.onload=()=>{
        if(xhr.status===RESPONSE_STATUS.SUCCESS){
            // if(xhr.response.code===1){
                // location.reload();
                console.log(save);
                location.href=xhr.response.url+save;
                const list=document.querySelector(".memo-list-table").querySelectorAll("input[type='checkbox']:checked");
                list.forEach(i=>i.checked=false);
            // }
        }
    }
}
let checkedAll=false;
function checkAll(){
    const checkList=document.querySelectorAll("table.memo-list-table input.memo-check-input");
    checkedAll=!checkedAll
    checkList.forEach(check=>check.checked=checkedAll);
}
let width;
let height;
function loadPopUp(){
    let hasScrollBar=false;
    const scrollBarWidth=18;
    const diffHeight=parent.outerHeight-parent.innerHeight;
    const diffWidth=parent.outerWidth-parent.innerWidth;

    width=document.body.offsetWidth+diffWidth;
    height=parent.outerHeight;

    if(document.body.offsetHeight<=parent.innerHeight){
        hasScrollBar=false;
    }
    if(document.body.offsetHeight>parent.innerHeight){
        hasScrollBar=true;
    }
    if(hasScrollBar){
        width=document.body.offsetWidth+diffWidth+scrollBarWidth;
    }
    resizePopUP();
}
function resizePopUP(){
    parent.resizeTo(width,height);
}
function getCommentViewByAlarm(url, boardNo, postNo, commentNo){

}
function setCategory(category){
    document.querySelector("form").category.value=category;
}
function checkMemo(form) {
    if (form.title.value.length === 0) {
        alert("제목을 입력해야 합니다.");
        return false;
    }

    else if(form.content.value.length===0){
        alert("내용을 입력해야 합니다.");
        return false;
    }
    form.action=`${form.path.value}/${form.category.value}`;
    console.log(form.action);
    return true;
}
function sendMemo(form){
    if(!checkMemo(form)) return false;
    // debugger;
    const data=new FormData(form);
    const xhr=new XMLHttpRequest();
    xhr.open(form.method,form.action,true);
    xhr.setRequestHeader(CONTENT_TYPE, REQUEST_CONTENT_TYPE.URL_ENCODED);
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.send(serialize(data));
    xhr.onload=()=> {
        if (xhr.status === RESPONSE_STATUS.SUCCESS) {
            console.log(xhr.response);
            // if (xhr.response.code === 1){
            const counterpartUser = data.get("counterpartUser");
            sendAlarm(counterpartUser, "memo");
            setTimeout(() => {
                location.href = xhr.response.url;
            }, 1000);

        }else if(xhr.status===RESPONSE_STATUS.UNAUTHORIZED){
            alert(xhr.response.message);
        }
        else {
            alert("에러");
        }
    }
    return false;
}
function checkAlarm(boardNo, postNo, commentNo){
    console.log(boardNo);
    console.log(postNo);
    console.log(commentNo);
    const xhr=new XMLHttpRequest();
    xhr.open(REQUEST_METHOD.GET,`${contextPath}/comment/${boardNo}/${postNo}/${commentNo}/exists`,true);
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.send();
    xhr.onload=()=>{
        if(xhr.status===RESPONSE_STATUS.SUCCESS){
            if(xhr.response.code===1){
                console.log(xhr.response.responseObject);
                const cp=xhr.response.responseObject;
                let url=`${contextPath}/board/${boardNo}/${postNo}?cp=${cp}&cno=${commentNo}`;
                // console.log(url);
                window.open(url);
            }else if(xhr.response.code===0){
                alert("댓글이 존재하지 않습니다.");
            }
        }else{
            alert("에러");
        }
    }
}
function eraseAlarm(i, alarmNo){
    const xhr=new XMLHttpRequest();
    xhr.open(REQUEST_METHOD.DELETE,`${contextPath}/alarm/${alarmNo}`,true);
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.send();
    xhr.onload=()=> {
        if (xhr.status === RESPONSE_STATUS.SUCCESS) {
            if (xhr.response.code === 1) {
                i.parentElement.parentElement.remove();
            }
        }else if(xhr.status===RESPONSE_STATUS.UNAUTHORIZED || xhr.status===RESPONSE_STATUS.FORBIDDEN){
            alert(xhr.response.message);
        }
        else{
            alert("에러");
        }
    }
}