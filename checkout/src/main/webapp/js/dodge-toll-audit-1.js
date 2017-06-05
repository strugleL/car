/**
 * Created by Nicholas_Wang on 2016/11/16.
 */

var app = angular.module('auditApp1',[]);

var map = new BMap.Map("allmap");
map.enableScrollWheelZoom();
var point = new BMap.Point(113.264469, 23.134245);
map.centerAndZoom(point, 10);
// 添加带有定位的导航控件
var navigationControl = new BMap.NavigationControl({
    // 靠左上角位置
    anchor: BMAP_ANCHOR_TOP_LEFT,
    // LARGE类型
    type: BMAP_NAVIGATION_CONTROL_LARGE,
    // 启用显示定位
    enableGeolocation: true
});
map.addControl(navigationControl);

var truckDic = {};
var trffic_color = ['#00FF00', '#FFFF00', '#FF8000', '#FF0000'];
var geoc = new BMap.Geocoder(); // 获取地理位置
var operator_id = "";

/**
 * 新增，显示日历
 *
 * */
$(function () {
    $('#datetimepicker1').datetimepicker({
        format: 'yyyy-mm-dd hh:00',
        minView: 1,
        language: 'zh-CN',
    });
});
$(function () {
    $('#datetimepicker2').datetimepicker({
        format: 'yyyy-mm-dd hh:00',
        minView: 1,
        language: 'zh-CN',
    });
});

/**
 * 从session中得到username和userid
 *
 * */
$(document).ready(function(){
    $.ajax({
        url : rootUrl + "login/getUsername",
        method : "GET",
        dataType:"text",
        contentType:"application/x-www-form-urlencoded:charset=UTF-8",
        success:function(user){
            console.log(user);
            //user为一个json字符串，需要转换为json格式
            var obj = eval('(' + user + ')');
            operator_id = obj["username"];
            var name = obj["name"];
            $(".user-info").text(name);
        }
    })
});

