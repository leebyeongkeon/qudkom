const REQUEST_METHOD={
    GET: "GET",
    POST: "POST",
    PATCH: "PATCH",
    PUT: "PUT",
    DELETE: "DELETE"
};
const CONTENT_TYPE='Content-Type';
const REQUEST_CONTENT_TYPE={
    JSON: 'application/json',
    URL_ENCODED: 'application/x-www-form-urlencoded',
    MULTI_PART: 'multipart/form-data'
};
const RESPONSE_STATUS={
    SUCCESS: 200,
    UNAUTHORIZED: 401,
    FORBIDDEN: 403
};
const RESPONSE_CONTENT_TYPE={
    JSON: "json"
};
let contextPath;
function initPath(cPath){
    contextPath=cPath;
}
function serialize(formData){
    const array=new Array();
    formData.forEach((value, key)=>{
        const param=''.concat(key,'=',value);
        array.push(param);
    });
    const result=array.join('&');
    return result;
}
function formToJson(formData){
    const json=Object();
    formData.forEach((value, key)=>{
        json[key]=value;
    });
    return json;
}
let serverName;
function setServerName(name){
    serverName=name;
}