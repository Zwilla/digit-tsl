digitTslWeb.controller('sieQExtensionController', function($scope, $modal, $http, $q, httpFactory, showModal, appConstant) {

	// Get Application properties
	$scope.getApplicationProperties("");

	$scope.load = true;
	$scope.submitDisable = true;
	$scope.searchInit = false;
	$scope.certificateType = "FILE";
	$scope.requestForm = {
		certificateFile : null,
		certificateB64 : "",
	}

	/**
	 * Update requestForm file
	 */
	$scope.uploadFile = function(certificateFile) {
		$scope.$apply(function() {
			$scope.requestForm.certificateFile = certificateFile;
		});
	}

	/**
	 * Submit the request
	 */
	$scope.submitCertificate = function() {
		$scope.load = false;
		$scope.searchInit = false;
		if ($scope.certificateType == "FILE") {
			var fd = new FormData();
			fd.append('certificateFile', $scope.requestForm.certificateFile);
			httpFactory.postFile('/api/sieQValidation/validation/file', fd, appConstant.wizardSieQValidation.request_error).then(function(data) {
				$scope.validationResult = data;
				console.log($scope.validationResult);
				$scope.searchInit = true;
				$scope.load = true;
			}, function() {
				$scope.searchInit = false;
				$scope.load = true;
			});
		} else {
			httpFactory.post('/api/sieQValidation/validation/b64', $scope.requestForm.certificateB64, appConstant.wizardSieQValidation.request_error)
					.then(function(data) {
						$scope.validationResult = data;
						console.log($scope.validationResult);
						$scope.searchInit = true;
						$scope.load = true;
					}, function() {
						$scope.searchInit = false;
						$scope.load = true;
					});
		}
	}

	/**
	 * Enable/disable submit button
	 */
	$scope.isSubmitDisabled = function() {
		if (($scope.certificateType === "FILE" && $scope.requestForm.certificateFile != null)
				|| ($scope.certificateType === "B64" && $scope.requestForm.certificateB64 != '')) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Init certificate show & ASN1
	 */
	$scope.initCert = function(certificate) {
		certificate.show = false;
		certificate = convertAsn1(certificate);
		return certificate;
	}

	/**
	 * Reset search when certificate type change
	 */
	$scope.$watch("certificateType", function() {
		$scope.resetSearch();
	}, true);

	/**
	 * Reset search form and params
	 */
	$scope.resetSearch = function() {
		$scope.requestForm.certificateFile = null;
		$scope.requestForm.certificateB64 = "";
		$scope.searchInit = false;
		$scope.tCertificate = null;
		$scope.results = null;
		$scope.submitDisable = true;
		$scope.searchInit = false;
		if (angular.element("#fileInput")[0] != null) {
			angular.element("#fileInput")[0].value = "";
		}
	}

	$scope.tableVisibility = function(table) {
		if (table == null || table.length == 0) {
			return true;
		}
		return false;
	}

});