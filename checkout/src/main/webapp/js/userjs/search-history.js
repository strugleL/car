/**
 * Created by daivdyyl on 2017/4/3.
 */
var app1 = angular.module('historyApp',[]);
function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  decodeURI(r[2]); return null;
}
var carNo = decodeURI(GetQueryString("carNo"));


app1.controller('historyCtrl', function ($scope, $http) {

    var url = rootUrl + "getDodgeTollhistory" + "?carNo=" + carNo;
    $http.get(url).success(function (response) {
        console.log('carInfoArr: ',response);
        $scope.carInfoArr = response;
    });
});