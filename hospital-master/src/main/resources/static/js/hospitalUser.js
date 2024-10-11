let hospitalUserIndex = {
    init:function () {
        $("#btn-save").on("click", () => {
            this.save();
        });

        $("#btn-update").on("click", () => {
            this.update();
        });
    },

    save:function () {
        let data = {
            username: $("#username").val(),
            password: $("#password").val(),
            name: $("#name").val(),
            number: $("#number").val(),
            address: $("#address").val(),
            resident: $("#resident").val(),
            hospitalName: $("#hospitalName").val(),
            hospitalAddress: $("#hospitalAddress").val(),
            hospitalPhoneNumber: $("#hospitalPhoneNumber").val()
        };
        console.log(data);

        $.ajax({
            type: "POST",
            url: "/hospitalUser/join",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function (resp){
            console.log(resp);
            alert("회원가입이 완료되었습니다.");
            location.href = "/";
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    },

    update:function () {
        let data = {
            id: $("#id").val(),
            username: $("#username").val(),
            password: $("#password").val(),
            number: $("#number").val(),
            address: $("#address").val(),
            resident: $("#resident").val(),
            hospitalName: $("#hospitalName").val(),
            hospitalAddress: $("#hospitalAddress").val(),
            hospitalPhoneNumber: $("#hospitalPhoneNumber").val()
        };

        $.ajax({
            type: "PUT",
            url: "/hospitalUser/edit",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function (resp){
            console.log(resp);
            alert("회원수정이 완료되었습니다.");
            location.href = "/";
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    }
};

// CSS 추가
let hospitalUserStyle = `
    #btn-save, #btn-update {
        margin-top: 20px;
        padding: 10px 20px;
        background-color: #28a745;
        color: #ffffff;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }
    
    #btn-save:hover, #btn-update:hover {
        background-color: #218838;
    }
`;

let styleTag = document.createElement('style');
styleTag.type = 'text/css';
styleTag.appendChild(document.createTextNode(hospitalUserStyle));
document.head.appendChild(styleTag);

hospitalUserIndex.init();
