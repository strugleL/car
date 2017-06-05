/**
 * Created by daivdyyl on 2017/6/1.
 */

var app = angular.module('administratorApp', []);

//将ID设为全局变量
var admin_id = "";
$(document).ready(function(){
    $.ajax({
        //将ajax异步设为同步
        async:false,
        url : rootUrl + "login/getId",
        method : "GET",
        dataType:"text",
        contentType:"application/x-www-form-urlencoded:charset=UTF-8",
        success:function(user){
            //user为一个json字符串，需要转换为json格式
            // var obj = eval('(' + user + ')');
            // operator_id = obj["username"];
            admin_id = user;
            console.log(admin_id);
            // var name = obj["name"];
            // $(".user-info").text(name);
        }
    })
});

app.controller('administratorCtrl', function ($scope, $http, $interval, $timeout) {
    // 系统计时器
    var timer = $interval(function () {
        $scope.datetime = new Date();
    }, 1000);

    //修改密码
    $scope.changePw = function() {

        var nowpw = $scope.nowpw;
        var newpw = $scope.newpw;
        var confirmpw = $scope.confirmpw;
        //首先判断是否为空
        if (nowpw == undefined || nowpw == "" || newpw == undefined || newpw == "" || confirmpw == undefined || confirmpw == "") {
            alert("密码输入不能为空");
        } else {
            if (newpw == nowpw) {
                alert("新旧密码相同");
            }
            else if (newpw != confirmpw) {
                alert("新密码与确认密码不相同");
            }
            else {
                //判断当前密码是否正确
                var url = rootUrl + "admin/isOldpw?admin_id=" + admin_id + "&nowpw=" + nowpw;
                $http.get(url).success(function (response) {
                    var result = response;
                    if (result == 0) {
                        alert("当前密码不正确");
                    } else {
                        var url = rootUrl + "admin/changePw?admin_id=" + admin_id + "&newpw=" + newpw;
                        $http.get(url).success(function (response) {
                            alert("修改成功,请重新登录");
                            //修改成功后，注销，退出到登录页面重新登录
                            var url = rootUrl + "login/logout";
                            $http.get(url).success(function(response){
                                var result = response;
                                console.log(result);
                                window.location.href="../../auditPage/login.html";
                            });
                        }).error(function (data) {
                            console.log(data);
                            alert("修改失败");
                        });
                    }
                });
            }
        }
    };
});