app.controller('auditCtrl1',function ($scope, $http, $interval) {

    // 系统计时器
    var timer = $interval(function () {
        $scope.datetime = new Date();
    }, 1000);


    // 获取车辆监控信息, 每30秒请求一次
    // var url = rootUrl + "getDodgeTollAuditInfo?count=";
    // var dodgeTollAuditInfoGet = function (counter) {
    //     $http.get(url + counter).success(function (response) {
    //         $scope.carInfoArr = response;
    //     });
    // };
    // // 请求计数器
    // var counter = 1;
    // // 首次请求
    // dodgeTollAuditInfoGet(counter);
    // // 之后每3秒请求一次
    // var carInfoTimer = $interval(function () {
    //     counter++;
    //     $http.get(url + counter).success(function (response) {
    //         dodgeTollAuditInfoGet(counter);
    //         console.log('counter: ' + counter);
    //     });
    //
    // }, 15000);
    /**
     * 得到车辆详细信息
     *
     * */

    var url = rootUrl + "getDodgeTollAuditInfo";
    $http.get(url).success(function (response) {
        console.log('carInfoArr: ',response);
        $scope.carInfoArr = response;
    });

    /**
     * 新增，选择日期，通过日期搜索车辆信息
     *
     * */


    /**
     * 新增,通过车牌号搜索车辆信息,实际上是想通过更改carInfoArr，来更改后面的数据
     *
     *
     * */

    $scope.searchCarNumber=function(){
        // 分四种情况
        // 首先将其中undefined转换为""
        if ($scope.starttime == undefined){
            $scope.starttime = "";
        }
        if($scope.endtime == undefined){
            $scope.endtime = "";
        }
        if ($scope.carNumber == undefined){
            $scope.carNumber = "";
        }
        // 当车牌号不为空，日期为空时
        if ($scope.carNumber != "" && $scope.starttime == "" && $scope.endtime == ""){
            var carnumber = $scope.carNumber;
            var url = rootUrl + "getDodgeTollAuditInfoBycarNumber?carNumber=";
            $http.get(url + carnumber).success(function(response){
                console.log('carInfoArr: ',response);
                //判断，若输入错误，则弹框警告
                if (response.length == 0){
                    alert("对不起,没有对应的车辆信息");
                }
                else{
                    $scope.carInfoArr = response;
                }
            });
        }
        //当车牌号为空，日期不为空时
        else if ($scope.starttime != "" && $scope.carNumber=="" && $scope.endtime != "" ){
            var start = $scope.starttime;
            var end = $scope.endtime;
            var url = rootUrl + "getDodgeTollAuditInfoByTime" + "?start=" + start + "&end=" + end;
            $http.get(url).success(function (response) {
                console.log("time",response);
                if (response.length == 0){
                    alert("对不起,没有对应的车辆信息");
                }
                else{
                    $scope.carInfoArr = response;
                }
            })
        }

        //当车牌号和日期都不为空时
        else if ($scope.carNumber != "" && $scope.starttime != "" && $scope.endtime != ""){
            var start = $scope.starttime;
            var end = $scope.endtime;
            var carnumber = $scope.carNumber;
            var url = rootUrl + "getDodgeTollAuditInfoByTimeAndcarNumber?carNumber=" + carnumber + "&start=" + start + "&end=" + end;
            $http.get(url).success(function (response) {
                console.log("time",response);
                if (response.length == 0){
                    alert("对不起,没有对应的车辆信息");
                }
                else{
                    $scope.carInfoArr = response;
                }
            })
        }

        //当车牌号和日期都不为空时
        // if ($scope.starttime != "" && $scope.endtime != "" && $scope.carNumber != ""){
        //
        // }
        //当车牌号和日期都为空时
        else{
            var url = rootUrl + "getDodgeTollAuditInfo";
            $http.get(url).success(function (response) {
                console.log('carInfoArr: ',response);
                $scope.carInfoArr = response;
            });
        }

    };


    // 对比车辆路线
    $scope.carTrack = function (carNo) {

        map.clearOverlays();
        var carSearchUrl = rootUrl + 'search?carNo=';
        $http.get(carSearchUrl + carNo).success(function (response) {
            if (response['pathInfo'].length == 0) {
                alert("对不起,没有对应的车辆信息");
            } else {
                // 渲染数据
                console.log(response);
                var tollInfo = response['tollInfo'];
                var pathInfo = response['pathInfo'];
                var lastIndex = pathInfo.length - 1;

                var startLng = pathInfo[0].lng;
                var endLng = pathInfo[lastIndex].lng;
                var startLat = pathInfo[0].lat;
                var endLat = pathInfo[lastIndex].lat;


                // 遍历每辆车的路线
                var arrPoint = []; //线路点
                for (var index = 0; index < pathInfo.length; index++) {
                    arrPoint.push(new BMap.Point(pathInfo[index].lng, pathInfo[index].lat));
                }
                var polyline1 = new BMap.Polyline(arrPoint, {
                    strokeColor: "blue",
                    strokeWeight: 5,
                    strokeOpacity: 0.5
                });   //创建折线

                var InfoOpts = {
                    width : 200,     // 信息窗口宽度
                    height: 100,     // 信息窗口高度
                    title : "到达此点时间" , // 信息窗口标题
                    enableMessage:true //设置允许信息窗发送短息
                    // message:"亲耐滴，晚上一起吃个饭吧？戳下面的链接看下地址喔~"
                };

                polyline1.addEventListener('mouseover',function (e) {

                    for (var i = 0; i < pathInfo.length; i++) {
                        var text = " ";
                        var lng1 = Number(pathInfo[i].lng);
                        var lng2 = Number(e.point.lng);
                        var lat1 = Number(pathInfo[i].lat);
                        var lat2 = Number(e.point.lat);
                        var compare1 = Math.abs(lng1-lng2);
                        var compare2 = Math.abs(lat1-lat2);
                        if ( compare1 <= 0.01 && compare2 <= 0.01) {
                            text = pathInfo[i].time;
                        } else {
                            continue;
                            // text = e.point.lng + ' ' + e.point.lat + ' ' + compare1 + ' ' + compare2;
                        }
                        var infoWindow = new BMap.InfoWindow(text, InfoOpts);
                        // console.log('mouse over ' + e.point.lat);
                        map.openInfoWindow(infoWindow, e.point); //开启信息窗口
                    }

                });

                polyline1.addEventListener('mouseout', function (e) {
                    map.closeInfoWindow();
                });

                map.addOverlay(polyline1);   //增加折线

                // 在 lastindex/2 位置车牌
                var halfindex = parseInt(lastIndex / 2);
                var carNoPoint = new BMap.Point(pathInfo[halfindex].lng, pathInfo[halfindex].lat);
                var carNoOpts = {
                    position: carNoPoint,    // 指定文本标注所在的地理位置
                    offset: new BMap.Size(10, -10)    //设置文本偏移量
                };
                var carNoLabel = new BMap.Label(carNo, carNoOpts);  // 创建文本标注对象
                carNoLabel.setStyle({
                    color: "red",
                    fontSize: "10px",
                    height: "15px",
                    lineHeight: "15px",
                    fontFamily: "微软雅黑"
                });
                map.addOverlay(carNoLabel);
                // map.panTo(carNoPoint);
                // 在起点和终点设置名称
                // 行驶轨迹起点和终点
                var point1 = new BMap.Point(startLng, startLat);
                var point2 = new BMap.Point(endLng, endLat);
                var opts1 = {
                    position: point1,    // 指定文本标注所在的地理位置
                    offset: new BMap.Size(10, -10)    //设置文本偏移量
                };
                var opts2 = {
                    position: point2,    // 指定文本标注所在的地理位置
                    offset: new BMap.Size(10, -10)    //设置文本偏移量
                };

                var label1 = new BMap.Label(carNo + "OBU轨迹·起", opts1);  // 创建文本标注对象
                var label2 = new BMap.Label(carNo + "OBU轨迹·终", opts2);  // 创建文本标注对象
                label1.setStyle({
                    color: "red",
                    fontSize: "10px",
                    height: "15px",
                    lineHeight: "15px",
                    fontFamily: "微软雅黑"
                });
                label2.setStyle({
                    color: "red",
                    fontSize: "10px",
                    height: "15px",
                    lineHeight: "15px",
                    fontFamily: "微软雅黑"
                });
                map.addOverlay(label1);
                map.addOverlay(label2);

                // 获取起点终点地理位置
                geoc.getLocation(point1, function (rs) {
                    var addComp = rs.addressComponents;

                    $scope.$apply(function () {
                        $scope.startLocation = addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
                    });


                    // console.log('start location: ' + startLocation);
                    // alert(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
                });
                geoc.getLocation(point2, function (rs) {
                    var addComp = rs.addressComponents;
                    $scope.$apply(function () {
                        $scope.endLocation = addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
                    });

                    // console.log('end location: ' + endLocation);
                    // alert(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
                });

                // 收费站起点和终点
                var tspoint = new BMap.Point(tollInfo.tollArray[0].lng, tollInfo.tollArray[0].lat);
                var tepoint = new BMap.Point(tollInfo.tollArray[1].lng, tollInfo.tollArray[1].lat);
                // 绘制导航路线
                addTruckLushu(0, tspoint, tepoint, 3);

                // 在收费站起点和终点设置名称
                var tollopts1 = {
                    position: tspoint,    // 指定文本标注所在的地理位置
                    offset: new BMap.Size(10, -10)    //设置文本偏移量
                };
                var tollopts2 = {
                    position: tepoint,    // 指定文本标注所在的地理位置
                    offset: new BMap.Size(10, -10)    //设置文本偏移量
                };

                var tolllabel1 = new BMap.Label(carNo + "收费公路·起", tollopts1);  // 创建文本标注对象
                var tolllabel2 = new BMap.Label(carNo + "收费公路·终", tollopts2);  // 创建文本标注对象
                tolllabel1.setStyle({
                    color: "red",
                    fontSize: "10px",
                    height: "15px",
                    lineHeight: "15px",
                    fontFamily: "微软雅黑"
                });
                tolllabel2.setStyle({
                    color: "red",
                    fontSize: "10px",
                    height: "15px",
                    lineHeight: "15px",
                    fontFamily: "微软雅黑"
                });
                map.addOverlay(tolllabel1);
                map.addOverlay(tolllabel2);

                var points = [point1, point2,tspoint,tepoint];
                var view = map.getViewport(eval(points));
                var mapZoom = view.zoom;
                var centerPoint = view.center;
                map.centerAndZoom(centerPoint,mapZoom);

            }

        }).error(function (data) {
            console.log(data);
        });

    };

    // 展示车辆详细信息
    $scope.getCarInfo = function (carNo) {

        var url = rootUrl + 'getDodgeTollCarInfo?carNo=';
        $http.get(url + carNo).success(function (response) {
            if (response.length == 0) {
                alert('没有此车辆信息');
            }
            console.log('车辆信息: ' + response);
            $scope.carInfo = response;

        }).error(function (data) {
            console.log(data);
        })
        // defaulttype = $scope.carInfo.behavior;
        // $scope.selectType = defaulttype;
        // $scope.names=["倒卡和遮挡卡逃费","两车交换卡逃费","记重作假逃费","假冒优惠车辆逃费","强行冲岗逃费","中途取得卡逃费","套牌逃费"];
    };

    /**
     * 新增,跳转到选择逃费类型页面
     *
     *
     * */
    $scope.openwin = function(laneexSerialNo){
        window.open ("http://localhost:8080/car/auditPage/collector/checkbox.html?laneexSerialNo="+laneexSerialNo, 'newwindow', 'height=300, width=600, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
    }

    /**
     * 详情表格中确认按钮功能实现
     *
     *
     * */
    $scope.confirmSuspicion = function (carNo, operate) {
        console.log("carNo: " + carNo);
        console.log("operation: " + operate);
        var url = rootUrl + 'dodgeTollCarConfirm' + '?carNo=' + carNo + '&operation=' + operate +"&operator=" + operator_id;
        $http.get(url).success(function (response) {

            var carArray = $scope.carInfoArr;
            for (var i in carArray) {
                // console.log('i: ' + i);
                if (carArray[i].exRecordNo == carNo) {
                    console.log('find carNO: ' + carArray[i].carNo);
                    carArray[i].action = operate;
                    // carArray[i].flag = 'S';
                    break;
                }
            }

            $scope.carInfo.action = operate;
            //刷新当前页面
            location.reload();
        }).error(function (data) {
            console.log(data)
        });
    };

    /**
     *
     * 新增，按车牌号搜索历史记录，跳转到历史记录页面
     *
     * */
    $scope.searchHistory=function(carNo,history){
        // window.location.href(encodeURI("history.html?carNo=" + carNo));
        // window.location = "history.html?carNo=" + encodeURI(carNo);
        if (history == 0){
            alert("无历史逃费记录");

        }else{
            window.open("http://localhost:8080/car/auditPage/collector/history.html?carNo=" + encodeURI(carNo));
        }

    };


    // 修改车牌号码
    var statu = 0;
    $scope.changeCarNo = function ($event, carN, laneexSerialNo) {
        console.log(carN);
        var carNo = document.getElementById(carN);
        console.log('carNo:' + carNo.toString());
        var edit = $event.target;
        console.log('eidt:' + edit.toString());
        console.log('status:' + statu);
        if (statu == 0) {
            edit.innerHTML = '保存';
            // carNo.innerHTML = "";
            carNo.innerHTML = '<input type="text" class="item_input" style="width: 63px" value="' + carN + '">';
            statu = 1;
        } else {
            edit.innerHTML = '纠正车牌';
            var newCarNo = document.getElementsByClassName("item_input")[0].value;
            if ($scope.carInfo != null) {
                $scope.carInfo.carNo = newCarNo;
            }

            //正则表达式匹配是否符合车牌格式
            var rex = /[\u4e00-\u9fa5][0-9A-Z]{3}(\d{3})/;
            var flag = newCarNo.match(rex);
            if (flag == null){
                alert("对不起，输入车牌格式不正确")
            }
            else {
                var url = rootUrl + 'changeCarNo' + '?laneexSerialNo=' + laneexSerialNo + '&newCarNo=' + newCarNo;
                carNo.innerHTML = newCarNo;
                statu = 0;
                $http.get(url).success(function (response) {
                    var updateurl = rootUrl + "getDodgeTollAuditInfo";
                    // url = 'http://localhost:8080/ServiceManagement/appcar/getDodgeTollCarInfo';

                    $http.get(updateurl).success(function (response) {
                        // console.log(response);
                        $scope.carInfoArr = response;

                    });
                    console.log('修改成功');
                }).error(function (data) {
                    console.log(data);
                });
            }
        }

    };

    $scope.$on('ngRepeatFinished', function (ngRepeatFinishedEvent) {
        // 下面是在table render完成后执行的js
        $(function () {
            var scrtime;
            var $ul = $("#con ul");
            var liFirstHeight = $ul.find("li:first").height();//第一个li的高度
            console.log('li First: '+ liFirstHeight);
            $ul.css({ top: "-" + liFirstHeight - 20 + "px" });//利用css的top属性将第一个li隐藏在列表上方	 因li的上下padding:10px所以要-20
            // $("#con").hover(function () {
            //     $ul.pause();//暂停动画
            //     clearInterval(scrtime);
            // }, function () {
            //     $ul.resume();//恢复播放动画
            //     scrtime = setInterval(function scrolllist() {
            //         //动画形式展现第一个li
            //         $ul.animate({ top: 0 + "px" }, 3000, function () {
            //             //动画完成时
            //             $ul.find("li:last").prependTo($ul);//将ul的最后一个剪切li插入为ul的第一个li
            //             liFirstHeight = $ul.find("li:first").height();//刚插入的li的高度
            //             $ul.css({ top: "-" + liFirstHeight - 20 + "px" });//利用css的top属性将刚插入的li隐藏在列表上方  因li的上下padding:10px所以要-20
            //         });
            //     }, 6000);
            //
            // }).trigger("mouseleave");//通过trigger("mouseleave")函数来触发hover事件的第2个函数
        // });
        // $(function () {
        //     $('.dowebok').vTicker({
        //         showItems: 4,
        //         pause: 4000
        //     });
        });

    });

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

// 绘制路线
function addTruckLushu(truck_id, start_p, end_p, traffic) {

    //收费站之间的路书
    var lushu;
    // 实例化一个驾车导航用来生成路线
    var routePolicy = [BMAP_DRIVING_POLICY_LEAST_TIME, BMAP_DRIVING_POLICY_LEAST_DISTANCE, BMAP_DRIVING_POLICY_AVOID_HIGHWAYS];
    var drv = new BMap.DrivingRoute('广州', {
        onSearchComplete: function (res) {
            if (drv.getStatus() == BMAP_STATUS_SUCCESS) {
                var arrPois = []; //线路点
                var plan = res.getPlan(0);
                for (var j = 0; j < plan.getNumRoutes(); j++) {
                    var route = plan.getRoute(j);
                    arrPois = arrPois.concat(route.getPath());
                }
                map.addOverlay(new BMap.Polyline(arrPois, {strokeColor: trffic_color[traffic]}));

                lushu = new BMapLib.LuShu(map, arrPois, {
                    defaultContent: "收费站线路",
                    icon: new BMap.Icon('truck_mini.png', new BMap.Size(52, 26), {anchor: new BMap.Size(27, 13)}),
                    speed: 4500
                });
                truckDic[truck_id] = {"lushu": lushu}
            }
        }
    });
    drv.setPolicy(routePolicy[1]);
    drv.search(start_p, end_p);
}

app.directive('onFinishRenderFilters', function ($timeout) {
    console.log('invoke directive');
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            console.log('scope last: ' + scope.$last);
            if (scope.$last === true) {
                $timeout(function () {
                    scope.$emit('ngRepeatFinished');
                });
            }
        }
    };
});

