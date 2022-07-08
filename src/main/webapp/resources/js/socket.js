let socket;

function socketInit() {
    // console.log("cPath:", cPath);
    socket = new WebSocket(`ws://${serverName}:80${contextPath}/alarm`);
    // socket.onopen = function (event) {
    //     socketOpen(event);
    // }
    socket.onclose = function (event) {
        socketClose(event);
    }
    socket.onmessage = function (event) {
        socketMessage(event);
    }
    socket.onerror = function (event) {
        socketError(event);
    }
}

function socketOpen(event){
    console.log(event);
    console.log("연결");
}
function socketClose(event){
    console.log(event);
    console.log("종료");
    socketInit();//재연결 시도
    console.log("재연결");
}
function socketMessage(event){
    console.log(event);
    const message=event.data;
    console.log(message);
    const msg=JSON.parse(event.data);
    if(msg.type==="memo"){
        getNewMemoCount();
    }else if(msg.type==="alarm"){
        getNewAlarmCount();
    }
}
function getNewAlarmCount(){
    const xhr=new XMLHttpRequest();
    xhr.open(REQUEST_METHOD.GET,`${contextPath}/alarm/new`,true);
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.send();
    xhr.onload=()=> {
        if (xhr.status === RESPONSE_STATUS.SUCCESS) {
            const count=xhr.response;
            rewriteAlarmCount(count);
            showMessage("새 댓글이 있습니다.");
        } else {

        }
    }
}
function getNewMemoCount(){
    const xhr=new XMLHttpRequest();
    xhr.open(REQUEST_METHOD.GET,`${contextPath}/memo/new`,true);
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.send();
    xhr.onload=()=> {
        if (xhr.status === RESPONSE_STATUS.SUCCESS) {
            const count=xhr.response;
            rewriteMemoCount(count);
            showMessage("새 쪽지가 도착했습니다.");
        } else {

        }
    }
    // xhr.onloadend=()=>{return count};
}
function showMessage(msg){
    const element=document.createElement("div");
    element.classList="message-box";
    element.innerHTML=`${msg}`;
    let closed=false;
    element.onclick=function (){
        element.remove();
        closed=true;
        console.log("미리 닫음");
    }
    document.body.appendChild(element);
    setTimeout(()=>{
        if(!closed) {
            element.remove();
            closed=true;
        }
        else{
            console.log("이미 닫힘")
        }
        },5000);
}
function socketError(event){
    console.log(event);
    console.log("에러")
}

function socketMessageSend(){
    const msg={
        type: "message",
        value: "메시지",
        seq: 1
    };
    socket.send(msg);
}

function disconnect(){
    socket.close();
}

function rewriteMemoCount(newMemoCount){
    console.log(newMemoCount);
    const memoButton=document.querySelector("div.login-menu button.user-memo");
    const memo=`쪽지<span class="memo-new new-count">${newMemoCount}</span>`;
    memoButton.innerHTML=memo;
}
function rewriteAlarmCount(newAlarmCount){
    console.log(newAlarmCount);
    const alarmButton=document.querySelector("div.login-menu button.user-alarm");
    const alarm=`알람<span class="alarm-new new-count">${newAlarmCount}</span>`;
    alarmButton.innerHTML=alarm;
}
function sendAlarm(targetUser, type){
    console.log(targetUser, type);
    const alarmMsg={
        userNo: targetUser,
        type: type
    };
    console.log(alarmMsg);
    // socket=getSocket();
    // if(socket===undefined) {
    //     console.log("재연결");
        socketInit();
    // }else{
    //     console.log("연결돼있음");
    // }
    socket.onopen=function (event){
        // console.log(event);
        // console.log(socket.readyState);
        // debugger;
        socket.send(JSON.stringify(alarmMsg));
    }
}