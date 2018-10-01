/******** Modal with tlso contact ********/
function modalTlsoContactController($scope, $modalInstance,tl) {
	initMessages($scope);
	$scope.tl = tl;
	
	$scope.ok = function() {
		$modalInstance.close();
	};

};