/******** Check Edition ********/
function modalCheckEditController($scope, $modalInstance, check,filter) {
	initMessages($scope);
	initStatusEnum($scope);
	initImpactEnum($scope);
	
	$scope.check = check;
	$scope.label = filter;

	$scope.ok = function() {
		$modalInstance.close($scope.check);
	};

	$scope.cancel = function() {
		$modalInstance.dismiss();
	};
};