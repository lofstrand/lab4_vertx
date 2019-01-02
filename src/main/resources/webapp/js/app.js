angular.module('DataApp', []).config(['$routeProvider', function ($routeProvider) {
    $routeProvider.
        when('/', {templateUrl: './tpl/lists.html', controller: ListCtrl}).
        when('/add-data', {templateUrl: './tpl/add-data.html', controller: AddCtrl}).
        when('/graph/:id', {templateUrl: './tpl/graph.html', controller: Graph1Ctrl}).
        when('/graph2/:id', {templateUrl: './tpl/graph2.html', controller: Graph2Ctrl}).
        when('/viewgraph/:id', {templateUrl: './tpl/viewgraph.html', controller: View1Ctrl}).
        when('/viewgraph2/:id', {templateUrl: './tpl/viewgraph2.html', controller: View2Ctrl}).
        when('/edit/:id', {templateUrl: './tpl/edit.html', controller: DeleteCtrl});//.
        //otherwise({redirectTo: './'});
}]);

function ListCtrl($scope, $http) {
    $http.get('/api/data').success(function (data) {
        $scope.data = data;
    });
}

function View1Ctrl($scope, $http, $location, $routeParams) {
    var id = $routeParams.id;
    $scope.activePath = null;

    $http.get('/api/data/' + id).success(function (datan) {
        $scope.datas = datan;

        var limit = 50000;
        var y = 100;
        var data = [];
        var dataSeries = { type: "line" };
        var dataPoints = [];
        var title;

        var dataS = datan.data;

        for( var i = 1; i < dataS.length; i++) {
            dataPoints.push({
                x: new Date(dataS[i].date),
                y: dataS[i].value
            });
        }

        dataSeries.dataPoints = dataPoints;
        data.push(dataSeries);

        var options = {
            zoomEnabled: true,
            animationEnabled: true,
            title: {
                text: "Try Zooming - Panning"
            },
            axisY: {
                title: "bpm",
                includeZero: false,
                lineThickness: 1
            },
            data: data  // random data
        };

        var chart = new CanvasJS.Chart("chartContainer", options);
        var startTime = new Date();
        chart.render();
        var endTime = new Date();
        //document.getElementById("timeToRender").innerHTML = "Time to Render: " + (endTime - startTime) + "ms";
    });

    $scope.share_graph = function () {

        var url = document.location.href;

        $http.post('/api/sharegraph', url).success(function () {
            $scope.activePath = $location.path('/');
        });
    };
}


function View2Ctrl($scope, $http, $location, $routeParams) {
    var id = $routeParams.id;
    $scope.activePath = null;

    $http.get('/api/data/' + id).success(function (datan) {
        $scope.datas = datan;

        var dataPoints = [];

        var chart = new CanvasJS.Chart("chartContainer", {
            animationEnabled: true,
            theme: "light2",
            title: {
                text: "BPM count"
            },
            axisY: {
                title: "Count",
                titleFontSize: 24
            },
            data: [{
                type: "column",
                yValueFormatString: "#,### Units",
                dataPoints: dataPoints
            }]
        });


        var datas = datan.data;

        var chartData = [];
        for(var i = 0; i < 9; i++) {
            chartData[i] = 0;
        }

        for (var i = 0; i < datas.length; i++) {
            var v = datas[i].value;
            if(v < 40) {
                chartData[0]++;
            } else if(v < 60) {
                chartData[1]++;
            }else if(v < 80) {
                chartData[2]++;
            }else if(v < 100) {
                chartData[3]++;
            }else if(v < 120) {
                chartData[4]++;
            }else if(v < 140) {
                chartData[5]++;
            }else if(v < 160) {
                chartData[6]++;
            } else if(v < 180) {
                chartData[7]++;
            } else if(v < 200) {
                chartData[8]++;
            }
        }

        for(var j = 0; j < chartData.length; j++) {
            dataPoints.push({
                x: (j+1)*20,
                y: chartData[j]
            });
        }
        chart.render();
    });


    $scope.share_graph = function () {

        var url = document.location.href;

        $http.post('/api/sharegraph', url).success(function () {
            $scope.activePath = $location.path('/');
        });
    };

    //var urlParams = new URLSearchParams(window.location.search);
    //var id = urlParams.get('id');
    //console.log(id)
    //$.getJSON("http://localhost:8080/api/data/" + id, addData);
}


