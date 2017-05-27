/**
 * Created by daivdyyl on 2017/5/3.
 */
/**
 *
 * 通过函数得到，地址栏上传递的参数
 *
 * */
function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null){
        return r[2];
    }
    return null;
}
var laneexSerialNo = GetQueryString("laneexSerialNo");

var app = angular.module("selectApp",[]);

app.controller("selectCtrl",function($scope,$http){

    //通过循环找到选中的type
    //目前这种情况：最多有两种逃费，且两种逃费分别是不同类型的逃费
    $scope.updateSelect = function(){
        var passCardDodge = "";
        var weightDodge = "";
        for (var t in $scope.type){
            switch (t){
                case "p0":
                    passCardDodge = "0";
                    break;
                case "p1":
                    passCardDodge = "1";
                    break;
                case "p2":
                    passCardDodge = "2";
                    break;
                case "p3":
                    passCardDodge = "3";
                    break;
                case "p4":
                    passCardDodge = "4";
                    break;
                case "w0":
                    weightDodge = "0";
                    break;
                case "w1":
                    weightDodge = "1";
                    break;
                case "w2":
                    weightDodge = "2";
                    break;
            }
        }
        var url = rootUrl+"updateSelect?laneexSerialNo=" + laneexSerialNo + "&passCardDodge=" + passCardDodge + "&weightDodge=" + weightDodge;
        $http.get(url).success(function(response){
            console.log('result: ',response);
            var result =response;
            if (result == 1){
                alert("修改逃费类型成功");
            }else {
                alert("修改逃费类型失败，请重新修改");
            }
        })
    }
});