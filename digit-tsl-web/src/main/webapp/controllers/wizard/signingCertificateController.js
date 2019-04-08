digitTslWeb.controller('signingCertificateController', function($scope, $modal, $http, $q, httpFactory, showModal, appConstant) {

	// Get Application properties
	$scope.getApplicationProperties("");
	$scope.countryCodeNameProperties = countryCodeNameProperties.sort();

	$scope.load = true;
	$scope.submitDisable = true;
	$scope.searchInit = false;
	$scope.certificateType = "FILE";
	$scope.requestForm = {
		countryCode : "",
		certificateFile : null,
		certificateB64 : ""
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
	 * Update submit button enable/disable state
	 */
	$scope.updateSubmitState = function() {
		if ($scope.requestForm.countryCode != "" && $scope.requestForm.certificateFile != null) {
			return false;
		} else if ($scope.requestForm.countryCode != "" && $scope.requestForm.certificateB64 != "") {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Submit request by certificate B64 or file and return results +
	 * certificate token
	 */
	$scope.submitCertificate = function() {
		$scope.searchInit = false;
		$scope.load = false;
		if ($scope.requestForm.countryCode && $scope.requestForm.certificateB64) {
			// Certificate B64
			httpFactory.post("/api/wizard/signing_certificate/b64/" + $scope.requestForm.countryCode, $scope.requestForm.certificateB64, "Error")
					.then(function(signingCertificateResult) {
						$scope.tCertificate = signingCertificateResult.certificate;
						$scope.tCertificate.show = true;
						$scope.results = signingCertificateResult.results;
						$scope.searchInit = true;
						$scope.load = true;
					}, function() {
						$scope.load = true;
					});
		} else if ($scope.requestForm.countryCode && $scope.requestForm.certificateFile != null) {
			// Certificate file
			var fd = new FormData();
			fd.append('file', $scope.requestForm.certificateFile);
			$http.post("/tl-manager/api/wizard/signing_certificate/file/" + $scope.requestForm.countryCode, fd, {
				transformRequest : angular.identity,
				headers : {
					'Content-Type' : undefined
				}
			}).success(function(data, status, headers, config) {
				if (serviceResponseHandler(data)) {
					$scope.tCertificate = data.content.certificate;
					$scope.tCertificate.show = true;
					$scope.results = data.content.results;
					$scope.searchInit = true;
					$scope.load = true;
				} else {
					showModal.httpStatusHandler(data.responseStatus, data.errorMessage);
					$scope.load = true;
				}
			}).error(function(data, status, headers, config) {
				showModal.httpStatusHandler(data.responseStatus, "Error");
				$scope.load = true;
			});
			angular.element("#fileInput")[0].value = "";
		} else {
			showModal.information(appConstant.wizardSigningCertificate.form_uncomplete);
			$scope.load = true;
		}
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
		if (angular.element("#fileInput")[0] != null) {
			angular.element("#fileInput")[0].value = "";
		}
	}

	$scope.getCountry = function(countryCode) {
		for (var i=0;i<$scope.countryCodeNameProperties.length;i++) {
			if($scope.countryCodeNameProperties[i].label===countryCode){
				return $scope.countryCodeNameProperties[i].description;
			}
		}
		return countryCode;
	}

	/** *** ***** Certificate method ***** **** */

	$scope.convertAsn1 = function(tCertificate) {
		tCertificate = convertAsn1(tCertificate);
	};

});