function Graph1Ctrl($scope, $http, $location, $routeParams) {
    var id = $routeParams.id;
    $scope.activePath = null;

    $http.get('/api/data/' + id).success(function (datan) {
        $scope.datas = datan;

            var limit = 50000;
            var y = 100;
            var data = [];
            var dataSeries = { type: "line" };
            var dataPoints = [];
            var title;

            var dataS = datan.data;

            for( var i = 1; i < dataS.length; i++) {
                dataPoints.push({
                    x: new Date(dataS[i].date),
                    y: dataS[i].value
                });
            }

            dataSeries.dataPoints = dataPoints;
            data.push(dataSeries);

            var options = {
                zoomEnabled: true,
                animationEnabled: true,
                title: {
                    text: "Try Zooming - Panning"
                },
                axisY: {
                    title: "bpm",
                    includeZero: false,
                    lineThickness: 1
                },
                data: data  // random data
            };

            var chart = new CanvasJS.Chart("chartContainer", options);
            var startTime = new Date();
            chart.render();
            var endTime = new Date();
            //document.getElementById("timeToRender").innerHTML = "Time to Render: " + (endTime - startTime) + "ms";
    });
}

function Graph2Ctrl($scope, $http, $location, $routeParams) {
    var id = $routeParams.id;
    $scope.activePath = null;

    $http.get('/api/data/' + id).success(function (datan) {
        $scope.datas = datan;

        var dataPoints = [];

        var chart = new CanvasJS.Chart("chartContainer", {
            animationEnabled: true,
            theme: "light2",
            title: {
                text: "BPM count"
            },
            axisY: {
                title: "Count",
                titleFontSize: 24
            },
            data: [{
                type: "column",
                yValueFormatString: "#,### Units",
                dataPoints: dataPoints
            }]
        });


        var datas = datan.data;

        var chartData = [];
        for(var i = 0; i < 9; i++) {
            chartData[i] = 0;
        }

        for (var i = 0; i < datas.length; i++) {
            var v = datas[i].value;
            if(v < 40) {
                chartData[0]++;
            } else if(v < 60) {
                chartData[1]++;
            }else if(v < 80) {
                chartData[2]++;
            }else if(v < 100) {
                chartData[3]++;
            }else if(v < 120) {
                chartData[4]++;
            }else if(v < 140) {
                chartData[5]++;
            }else if(v < 160) {
                chartData[6]++;
            } else if(v < 180) {
                chartData[7]++;
            } else if(v < 200) {
                chartData[8]++;
            }
        }

        for(var j = 0; j < chartData.length; j++) {
            dataPoints.push({
                x: (j+1)*20,
                y: chartData[j]
            });
        }
        chart.render();
    });

    //var urlParams = new URLSearchParams(window.location.search);
    //var id = urlParams.get('id');
    //console.log(id)
    //$.getJSON("http://localhost:8080/api/data/" + id, addData);
}

function AddCtrl($scope, $http, $location) {
    $scope.master = {};
    $scope.activePath = null;

    $scope.add_new_data = function (data, AddNewForm) {

        $http.post('/api/data', data).success(function () {
            $scope.reset();
            $scope.activePath = $location.path('/');
        });

        $scope.reset = function () {
            $scope.data = angular.copy($scope.master);
        };

        $scope.reset();
    };
}

function DeleteCtrl($scope, $http, $location, $routeParams) {
    var id = $routeParams.id;
    $scope.activePath = null;

    $http.get('/api/users/' + id).success(function (data) {
        $scope.user = data;
    });

    $scope.delete = function (user) {
        var deleteUser = confirm('Are you absolutely sure you want to delete ?');
        if (deleteUser) {
            $http.delete('/api/users/' + id)
                .success(function(data, status, headers, config) {
                    $scope.activePath = $location.path('/');
                }).
                error(function(data, status, headers, config) {
                    console.log("error");
                    // custom handle error
                });
        }
    };
}