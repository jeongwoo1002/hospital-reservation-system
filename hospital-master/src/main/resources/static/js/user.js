// let index = {
//
//     init:function () {
//         $("#btn-save").on("click", ()=> {
//             this.save();
//         });
//
//         $("#btn-update").on("click", ()=> {
//             this.update();
//         });
//     },
//
//     save:function () {
//         let data = {
//             username: $("#username").val(),
//             password: $("#password").val(),
//             name: $("#name").val(),
//             number: $("#number").val(), // 전화번호 추가
//             address: $("#address").val(), // 주소 추가
//             resident: $("#resident").val() // 주민번호 추가
//         }
//         console.log(data);
//
//         $.ajax({
//             type:"POST",
//             url:"/auth/joinProc",
//             data:JSON.stringify(data),
//             contentType:"application/json; charset=utf-8",
//             dataType:"json"
//         }).done(function (resp){
//             console.log(resp);
//             alert("회원가입이 완료되었습니다.");
//             location.href="/";
//         }).fail(function (error){
//             alert(JSON.stringify(error));
//         });
//     },
//
//     update:function () {
//         let data = {
//             id: $("#id").val(),
//             username: $("#username").val(),
//             password: $("#password").val(),
//             number: $("#number").val(), // 전화번호 추가
//             address: $("#address").val(), // 주소 추가
//             resident: $("#resident").val() // 주민번호 추가
//         }
//
//         $.ajax({
//             type:"PUT",
//             url:"/user",
//             data:JSON.stringify(data),
//             contentType:"application/json; charset=utf-8",
//             dataType:"json"
//         }).done(function (resp){
//             console.log(resp);
//             alert("회원수정이 완료되었습니다.");
//             location.href="/";
//         }).fail(function (error){
//             alert(JSON.stringify(error));
//         });
//     }
// }
//
// index.init();

let index = {

    init:function () {
        $("#btn-save").on("click", ()=> {
            this.save();
        });

        $("#btn-update").on("click", ()=> {
            this.update();
        });
    },

    save:function () {
        let data = {
            username: $("#username").val(),
            password: $("#password").val(),
            name: $("#name").val(),
            number: $("#number").val(), // 전화번호 추가
            address: $("#address").val(), // 주소 추가
            resident: $("#resident").val() // 주민번호 추가
        }
        console.log(data);

        $.ajax({
            type:"POST",
            url:"/auth/joinProc",
            data:JSON.stringify(data),
            contentType:"application/json; charset=utf-8",
            dataType:"json"
        }).done(function (resp){
            console.log(resp);
            alert("회원가입이 완료되었습니다.");
            location.href="/";
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    },

    update:function () {
        let data = {
            id: $("#id").val(),
            username: $("#username").val(),
            password: $("#password").val(),
            number: $("#number").val(), // 전화번호 추가
            address: $("#address").val(), // 주소 추가
            resident: $("#resident").val() // 주민번호 추가
        }

        $.ajax({
            type:"PUT",
            url:"/user",
            data:JSON.stringify(data),
            contentType:"application/json; charset=utf-8",
            dataType:"json"
        }).done(function (resp){
            console.log(resp);
            alert("회원수정이 완료되었습니다.");
            location.href="/user/userInfo";
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    }
}

// CSS 추가
let style = `
    #btn-save, #btn-update {
        margin-top: 20px;
        padding: 10px 20px;
        background-color: #007bff;
        color: #ffffff;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }
    
    #btn-save:hover, #btn-update:hover {
        background-color: #0056b3;
    }
`;

// <style> 태그 생성 후 CSS 추가
let styleTag = document.createElement('style');
styleTag.type = 'text/css';
styleTag.appendChild(document.createTextNode(style));
document.head.appendChild(styleTag);

index.init();
