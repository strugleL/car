/**
 * Created by daivdyyl on 2017/4/13.
 */
var app = angular.module("loginApp",[]);
app.controller("loginCtrl",function($scope,$http){

    $scope.login = function(){

        if ($scope.username == undefined || $scope.password == undefined || $scope.username == "" || $scope.password == ""){
            alert("用户名和密码是必须的");
        }if ($scope.identity == "" || $scope.identity == undefined){
            alert("请选择登录身份");
        } else{
                $http({
                method : "post",
                url : rootUrl + "login/collectorLogin",
                params : {
                    "username":$scope.username,
                    "password":$scope.password,
                    "identity":$scope.identity
                }
            }).success(function(response){
                console.log("result",response);
                var result = response;
                //判断，不同result，跳转不同的页面
                if (result == "1"){
                    // window.location.href="http://localhost:8080/car/auditPage/index.html";
                    //     // ?operator_id=" + encodeURI(user.username)
                    window.location.href="../auditPage/collector/index.html";
                }
                else if (result == '2'){
                    window.location.href="../auditPage/owner/owner_index.html";
                }
                else{
                    alert("用户名密码错误")
                }
            });
        }

        // $cookieStore.put("operator_id",user.username);
    }
});