let msg_frame = document.getElementById("msg_frame");
let phone_input = document.getElementById("phone_input");
let password_input = document.getElementById("password_input");


function login_submit() {
    let user_phone = phone_input.value;
    let user_password = password_input.value;

    if (user_phone !== "") {
        if (user_password !== "") {
            login(user_phone, user_password);
        } else {
            alert("密码不能为空！！！");
        }
    } else {
        alert("手机号不能为空！！！");
    }
}

function login(user_phone, user_password) {
    let request_info = {
        "login_info": {
            "user_phone": user_phone,
            "user_password": user_password
        }
    }
    let settings = {
        url: "/login",
        type: "post",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(request_info),
        success: function (result) {
            console.log(result);
            if (result["status"] === 1) {
                show_success_msg(result["msg"]);
                setTimeout(window.location.href = "/html/home.html", 5000);
            } else {
                show_error_msg(result["msg"]);
            }
        }
    }
    $.ajax(settings);
}

function register() {
    window.location.href = "/register";
}

function show_success_msg(msg) {
    msg_frame.innerHTML = "<div class=\"alert alert-success msg\" role=\"alert\">" + msg + "</div>";
}

function show_error_msg(msg) {
    msg_frame.innerHTML = "<div class=\"alert alert-danger msg\" role=\"alert\">" + msg + "</div>";
}

function reset_msg() {
    msg_frame.innerHTML = "";
}