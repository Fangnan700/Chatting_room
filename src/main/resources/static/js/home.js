
let online_nums = document.getElementById("online_nums");
let user_info = document.getElementById("user_info");
let messages_content = document.getElementById("message_content");
let message_input = document.getElementById("message_input");

let online_number = 0;
let user_name;
let user_phone;
let message_history;

user_phone = getCookie("user_phone");
user_name = getCookie("user_name");





// 开始运行
init_page();




let websocket_host = "localhost:8080";

let socket = new ReconnectingWebSocket("ws://" + websocket_host + "/websocket/" + user_phone + "/" + encodeURI(user_name));

socket.onopen = function (e) {
    console.log("[websocket消息] 已连接到服务端");
}

socket.onclose = function (e) {
    console.log("[websocket消息] 连接已断开");
}

socket.onmessage = function (e) {
    let message_obj = JSON.parse(e.data);
    let rv_message = message_obj["message"];
    let from_name = message_obj["from_name"];
    let from_phone = message_obj["from_phone"];
    let time = formatDate(message_obj["time"]);

    console.log(e.data);

    if(from_name === "system" && rv_message === "1_close") {
        init_page();
        let who_leave = message_obj["who_leave"];
        message_history = messages_content.innerHTML;
        messages_content.innerHTML = message_history+ "<div style='width: 100%; text-align: center'><p style='color: dimgrey'>" + who_leave + "离开了小黑屋</p></div>";
    }

    if(from_name === "system" && rv_message === "1_open") {
        init_page();
        let who_join = message_obj["who_join"];
        message_history = messages_content.innerHTML;
        messages_content.innerHTML = message_history+ "<div style='width: 100%; text-align: center'><p style='color: dimgrey'>" + who_join + "加入了小黑屋</p></div>";
    }

    if(from_name !== "system" && from_phone !== user_phone) {
        console.log(time);
        console.log(rv_message);
        message_history = messages_content.innerHTML;
        messages_content.innerHTML = message_history+ "<div style='width: 100%; text-align: left'><p style='color: white'>>>&nbsp&nbsp" + time + "&nbsp&nbspFROM:&nbsp[" + from_name + "]<br><pre style='color: white; white-space: pre-wrap; font-size: 16px'>" + rv_message + "</pre></p></div>";
    }

    if(from_name !== "system" && from_phone === user_phone) {
        console.log(time);
        console.log(rv_message);
        message_history = messages_content.innerHTML;
        messages_content.innerHTML = message_history+ "<div style='width: 100%; text-align: end'><p style='color: white'>&nbsp&nbsp" + time + "&nbsp&nbspFROM:&nbsp[" + from_name + "]&nbsp&nbsp<<<br><pre style='color: white; white-space: pre-wrap; font-size: 16px'>" + rv_message + "</pre></p></div>";
    }

    messages_content.scrollTop = messages_content.scrollHeight;
}

socket.onerror = function (e) {
    console.log("[websocket消息] 出现异常:" );
}

message_input.onkeydown = function (event) {
    if(event.code === "Enter") {
        send();
    }
}


$("#user_id").attr("readOnly", true);
$("#user_name_input").attr("readOnly", true);
$("#user_phone_input").attr("readOnly", true);




function init_page() {
    let query_data = {
        "query_info": {
            "user_phone": user_phone
        }
    };
    let query_user_name = {
        url: "/query_user_info",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify(query_data),
        dataType: "json",
        success: function (result) {
            online_number = result["online_number"];
            user_info.innerText = "欢迎您：" + user_name;
            online_nums.innerText = "在线人数：" + online_number;
        }
    }
    $.ajax(query_user_name);
}

function getCookie(cname) {
    let name = cname + "=";
    let ca = document.cookie.split(';');
    for(let i=0; i<ca.length; i++) {
        let c = ca[i].trim();
        if (c.indexOf(name)===0)
            return c.substring(name.length,c.length);
    }
    return "";
}


function send() {
    let message = message_input.innerText;

    let time = Date.now();

    if(message !== "") {
        message_input.blur();
        message_input.innerText = "";

        let se_message = {
            "time": time,
            "message": message,
            "from_phone": getCookie("user_phone"),
            "from_name": user_name
        }
        socket.send(JSON.stringify(se_message));
    }
}

function logout() {
    socket.close();
    window.location.href = "/logout";
}



