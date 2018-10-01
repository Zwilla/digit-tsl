digitTslWeb.controller('systemController', [ '$scope', '$modal', 'httpFactory', 'showModal', 'appConstant', function($scope, $modal, httpFactory, showModal, appConstant) {

    initMessages($scope);

    $scope.callLoading = function() {
        httpFactory.get("/api/jobs/loading", appConstant.systemController.loadingFailure).then(function() {
        });
    };

    $scope.callRules = function() {
        httpFactory.get("/api/jobs/rulesValidation", appConstant.systemController.rulesFailure).then(function() {
        });
    };

    $scope.callSignature = function() {
        httpFactory.get("/api/jobs/signatureValidation", appConstant.systemController.signatureFailure).then(function() {
        });
    };

    $scope.callRetention = function() {
        httpFactory.get("/api/jobs/retentionPolicy", appConstant.systemController.retentionFailure).then(function() {
        });
    };

    $scope.callSignatureAlert = function() {
        httpFactory.get("/api/jobs/signatureAlert", appConstant.systemController.signatureAlertFailure).then(function() {
        });
    };

    $scope.callApproachBreakAlert = function() {
        httpFactory.get("/api/jobs/approachBreakAlert", appConstant.systemController.approachBreakAlertFailure).then(function() {
        });
    };

    $scope.callServiceData = function() {
        httpFactory.get("/api/jobs/serviceDataRefresh", appConstant.systemController.serviceDataFailure).then(function() {
        });
    };
    
    $scope.callLOTLValidation = function() {
        httpFactory.get("/api/jobs/validate_lotl", appConstant.systemController.lotlValidationFailure).then(function() {
        });
    };

    $scope.cacheCountries = function() {
        httpFactory.get("/api/cache/countries", appConstant.systemController.cacheCountriesFailure).then(function() {
            showModal.information(appConstant.systemController.cacheCountriesSuccess);
        });
    };

    $scope.cacheProperties = function() {
        httpFactory.get("/api/cache/properties", appConstant.systemController.cachePropertiesFailure).then(function() {
            showModal.information(appConstant.systemController.cachePropertiesSuccess);
        });
    };

    $scope.cacheChecks = function() {
        httpFactory.get("/api/cache/check", appConstant.systemController.cacheChecksFailure).then(function() {
            showModal.information(appConstant.systemController.cacheChecksSuccess);
        });
    };

} ]);
