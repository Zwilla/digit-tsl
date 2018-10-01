/** ****** Browse a TrustedList Availability ******* */
function modalAvailabilityChartController($scope, $modalInstance, $filter, httpFactory, showModal, tl) {
    initMessages($scope);

    $scope.tl = tl;
    $scope.loadAvailability = true;

    $scope.dMin = null;
    $scope.dMax = null;
    var dateCorrect = true;

    $scope.ok = function() {
        $modalInstance.close();
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };

    $scope.$watch("[dMin,dMax]", function() {
        if (dateCorrect) {
            var today = new Date();
            var dMin = $scope.dMin;
            var dMax = $scope.dMax;
            if ((dMin != null) && (dMax != null)) {
                dMin.setHours(0, 0, 1);
                dMax.setHours(23, 59, 59);
                if (dMax > today) {
                    dMax = today;
                }
            }
            var tomorrow = new Date();
            tomorrow.setDate(tomorrow.getDate() + 1);
            if (($scope.dMin > $scope.dMax) || ($scope.dMin > today) || ($scope.dMax > tomorrow)) {
                dateCorrect = false;
                showModal.information($scope.availability_data_before_after).then(function() {
                    dateCorrect = true;
                    $scope.loadAvailability = false;
                    initData();
                });
            } else {
                update(dMin, dMax);
            }
        }
    }, true);

    var update = function(dMin, dMax) {
        $scope.loadAvailability = false;
        var tlAvailability = {
            id : tl.id,
            dMin : dMin,
            dMax : dMax
        };
        httpFactory.post("/api/tl/availability", tlAvailability, $scope.homeController_errorAvailability).then(function(result) {
            initAllView(result);
        }, function() {
            $scope.loadAvailability = true;
        });
    };

    var initData = function() {
        var currentDate = new Date();
        currentDate.setMonth(currentDate.getMonth() - 1);

        $scope.dMin = currentDate;
        $scope.dMin.setHours(0, 0, 1);
        $scope.dMax = new Date();
        update($scope.dMin, $scope.dMax);
    };
    initData();

    /* ---------- CSS ---------- */

    $scope.isTabActive = function(key) {
        if ($scope.showTab == key) {
            return "active";
        }
        return "";
    }

    /* ---------- Date ---------- */

    /** Calendar * */
    $scope.openMin = function($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.openedMin = true;
    };

    $scope.openMax = function($event) {
        $event.preventDefault();
        $event.stopPropagation();
        if ($scope.openedMax) {
            $scope.openedMax = false;
        } else {
            $scope.openedMax = true;
        }
    };

    $scope.format = 'yyyy-MM-dd';

    /* ---------- INIT ---------- */
    var initAllView = function(availability) {
        // Data
        $scope.unavailableList = availability.unavailableList;
        $scope.availabilityChartList = availability.availabilityChartList;

        // Data table
        var initUnavailabilityTable = function() {
            var head = [ {
                label : $scope.tStatus,
                size : "200px;"
            }, {
                label : $scope.tStartDate,
                size : "430px;"
            }, {
                label : $scope.tEndDate,
                size : "430px;"
            }, {
                label : $scope.tDuration,
                size : "200px"
            } ];
            var list = [];
            for (var i = 0; i < $scope.unavailableList.length; i++) {
                var obj = new Object();
                obj.status = $scope.unavailableList[i].status;
                obj.startDate = $filter('date')($scope.unavailableList[i].startDate, "yyyy-MM-dd H:mm:ss");
                obj.endDate = $filter('date')($scope.unavailableList[i].endDate, "yyyy-MM-dd H:mm:ss");
                obj.duration = $filter('millSecondsToTimeString')($scope.unavailableList[i].timing);
                list.push(obj);
            }

            // Display no end date for last unavailable/unsupported entry if current status is not available
            if ((availability.availabilityChartList.length > 0) && (availability.availabilityChartList[availability.availabilityChartList.length - 1].status != "AVAILABLE")) {
                list[0].endDate = null;
            }

            $scope.tableOptions = {
                nbItemPerPage : 5,
                listObj : list,
                thead : head,
                displayHeading : false
            };
        };
        initUnavailabilityTable();

        var toPercent = function(availabilityPieChart, timing) {
            var nb = timing / (availabilityPieChart.availableTiming + availabilityPieChart.unavailableTiming + availabilityPieChart.unsupportedTiming) * 100;
            return nb.toFixed(2);
        };

        // Line Chart
        $scope.lineStatus = [ $scope.tUnavailable, $scope.tAvailable ];
        $scope.lineData = [ {
            key : "Status: ",
            values : $scope.availabilityChartList
        } ];
        $scope.lineOption = {
            chart : {
                type : 'lineChart',
                height : 350,
                width : 980,
                margin : {
                    top : 20,
                    right : 70,
                    bottom : 60,
                    left : 80
                },
                x : function(d) {
                    return d.checkDate
                },
                y : function(d) {
                    return d.statusNumber;
                },
                showValues : true,
                showLegend : false,
                xAxis : {
                    axisLabel : 'Date ',
                    ticks : 5,
                    tickFormat : function(d) {
                        return d3.time.format('%Y-%m-%d %H:%M')(new Date(d));
                    },
                    axisLabelDistance : 50
                },
                yAxis : {
                    axisLabel : $scope.tbAvailability,
                    tickValues : [ 0, 1 ],
                    tickFormat : function(d) {
                        return $scope.lineStatus[d];
                    },
                    axisLabelDistance : 30
                }
            }
        };

        // Pie Chart
        $scope.pieData = [ {
            key : $scope.tAvailable,
            y : toPercent(availability.availabilityPieChart, availability.availabilityPieChart.availableTiming)
        }, {
            key : $scope.tUnavailable,
            y : toPercent(availability.availabilityPieChart, availability.availabilityPieChart.unavailableTiming)
        }, {
            key : $scope.tUnsupported,
            y : toPercent(availability.availabilityPieChart, availability.availabilityPieChart.unsupportedTiming)
        } ];
        $scope.pieOption = {
            chart : {
                type : 'pieChart',
                height : 300,
                width : 400,
                x : function(d) {
                    return d.key;
                },
                y : function(d) {
                    return d.y;
                },
                showLabels : true,
                labelThreshold : 0.01,
                labelSunbeamLayout : true,
                showLegend : false,
                valueFormat : function(d) {
                    return d3.format(',.2f')(d);
                }
            }
        };
        $scope.showTab = 'line';
        $scope.loadAvailability = true;
    };

};