/******** Modal with textArea for Certificate B64 adding ********/
function modalB64Controller($scope, $modalInstance,label) {
	initMessages($scope);
	
	$scope.label = label;
	$scope.b64 = "";
	
	$scope.ok = function() {
		$modalInstance.close($scope.b64);
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};
};