/******** Propertie Edition ********/
function modalPropertieEditController($scope, $modalInstance, propertie,filter) {
	initMessages($scope);
	
	$scope.propertie = propertie;
	$scope.label = filter;

	$scope.ok = function() {
		$modalInstance.close($scope.propertie);
	};

	$scope.cancel = function() {
		$modalInstance.dismiss();
	};
};