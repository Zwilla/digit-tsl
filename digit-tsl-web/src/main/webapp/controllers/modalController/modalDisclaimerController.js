/******** TrustedList :  Information Edit Controller (Generic)********/
function modalDisclaimerController($scope, $modalInstance) {
	
	initMessages($scope);
	$scope.loadingStatus = "";

	$scope.ok = function() {
		$modalInstance.close();
	};
	
	/**
	 * Status loading process
	 */
	var loadingStatusProcess = function(){
		$scope.loadingStatus = $scope.clone_create;
		setTimeout(function () {
	        $scope.$apply(function () {
	        	$scope.loadingStatus=$scope.clone_migrate; 
	        });
	    }, 2000);
		setTimeout(function () {
	        $scope.$apply(function () {
	        	$scope.loadingStatus=$scope.clone_check; 
	        });
	    }, 7000);
		setTimeout(function () {
	        $scope.$apply(function () {
	        	$scope.loadingStatus=$scope.clone_uri; 
	        });
	    }, 17000);
	};
	loadingStatusProcess();

};