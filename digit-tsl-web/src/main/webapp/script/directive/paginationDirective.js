/**
 * paginator : display dynamic data table with pagination Parameters -> options { 
 * nbItemPerPage : number of item per page (integer) 
 * listObj: data 
 * thead : array with label & column size (label : string , size : "XXXpx"),
 * displayHeading : display or hide table header (true/false) 
 * }
 * 
 * Support :
 * - Dynamic button with reflective call (see modalChangesProdController.js for example)
 * - HTML template 
 * 
 */
digitTslWeb.directive('paginator', [ '$window', function($window) {
    return {
        restrict : 'EA',
        replace : false,
        transclude : true,
        scope : {
            options : '='
        },
        templateUrl : 'pagination',
        link : function($scope, element, attrs) {
            $scope.selectPage = 1;
            $scope.pagination = [];

            $scope.evalFunction = function(parentFunction) {
                $scope.$parent.$eval(parentFunction);
            }

            // Display heading option: always true if not specified in options
            if ($scope.options.displayHeading == undefined || $scope.options.displayHeading == null) {
                $scope.options.displayHeading = true;
            }
            
         // Display border option: always false if not specified in options
            if ($scope.options.displayBorder == undefined || $scope.options.displayBorder == null) {
                $scope.options.displayBorder = false;
            }

            /** Pagination * */

            /* Update the current page */
            $scope.setPage = function(page) {
                if (page >= 1 && page <= $scope.nbPage) {
                    $scope.selectPage = page;
                }
                dynamicPaginator();
            };

            /**
             * Create paginator Split listObj in X page & create dynamic footer pagination
             */
            var dynamicPaginator = function() {
                $scope.pagination = [];
                var nbBefore = 4;
                var nbAfter = 5;

                for (var i = nbBefore; i > 0; i--) {
                    var nbTemp = $scope.selectPage - i;
                    if (nbTemp > 0) {
                        $scope.pagination.push(nbTemp);
                    } else {
                        nbAfter = nbAfter + 1
                    }
                }
                $scope.pagination.push($scope.selectPage);
                for (var j = 1; j <= nbAfter; j++) {
                    var nbTemp = $scope.selectPage + j;
                    if (nbTemp <= $scope.nbPage) {
                        $scope.pagination.push(nbTemp);
                    } else {
                        var first = $scope.pagination[0];
                        if (first > 1) {
                            $scope.pagination.unshift(first - 1);
                        }
                    }
                }
            };

            /* Set CSS active on selectPage */
            $scope.isActive = function(page) {
                if ($scope.selectPage == page) {
                    return "active";
                }
                return "";
            };

            /* Create the pagined table function to the numberPerPage and nbPage */
            var initTab = function(list) {
                if (list != undefined && list != null) {
                    $scope.nbPage = Math.ceil(list.length / $scope.options.nbItemPerPage);
                    dynamicPaginator();
                    $scope.paginedTable = [];
                    var table = [];

                    for (var i = 0; i < list.length; i++) {
                        if (table.length < $scope.options.nbItemPerPage) {
                            table.push(list[i]);
                        } else {
                            $scope.paginedTable.push(table);
                            table = [];
                            table.push(list[i]);
                        }
                    }
                    $scope.paginedTable.push(table);
                }
            };
            initTab($scope.options.listObj);

            /** Filter Handler * */

            /* Table update */
            $scope.$watch("options", function() {
                $scope.selectPage = 1;
                initTab($scope.options.listObj);
            }, true);
        }
    }
} ]);
