/******** TrustedList :  Information Edit Controller (Generic)********/
function modalTLInfoEdit($scope, $modalInstance, myValue ,  myTLInfo,$modal,showModal) {
	initMessages($scope);
	
	$scope.value = myValue;
	$scope.label = myTLInfo.label;
	$scope.myTLInfo = myTLInfo;
	$scope.listProperties = myTLInfo.listProperties;

	/**
	 * Validate
	 */
	$scope.ok = function() {
		$modalInstance.close($scope.value);
	};

	$scope.cancel = function() {
		$modalInstance.dismiss();
	};
	
	/**
	 * Calendar
	 */
	$scope.open = function($event) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope.opened = true;
	};

	$scope.format = 'yyyy-MM-dd HH:mm:ss';
};