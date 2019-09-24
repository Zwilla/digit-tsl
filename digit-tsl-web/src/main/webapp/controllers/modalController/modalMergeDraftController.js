function modalMergeDraftController($scope, $modalInstance, httpFactory, appConstant, drafts, draftstoreId) {

	initMessages($scope);

	$scope.drafts = angular.copy(drafts);
	$scope.countries = [];
	$scope.country = "";
	$scope.loadMerge = false;
	$scope.loadStatus = $scope.mergeDraft_preCalcul;
	$scope.readyToMerge = false;
	$scope.conflicts = [];
	$scope.changes = [];

	$scope.selectedCountry = null;
	$scope.selectedDrafts = [];
	$scope.isDraftsSelected = false;
	$scope.mergeChanges = null;

	var updateDraftList = function() {
		// Reset drafts
		$scope.isDraftsSelected = false;
		$scope.readyToMerge = false;
		$scope.conflicts = [];
		// $scope.selectedCountry = null;
		$scope.mergeChanges = null;
		for (var i = 0; i < $scope.drafts.length; i++) {
			$scope.drafts[i].selectToMerge = false;
		}

		// Update selected country
		for (var i = 0; i < $scope.countries.length; i++) {
			if ($scope.country === $scope.countries[i].countryCode) {
				$scope.selectedCountry = $scope.countries[i];
				return;
			}
		}
	}

	/**
	 * Init modal country list. Display a country list if there is more than one
	 * country managed in the draftstore
	 */
	var initModal = function() {
		var tmpCountries = [];
		for (var i = 0; i < $scope.drafts.length; i++) {
			if (tmpCountries.indexOf($scope.drafts[i].territoryCode) == -1) {
				var countryEntry = {
					"countryCode" : $scope.drafts[i].territoryCode,
					"countryName" : $scope.drafts[i].countryName
				};
				$scope.countries.push(countryEntry);
				tmpCountries.push($scope.drafts[i].territoryCode)
			}
			$scope.drafts[i].selectToMerge = false;
		}

		if ($scope.countries.length == 1) {
			$scope.selectedCountry = $scope.countries[0];
			updateDraftList();
		}
	}
	initModal();

	/** Update selected country * */
	$scope.$watch("country", function() {
		updateDraftList();
	}, true);

	/**
	 * Update selected drafts list and enable/disable the merge button when a
	 * draft is checked/unchecked
	 */
	$scope.updateSelectedList = function() {
		$scope.readyToMerge = false;
		$scope.conflicts = [];
		$scope.selectedDrafts = [];
		$scope.mergeChanges = null;
		for (var i = 0; i < $scope.drafts.length; i++) {
			if ($scope.drafts[i].territoryCode == $scope.selectedCountry.countryCode && $scope.drafts[i].selectToMerge) {
				$scope.selectedDrafts.push($scope.drafts[i]);
			}
		}

		if ($scope.selectedDrafts.length >= 1) {
			$scope.isDraftsSelected = true;
		} else {
			$scope.isDraftsSelected = false;
		}
	}

	/**
	 * Get changes between PROD and DRAFT(s). If conflicts present display it
	 */
	$scope.prepareMerge = function() {
		$scope.loadStatus = $scope.mergeDraft_preCalcul;
		$scope.loadMerge = true;
		$scope.conflicts = [];
		$scope.changes = [];
		$scope.mergeChanges = null;
		var obj = {
			countryCode : $scope.selectedCountry.countryCode,
			drafts : $scope.selectedDrafts
		}
		httpFactory.post("/api/draft/merge/changes", obj, appConstant.merge_changes_error).then(function(merge) {
			$scope.mergeChanges = merge;
			processMap(merge.siResult.mergeResult);
			processMap(merge.pointerResult.mergeResult);
			processMap(merge.tspResult.mergeResult);

			if ($scope.conflicts.length == 0) {
				$scope.readyToMerge = true;
			} else {
				$scope.readyToMerge = false;
			}
			$scope.loadMerge = false;
		});
	}

	/**
	 * Process mergeResult and manage conflicts if there is more than one change
	 * on the same element of a TL
	 */
	var processMap = function(map) {
		Object.keys(map).forEach(function(key) {
			// Conflict
			if (map[key].length > 1) {
				var tmpConflict = {
					location : key,
					drafts : []
				}

				for (var i = 0; i < map[key].length; i++) {
					if (tmpConflict.drafts.indexOf(map[key][i].draft) == -1) {
						tmpConflict.drafts.push(map[key][i].draft);
					}
				}
				$scope.conflicts.push(tmpConflict);
			}

			// Changes
			var tmpChange = {
				location : key,
				drafts : []
			}
			for (var i = 0; i < map[key].length; i++) {
				tmpChange.drafts.push(map[key][i].draft);
			}
			$scope.changes.push(tmpChange);
		});
	}

	$scope.merge = function() {
		$scope.loadStatus = $scope.mergeDraft_performMerge;
		var performMerge = {
			countryCode : $scope.selectedCountry.countryCode,
			cookie : draftstoreId

		}
		$scope.loadMerge = true;
		httpFactory.post("/api/draft/merge/perform", performMerge, appConstant.merge_perform_error).then(function(draft) {
			$modalInstance.close(draft);
		}, function() {
			$scope.loadMerge = false;
		});
	}

	$scope.getMapLength = function(obj) {
		return Object.keys(obj).length;
	}

	$scope.getStatus = function(status) {
		if (status == "UPDATE") {
			return $scope.mergeDraft_toUpdate;
		} else if (status == "NEW") {
			return $scope.mergeDraft_toAdd;
		} else if (status == "DELETE") {
			return $scope.mergeDraft_toDelete;
		}
	}

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};
};