const userRegInfo={
    id:{
        // minLen:4,
        // maxLen:10,
        value:'',
        usable:false
    },
    pwd:{
        // minLen:8,
        // maxLen:15,
        first:'',
        second:'',
        usable:false,
    },
    nickname:{
        // minLen:2,
        // maxLen:6,
        usable:false
    },
    email:{
        usable:false
    }
}

function checkId(input){
    const id=input.previousElementSibling.value;
    const xhr=new XMLHttpRequest();
    xhr.open(REQUEST_METHOD.POST,`${contextPath}/user/id`,true);
    xhr.setRequestHeader(CONTENT_TYPE, REQUEST_CONTENT_TYPE.URL_ENCODED);
    // xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    const formData=new FormData();
    formData.set("id", id);
    xhr.send(serialize(formData));
    // xhr.send(`id=${id}`);
    xhr.onload=()=>{
        if(xhr.status===RESPONSE_STATUS.SUCCESS){
            if(xhr.response.code===0){//존재하지 않는 아이디이므로 사용 가능함
                userRegInfo.id.usable=true;
            }else{
                userRegInfo.id.usable=false;
            }
            // console.log(xhr.response);
            alert(xhr.response.message);
        }
    };

    /*
    const len=element.previousElementSibling.value.length;
    if(len<userRegInfo.id.minLen) {
        confirm("길이가 짧습니다.");
        userRegInfo.id.usable=false;
        return;
    }
    if(len>userRegInfo.id.maxLen){
        confirm("적정 길이를 초과했습니다.")
        userRegInfo.id.usable=false;
        return;
    }
    userRegInfo.id.usable=true;
    */
}
function checkPwd(element){
    userRegInfo.pwd.first=element.previousElementSibling.value;
    if(pwd.length<6)
        confirm("길이가 짧습니다");
}

function checkNickname(input){
    const nickname=input.previousElementSibling.value;
    const xhr=new XMLHttpRequest();
    xhr.open(REQUEST_METHOD.POST,`${contextPath}/user/nickname`,true);
    xhr.setRequestHeader(CONTENT_TYPE, REQUEST_CONTENT_TYPE.URL_ENCODED);
    // xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    const formData=new FormData();
    formData.set("nickname", nickname);
    // xhr.send(`nickname=${nickname}`);
    xhr.send(serialize(formData));
    xhr.onload=()=>{
        if(xhr.status===RESPONSE_STATUS.SUCCESS){
            console.log(xhr.response);
            if(xhr.response.code===0){//존재하지 않는 닉네임이므로 사용 가능함
                userRegInfo.nickname.usable=true;
            }else{
                userRegInfo.nickname.usable=false;
            }
            alert(xhr.response.message);
        }
    };
}
function sendMail(input){
    const email=input.previousElementSibling.value;
    const xhr=new XMLHttpRequest();
    xhr.open(REQUEST_METHOD.POST,`${contextPath}/user/email/send`,true);
    xhr.setRequestHeader(CONTENT_TYPE, REQUEST_CONTENT_TYPE.URL_ENCODED);
    // xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    const formData=new FormData();
    formData.set("email", email);
    xhr.send(serialize(formData));
    // xhr.send(`email=${email}`);
    xhr.onload=()=>{
        if(xhr.status===RESPONSE_STATUS.SUCCESS){
            if(xhr.response.code==1){
                //이메일이 이미 존재하는 경우
            }else if(xhr.response.code==0){
                //메일을 보내는데 성공함
            }
            alert(xhr.response.message);
        }
    }
}
function cerifyMail(input){
    const cerifCode=input.previousElementSibling.value;
    const xhr=new XMLHttpRequest();
    xhr.open(REQUEST_METHOD.POST,`${contextPath}/user/email/cerif`,true);
    // xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.setRequestHeader(CONTENT_TYPE, REQUEST_CONTENT_TYPE.URL_ENCODED);
    // xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    const formData=new FormData();
    formData.set("cerifCode", cerifCode);
    xhr.send(serialize(formData));
    // xhr.send(`cerifCode=${cerifCode}`);
    xhr.onload=()=>{
        if(xhr.status===RESPONSE_STATUS.SUCCESS){
            if(xhr.response.code==1){
                //메일 인증에 성공함
                userRegInfo.email.usable=true;
            }else if(xhr.response.code==0){
                userRegInfo.email.usable=false;
            }
            alert(xhr.response.message);
        }
    }
}
function submitRegister(form){
    // const id=form.querySelector("input[name='id']");
    // const password=form.querySelector("input[name='password']");
    // const checkPwd=form.querySelector("input[name='checkPwd']");
    // const nickname=form.querySelector("input[name='nickname']");
    // const email=form.querySelector("input[name='email']");
    // const cerifCode=form.querySelector("input[name='cerifCode']");
    // const data=`id=${id.value}&password=${password.value}&checkPwd=${checkPwd.value}&nickname=${nickname.value}&email=${email.value}&cerifCode=${cerifCode.value}`;

    const formData=new FormData(form);
    const xhr=new XMLHttpRequest();
    xhr.open(REQUEST_METHOD.POST,form.action,true);
    xhr.setRequestHeader(CONTENT_TYPE, REQUEST_CONTENT_TYPE.URL_ENCODED);
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.send(serialize(formData));
    // xhr.send(data);
    xhr.onload=()=>{
        if(xhr.status===RESPONSE_STATUS.SUCCESS){
            let res=xhr.response
            console.log(res);
            alert(res.message);
            location.href=res.url;
            // if(xhr.response.code==1){
                // alert(xhr.response.msg);
                // location.href="/";
            // }else if(xhr.response.code==0){
            //     alert(xhr.response.msg);
            //     let errors=xhr.response.errors;
            //     errors.forEach(e=()=>{
            //         e.g
            //     })
            // }
        }else if(xhr.status===207){
            console.log(xhr.response);
            xhr.response.errors.forEach(error=>{
                // let field=`${error.field}`;
               let sib=form.querySelector("input[name='"+error.field+"']").parentElement.nextElementSibling;
                sib.textContent=error.message;
            });
        }
    }
    return false;
}
function login(form){
    // const id=form.querySelector("input[name='id']").value;
    // const password=form.querySelector("input[name='password']").value;
    const auto=form.querySelector("input[name='autoLogin']").checked?true:false;
    // const data=`id=${id}&password=${password}&autoLogin=${autoLogin}`;
    const data=new FormData(form);
    data.set("autoLogin", auto);
    const xhr=new XMLHttpRequest();
    xhr.open(REQUEST_METHOD.POST,form.action,true);
    xhr.setRequestHeader(CONTENT_TYPE, REQUEST_CONTENT_TYPE.URL_ENCODED);
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    // alert(data);
    // xhr.send(data);
    xhr.send(serialize(data));
    xhr.onload=()=>{
        if(xhr.status===RESPONSE_STATUS.SUCCESS){
            if(xhr.response.code==1){//로그인 성공
                location.reload();
            }else{//실패
                // alert("아이디 또는 비밀번호가 일치하지 않습니다.");
                alert(xhr.response.message);
            }
        }else{

            // alert(xhr.response.message);
        }
    }
    return false;
}
function logout(url){
    const xhr=new XMLHttpRequest();
    xhr.open(REQUEST_METHOD.GET,url,true);
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.send();
    xhr.onload=()=> {
        if (xhr.status === RESPONSE_STATUS.SUCCESS) {
            alert(xhr.response.message);
        }else{
        }
        location.reload();
    }
}