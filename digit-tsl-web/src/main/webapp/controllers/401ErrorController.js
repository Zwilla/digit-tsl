digitTslWeb.controller('401errorController', [ '$scope', '$modal', '$interval', '$window', 'httpFactory', 'showModal', 'appConstant',
        function($scope, $modal, $interval, $window, httpFactory, showModal, appConstant) {

            initMessages($scope);

            $scope.timer = 5;

            var getBrowserUrl = function() {
                httpFactory.get("/api/tl/browser/url", $scope.appController_errorUserLoading).then(function(url) {
                    $scope.url = url;
                    $interval(function() {
                        if ($scope.timer > 0) {
                            $scope.timer = $scope.timer - 1;
                        }
                        if ($scope.timer === 0) {
                            $window.location.href = url;
                        }
                    }, 1000);
                });
            }
            getBrowserUrl();

        } ]);
