/**
 * Created by daivdyyl on 2017/4/13.
 */

//将operator设为全局变量
var operator_id = "";
var year = 666;
var app = angular.module("collectorApp",[]);


$(document).ready(function(){
    $.ajax({
        //将ajax异步设为同步
        async:false,
        url : rootUrl + "login/getUsername",
        method : "GET",
        dataType:"text",
        contentType:"application/x-www-form-urlencoded:charset=UTF-8",
        success:function(user){
            //user为一个json字符串，需要转换为json格式
            var obj = eval('(' + user + ')');
            operator_id = obj["username"];
            var name = obj["name"];
            $(".user-info").text(name);
        }
    })
});


app.controller("collectCtrl",function($scope, $http, $interval,$timeout){

    // 系统计时器
    var timer = $interval(function () {
        $scope.datetime = new Date();
    }, 1000);


    /**
     * 得到个人信息
     * 由于最外层取得的operate_id还是没有改变的，但是success后的operator_id改变了
     * 所以，里面再进行一次请求访问
     *
     * */
    // var url = rootUrl + "operatorController/getProfile?operator_id=";
    // $http.get(url + operator_id).success(function(response){
    //     var url = rootUrl + "operatorController/getProfile?operator_id=";
    //     $http.get(url + operator_id).success(function(response){
    //         $scope.profile = response[0];
    //         console.log($scope.profile.name);
    //     });
    //     // console.log("operaor_id:",operator_id);
    //     // console.log(response);
    //     // $scope.profile = response;
    // });

    $timeout(function () {
        var url = rootUrl + "operatorController/getProfile?operator_id=";
        $http.get(url + operator_id).success(function(response){
            $scope.profile = response[0];
        });
    },0.001);

    /**
     * 修改密码
     *
     * */
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
                var url = rootUrl + "operatorController/isOldpw?operator_id=" + operator_id + "&nowpw=" + nowpw;
                $http.get(url).success(function (response) {
                    var result = response;
                    if (result == 0) {
                        alert("当前密码不正确");
                    } else {
                        var url = rootUrl + "operatorController/changePw?operator_id=" + operator_id + "&newpw=" + newpw;
                        $http.get(url).success(function (response) {
                            alert("修改成功，请重新登录");
                            //修改成功后，注销，退出到登录页面重新登录
                            var url = rootUrl + "login/logout";
                            $http.get(url).success(function(response){
                                var result = response;
                                console.log(result);
                                window.location.href="car/auditPage/login.html";
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
     *  获取近一个月该收费员确认的逃费次数
     *
     * */
    var findTollUrl = rootUrl + 'statistic/getThisMonthOperatorFindDodgeTimes?operator=' + operator_id;
    $http.get(findTollUrl).success(function (response) {
        $scope.findTollTimes = response;
    }).error(function (data) {
        console.log(data);
    });


    /**
     *  获取近一个月该收费员取消的逃费次数
     *
     * */
    var cancelTollUrl = rootUrl + 'statistic/getThisMonthCancelDodgeTimes?operator=' + operator_id;
    $http.get(cancelTollUrl).success(function (response) {
        console.log("operator:",operator_id);
        $scope.cancelTollTimes = response;
    }).error(function (data) {
        console.log(data);
    });

    /**
     * 根据每月的确认逃费次数和取消逃费次数
     *
     *
     * */
        // 获取公司确认逃费次数/报警逃费次数按月变化数据
    var dodgeTollTimesUrl = rootUrl + 'statistic/getDodgeTollTimesData?year=' + year;
    $http.get(dodgeTollTimesUrl).success(function (response) {
        console.log(response);
        // 路径配置
        require.config({
            paths: {
                echarts: 'http://echarts.baidu.com/build/dist'
            }
        });
        var confirmTimes = response.confirmTimes;
        var alarmTimes = response.alarmTimes;
        require(
            [
                'echarts',
                'echarts/chart/line', // 使用柱状图就加载bar模块，按需加载
                'echarts/chart/bar'
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('echartsline'));
                var option = {
                    title: {
                        text: '系统报警次数／打击逃费次数',
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['报警逃费次数','打击逃费次数']
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true},
                            dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line','bar']},
                            restore : {show: true},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    xAxis: [
                        {
                            type: 'category',
                            boundaryGap: false,
                            data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            axisLabel: {
                                formatter: '{value} 次'
                            }
                        }
                    ],
                    series: [
                        {
                            "name": "系统报警次数",
                            "type": "line",
                            "data": [alarmTimes[0], alarmTimes[1], alarmTimes[2], alarmTimes[3], alarmTimes[4], alarmTimes[5], alarmTimes[6], alarmTimes[7], alarmTimes[8], alarmTimes[9], alarmTimes[10], alarmTimes[11]],
                            markPoint : {
                                data : [
                                    {type : 'max', name: '最大值'},
                                    {type : 'min', name: '最小值'}
                                ]
                            },
                            markLine : {
                                data : [
                                    {type : 'average', name: '平均值'}
                                ]
                            }
                        },
                        {
                            "name": "打击逃费次数",
                            "type": "line",
                            "data": [confirmTimes[0], confirmTimes[1], confirmTimes[2], confirmTimes[3], confirmTimes[4], confirmTimes[5], confirmTimes[6], confirmTimes[7], confirmTimes[8], confirmTimes[9], confirmTimes[10], confirmTimes[11]],
                            markPoint : {
                                data : [
                                    {type : 'max', name: '最大值'},
                                    {type : 'min', name: '最小值'}
                                ]
                            },
                            markLine : {
                                data : [
                                    {type : 'average', name: '平均值'}
                                ]
                            }
                        }
                    ]
                };

                // 为echarts对象加载数据
                myChart.setOption(option);
            }
        );
    }).error(function (data) {
        console.log(data);
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
            window.location.href="car/auditPage/login.html";
        });
    }
});