function query_user_info() {
    user_phone = getCookie("user_phone");
    let query_data = {
        "query_info": {
            "user_phone": user_phone
        }
    };
    let query_user_name = {
        url: "/query_user_info",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify(query_data),
        dataType: "json",
        success: function (result) {
            document.getElementById("user_id").value = result["user_id"];
            document.getElementById("user_name_input").value = result["user_name"];
            document.getElementById("user_phone_input").value = result["user_phone"];
        }
    }
    $.ajax(query_user_name);
}


function update_user_info() {

    document.getElementById("update_password").innerHTML =
        "                        <label for=\"user_password_input_1\" class=\"form-label\">旧密码</label>\n" +
        "                        <input type=\"password\" class=\"form-control\" id=\"user_password_input_0\">\n" +
        "                        <label for=\"user_password_input_1\" class=\"form-label\">新密码</label>\n" +
        "                        <input type=\"password\" class=\"form-control\" id=\"user_password_input_1\">\n" +
        "                        <label for=\"user_password_input_1\" class=\"form-label\">确认密码</label>\n" +
        "                        <input type=\"password\" class=\"form-control\" id=\"user_password_input_2\">"
    $("#user_name_input").attr("readOnly", false);
    $("#user_phone_input").attr("readOnly", false);
}


function update_info() {
    let user_id = document.getElementById("user_id").value;
    let user_name = document.getElementById("user_name_input").value;
    let user_phone = document.getElementById("user_phone_input").value;
    let old_password = document.getElementById("user_password_input_0").value;
    let new_password_1 = document.getElementById("user_password_input_1").value;
    let new_password_2 = document.getElementById("user_password_input_2").value;

    if(user_name === "") {
        $("#user_name_input").attr("readOnly", true);
        $("#user_phone_input").attr("readOnly", true);
        document.getElementById("update_password").innerHTML = "";
        alert("用户名不能为空");
    } else {
        if(user_phone === "") {
            $("#user_name_input").attr("readOnly", true);
            $("#user_phone_input").attr("readOnly", true);
            document.getElementById("update_password").innerHTML = "";
            alert("手机号不能为空");
        } else {
            if(old_password === "") {
                $("#user_name_input").attr("readOnly", true);
                $("#user_phone_input").attr("readOnly", true);
                document.getElementById("update_password").innerHTML = "";
                alert("请输入旧密码");
            } else {
                if(new_password_1 === "") {
                    $("#user_name_input").attr("readOnly", true);
                    $("#user_phone_input").attr("readOnly", true);
                    document.getElementById("update_password").innerHTML = "";
                    alert("请输入新密码");
                } else {
                    if(new_password_2 === "") {
                        $("#user_name_input").attr("readOnly", true);
                        $("#user_phone_input").attr("readOnly", true);
                        document.getElementById("update_password").innerHTML = "";
                        alert("请确认新密码");
                    } else {
                        if(new_password_1 !== new_password_2) {
                            $("#user_name_input").attr("readOnly", true);
                            $("#user_phone_input").attr("readOnly", true);
                            document.getElementById("update_password").innerHTML = "";
                            alert("两次密码不一致");
                        } else {

                            let request_info = {
                                "update_info": {
                                    "user_id": user_id,
                                    "user_name": user_name,
                                    "user_phone": user_phone,
                                    "old_password": old_password,
                                    "new_password_1": new_password_1,
                                    "new_password_2": new_password_2
                                }
                            }
                            let settings = {
                                url: "/update",
                                type: "post",
                                contentType: "application/json",
                                dataType: "json",
                                data: JSON.stringify(request_info),
                                success: function (result) {
                                    console.log(result);
                                    if (result["status"] === 1) {
                                        alert("信息修改成功！");
                                        window.history.go(0);
                                    } else {
                                        $("#user_name_input").attr("readOnly", true);
                                        $("#user_phone_input").attr("readOnly", true);
                                        document.getElementById("update_password").innerHTML = "";
                                        alert(result["msg"]);
                                    }
                                }
                            }
                            $.ajax(settings);
                        }
                    }
                }
            }
        }
    }
}





function formatDate(time = new Date()) {
    let date = new Date(time);
    let YY = date.getFullYear();
    let MM = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
    let DD = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
    let hh = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
    let mm = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
    let ss = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();

    // 这里修改返回时间的格式
    return YY + "-" + MM + "-" + DD + " " + hh + ":" + mm + ":" + ss;
}

