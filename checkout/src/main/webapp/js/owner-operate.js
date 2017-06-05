/**
 * Created by daivdyyl on 2017/4/27.
 */

//将operator设为全局变量
var owner_id = "";
var app = angular.module("ownerApp",[]);

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
            owner_id = user;
            console.log(owner_id);
            // var name = obj["name"];
            // $(".user-info").text(name);
        }
    })
});

app.controller("ownerContrl",function($scope, $http, $interval){
    // 系统计时器
    var timer = $interval(function () {
        $scope.datetime = new Date();
    }, 1000);

    //通过姓名或id搜索收费员
    $scope.searchCollector = function(){
        if ($scope.operatorNumber == undefined){
            $scope.operatorNumber = "";
        }
        else if ($scope.operatorName == undefined){
            $scope.operatorName = "";
        }
        else if ($scope.operatorNumber != "" && $scope.operatorName == ""){
            var collectNumber = $scope.operatorNumber;
            var url = rootUrl + "ownerController/getInfoById?collectNumber=";
            $http.get(url + collectNumber).success(function(response){
                console.log('coInfoArr: ',response);
                //判断，若response==0，则返回错误
                if (response == 0){
                    alert("对不起,没有对应的收费员信息");
                }else {
                    $scope.coInfoArr = response;
                }
            });
        }
        else if ($scope.operatorNumber == "" && $scope.operatorName != ""){
            var collectName = $scope.operatorName;
            var url = rootUrl + "ownerController/getInfoByName?collectName=";
            $http.get(url + collectName).success(function(response){
                console.log('coInfoArr: ',response);
                //判断，若response==0，则返回错误
                if (response == 0){
                    alert("对不起,没有对应的收费员信息");
                }else {
                    $scope.coInfoArr = response;
                }
            });
        }else if ($scope.operatorNumber != "" && $scope.operatorName != ""){
            var collectNumber = $scope.operatorNumber;
            var collectName = $scope.operatorName;
            var url = rootUrl + "ownerController/getInfoById_Name?collectNumber=" + collectNumber + "&collectName=" + collectName;
            $http.get(url).success(function(response){
                console.log('coInfoArr: ',response);
                //判断，若response==0，则返回错误
                if (response == 0){
                    alert("对不起,没有对应的收费员信息");
                }else {
                    $scope.coInfoArr = response;
                }
            });
        }
    }

    //通过id删除收费员
    $scope.deleteOperator = function(operator_id){
        var url = rootUrl + "ownerController/deleteOperator?operator_id=" + operator_id;
        $http.get(url).success(function(response){
            if (response == 0){
                alert("删除操作错误");
            }else {
                alert("删除成功");
                location.reload("../auditPage/owner/collectorInfo.html");
            }
        })
    }

    //添加收费员
    $scope.addOperatorInfo = function(){
        //判断输入是否为空
        if ($scope.operator_id == undefined || $scope.operator_id == "" ||
            $scope.name == undefined || $scope.name == ""){
            alert("工号，姓名不能为空")
        }
        //密码初始化123456
        if ($scope.password == undefined || $scope.password == ""){
            $scope.password = "123456";
        }
        //将undefined转换为""
        if ($scope.gender == undefined){
            $scope.gender = "";
        }
        if ($scope.birth == undefined){
            $scope.birth = "";
        }
        if ($scope.political == undefined){
            $scope.political = "";
        }
        if ($scope.minzu == undefined){
            $scope.minzu = "";
        }
        if ($scope.phone == undefined){
            $scope.phone = "";
        }
        if ($scope.address == undefined){
            $scope.address = "";
        }
        if ($scope.email == undefined){
            $scope.email = "";
        }
        if ($scope.status == undefined){
            $scope.status = "";
        }
        if ($scope.work_age == undefined){
            $scope.work_age = "";
        }

        // if (){
        //
        // }
        var url = rootUrl + "ownerController/addOperatorInfo";
        $http({
            method:"post",
            url : url,
            params : {
                "operator_id":$scope.operator_id,
                "name":$scope.name,
                "gender":$scope.gender,
                "birth":$scope.birth,
                "political":$scope.political,
                "minzu":$scope.minzu,
                "phone":$scope.phone,
                "address":$scope.address,
                "email":$scope.email,
                "password":$scope.password,
                "status":$scope.status,
                "work_age":$scope.work_age
            }
        }).success(function(response){
            if (response == 1){
                alert("添加成功")
            } else {
                alert("添加失败,工号重复");
            }
        }).error(function(data){
            console.log(data);
            alert("添加失败，请输入正确的工号");
        })
    };

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
                var url = rootUrl + "ownerController/isOldpw?owner_id=" + owner_id + "&nowpw=" + nowpw;
                $http.get(url).success(function (response) {
                    var result = response;
                    if (result == 0) {
                        alert("当前密码不正确");
                    } else {
                        var url = rootUrl + "ownerController/changePw?owner_id=" + owner_id + "&newpw=" + newpw;
                        $http.get(url).success(function (response) {
                            alert("修改成功,请重新登录");
                            //修改成功后，注销，退出到登录页面重新登录
                            var url = rootUrl + "login/logout";
                            $http.get(url).success(function(response){
                                var result = response;
                                console.log(result);
                                window.location.href="http://localhost:8080/car/auditPage/login.html";
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

    /**
     * 注销，跳转到登录页面
     *
     *
     * */

    $scope.logout = function(){
        var url = rootUrl + "login/logout";
        $http.get(url).success(function(response){
            var result = response;
            console.log(result);
            window.location.href="http://localhost:8080/car/auditPage/login.html";
        });
    }
});