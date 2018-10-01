digitTslWeb.controller('tlStatsController', function($scope, $modal, $timeout, httpFactory, showModal, appConstant) {

    initMessages($scope);

    $scope.loadStats = false;
    $scope.loadingStatsMessage = $scope.tl_stats_loading;

    var initView = function() {
        httpFactory.get("/api/stats/criteria", $scope.tl_stats_criteria_init_error).then(function(criteria) {
            $scope.criteria = criteria;
            $scope.displayType = "ALL";
            $scope.loadStats = true;
        }, function() {
            $scope.loadStats = true;
        });
    }
    initView();

    $scope.performExtract = function() {
        $scope.loadStats = false;
        $scope.loadingStatsMessage = $scope.tl_stats_generation;
        httpFactory.post("/api/stats/generate", $scope.criteria, $scope.tl_stats_criteria_init_error).then(function() {
            $timeout(function() {
                document.getElementById('download_csv').click()
            });
            $scope.loadStats = true;
        }, function() {
            $scope.loadStats = true;
        });
    }

    $scope.resetCriteria = function() {
        $scope.criteria.type = "COUNTRY";
        $scope.criteria.specificCountry = "";
        $scope.displayType = "ALL";
        $scope.criteria.extractDate = new Date();
        $scope.criteria.showTOB = true;
        $scope.criteria.showSequenceNumber = true;
        $scope.criteria.showTradeName = true;
        $scope.criteria.showQualified = true;
        $scope.criteria.showUnqualified = true;
    }

    /**
     * Update criteria qualified/unqualified boolean based on displayType radio value
     */
    $scope.$watch('displayType', function() {
        if ($scope.criteria != undefined) {
            if ($scope.displayType === "ALL") {
                $scope.criteria.showQualified = true;
                $scope.criteria.showUnqualified = true;
            } else if ($scope.displayType === "QUALIFIED") {
                $scope.criteria.showQualified = true;
                $scope.criteria.showUnqualified = false;
            } else if ($scope.displayType === "UNQUALIFIED") {
                $scope.criteria.showQualified = false;
                $scope.criteria.showUnqualified = true;
            } else {
                $scope.criteria.showQualified = true;
                $scope.criteria.showUnqualified = true;
            }
        }

    });

    /**
     * Calendar
     */
    $scope.open = function($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    };

});
