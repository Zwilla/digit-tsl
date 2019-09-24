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
	$scope.showTranslation = false;
	$scope.translationPresent = false;

	$scope.ok = function() {
		$modalInstance.close();
	};

	$scope.cancel = function() {
		$modalInstance.dismiss();
	};

	var initOption = function() {
		// Show/Hide translation switch
		for (var i = 0; i < list.length; i++) {
			if (list[i].translation) {
				$scope.translationPresent = true;
				break;
			}
		}

		$scope.tableOptions = initCheckOption(list, "Location", null, null, $scope.showTranslation);
	};
	initOption();

	/** Filter * */
	$scope.$watch("filtre", function() {
		updateList();
	}, true);

	/** Filter * */
	$scope.$watch("showTranslation", function() {
		updateList();
	}, true);

	/**
	 * Update list on filtre/show translation value change
	 */
	var updateList = function() {
		if ($scope.filtre == "") {
			list = backUpList;
		} else {
			list = [];
			for (var i = 0; i < backUpList.length; i++) {
				if (backUpList[i].status == $scope.filtre) {
					list.push(backUpList[i]);
				}
			}
		}
		initOption();
	}

	$scope.switchTranslation = function() {
		$scope.showTranslation = !$scope.showTranslation;
	}
};