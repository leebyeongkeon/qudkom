const images=[];
const imageNames=new Map();
function setUpTiny(mode, content, boardNo){
    tinymce.init({
        selector: '#content',
        language: "ko_KR",
        menubar: true,
        entity_encoding: 'raw',
        height: 500,
        plugins: 'lists advlist link image code preview media table',
        toolbar: "undo redo | fontsizeselect | forecolor | bold italic strikethrough underline | alignleft aligncenter alignright alignjustify | link image media | code preview | bullist numlist outdent indent table",
        // meta: false,
        relative_urls: false,
        automatic_uploads: true,
        // images_upload_url: '/board/image',
        // images_reuse_filename: true,

        file_picker_types: 'image',
        image_list: images,
        images_upload_handler: imageUploadHandler,
        setup: function (editor){
            if(mode==='edit'){
                editor.on('init', function (e) {
                    editor.setContent(content);
                    // initImages(editor);
                });
            }
            // editor.on("execCommand",function (e){
            //     if(e.command==="mceUpdateImage"){
            //         console.log(e.value);
            //         const img=editor.contentDocument.querySelector(`img[src='${e.value.src}']`);
            //         console.log("img", img);
            //         const mceSrcAttr="data-mce-src";
            //         let srcValue=e.value.src;
            //         if(img) {
            //             const mceSrc=img.getAttribute(mceSrcAttr);
            //             if (mceSrc.startsWith("..")) {
            //                 img.setAttribute(mceSrcAttr, srcValue);
            //             }
            //         }else{
            //             // console.log(img);
            //             // console.log(srcValue);
            //             if(srcValue.match("/v/") && srcValue.startsWith("..")){
            //                 const path=srcValue.substring(0, srcValue.search("/v/"));
            //                 srcValue.replace(path,contextPath);
            //             }else if(srcValue.match("/temp/") && srcValue.startsWith("..")){
            //                 const path=srcValue.substring(0, srcValue.search("/temp/"));
            //                 srcValue.replace(path,contextPath);
            //             }
            //         }
            //     }
            // });
        }
    });
}
function initImages(editor){
    const imgs=editor.contentDocument.querySelectorAll("img");
    imgs.forEach((img)=>{
        const src=img.getAttribute("src");
        img.setAttribute("data-mce-src",src);
        console.log(img);
    });
}
function imageUploadHandler (blobInfo, success, failure, progress) {

    const xhr = new XMLHttpRequest();
    xhr.responseType=RESPONSE_CONTENT_TYPE.JSON;
    xhr.withCredentials = false;
    xhr.open(REQUEST_METHOD.POST, `${contextPath}/board/image`);

    xhr.upload.onprogress = function (e) {
        progress(e.loaded / e.total * 100);
    };
    // xhr.upload.=function (e){
    //     console.log(e);
    // };

    xhr.onload = function() {
        let json;

        if (xhr.status === RESPONSE_STATUS.FORBIDDEN) {
            failure('HTTP Error: ' + xhr.status, { remove: true });
            return;
        }

        if (xhr.status < RESPONSE_STATUS.SUCCESS || xhr.status >= 300) {
            failure('HTTP Error: ' + xhr.status);
            return;
        }

        json = xhr.response;

        // if (!json || typeof json.location != 'string') {
        //     failure('Invalid JSON: ' + xhr.response);
        //     return;
        // }

        const src=contextPath+TEMP_PREFIX+json.filename;
        success(src);

        images.push({title: blobInfo.filename(),value: src});
        imageNames[json.filename] = blobInfo.filename();

    };

    xhr.onerror = function () {
        failure('Image upload failed due to a XHR Transport error. Code: ' + xhr.status);
    };

    const formData = new FormData();
    formData.append('file', blobInfo.blob(), blobInfo.filename());
    xhr.send(formData);
}

const sleep = (ms) => {
    return new Promise((resolve) => setTimeout(resolve, ms))
}