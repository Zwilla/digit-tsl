/**
 * Modal Check Pop-up Controller
 */
function modalCheckDirectiveController($scope, $modalInstance, list, label) {
	initMessages($scope);
	initStatusEnum($scope);

	$scope.label = label;
	var backUpList = angular.copy(list);
	var list = angular.copy(list);
	$scope.filtre = "";

	$scope.ok = function() {
		$modalInstance.close();
	};

	$scope.cancel = function() {
		$modalInstance.dismiss();
	};

	var initOption = function() {
		$scope.tableOptions = initCheckOption(list,"Location");
	};
	initOption();

	/** Filter * */
	$scope.$watch("filtre", function() {
		if ($scope.filtre == "") {
			list = backUpList;
		} else {
			list = [];
			for (var i = 0; i < backUpList.length; i++) {
				if (backUpList[i].status == $scope.filtre) {
					list.push(backUpList[i]);
				};
			};
		};
		initOption();
	}, true);

};