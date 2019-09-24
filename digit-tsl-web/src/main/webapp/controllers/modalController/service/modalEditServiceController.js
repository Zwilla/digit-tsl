/** ****** Browse a Service ******* */
function modalEditServiceController($scope, $modalInstance, $modal, service, listChecks, listDiff, properties, digitalIdService, httpFactory,
		extensionFactory, showModal) {
	initMessages($scope);

	$scope.service = angular.copy(service);
	$scope.listChecks = listChecks;
	$scope.listDiffProviders = listDiff;
	$scope.languages = languagesProperties;
	$scope.properties = properties;
	$scope.serviceTypeIdentifiers = serviceTypeIdentifiersProperties.sort(dynamicSort("label"));
	$scope.serviceStatus = serviceStatusProperties.sort(dynamicSort("label"));
	initScope($scope);

	/* MODAL FUNCTION */
	$scope.ok = function() {
		$modalInstance.close($scope.service);
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};

	$scope.addTable = function(table) {
		var obj = new Object();
		obj.id = Math.floor(Math.random() * 100 + 1);
		table.push(obj);
	};

	$scope.deleteTable = function(table, index) {
		table.splice(index, 1);
	};

	/*--------------- Type Identifier -----------------*/

	$scope.changeTypeIdentifier = function() {
		// if service type identifier is changed to a nothavingpki
		if ($scope.service.typeIdentifierBis.indexOf($scope.service_nothavingpki, 1) > -1) {
			if (($scope.service.digitalIdentification != null && $scope.service.digitalIdentification.length > 0 && $scope.service.digitalIdentification[0] != null)
					&& (($scope.service.digitalIdentification[0].certificateList != undefined && $scope.service.digitalIdentification[0].certificateList.length > 0)
							|| $scope.service.digitalIdentification[0].subjectName || $scope.service.digitalIdentification[0].x509ski)) {
				showModal.applicationError($scope.service_warnNonOtherPki);
				$scope.service.typeIdentifierBis = $scope.service.typeIdentifier;
				return;
			}
		}
		$scope.service.typeIdentifier = $scope.service.typeIdentifierBis;
	};

	var initTypeIdentifier = function() {
		if (service.typeIdentifier != undefined && service.typeIdentifier != null) {
			$scope.service.typeIdentifierBis = service.typeIdentifier;
		} else {
			$scope.service.typeIdentifierBis = "";
		}
	};
	initTypeIdentifier();

	/*------------------------------ Digital Identification (Factory) ----------------------------*/

	$scope.deleteDigitalIdentitie = function(digital) {
		$scope.service.digitalIdentification.splice($scope.service.digitalIdentification.indexOf(digital), 1);
	};

	$scope.addDigitalIdentitie = function() {
		if ($scope.service.typeIdentifier.indexOf($scope.service_nothavingpki, 1) != -1) {
			// NotHavingPKI -> Digital identity 'other' only
			digitalIdService.addDigitalOther(listDiff, listChecks, "service", $scope.service.typeIdentifier).then(function(other) {
				$scope.service.digitalIdentification.push(other);
			});
		} else {
			digitalIdService.addDigitalIdentitie(listDiff, listChecks, "service").then(function(tmpDigital) {
				$scope.service.digitalIdentification.push(tmpDigital);
			});
		}
	};

	$scope.editDigitalIdentitie = function(digital, index) {
		var digitalBackUp = angular.copy(digital);
		digitalBackUp.checkToRun = $scope.service.checkToRun;
		digitalIdService.editDigitalIdentitie(digitalBackUp, listDiff, listChecks, "service", $scope.service.typeIdentifier).then(
				function(tmpDigital) {
					$scope.service.digitalIdentification[index] = tmpDigital;
				});
	};

	/*--------------- Extension (Factory) -----------------*/

	$scope.labelExtension = function(extension) {
		return extensionFactory.extensionLabel(extension);
	};

	$scope.browserExtension = function(extension, serviceName) {
		extensionFactory.browseExtension(extension, serviceName);
	};

	$scope.addExtension = function(typeExtension) {
		extensionFactory.addExtension(typeExtension).then(function(ext) {
			$scope.service.extension.push(ext);
		});
	};

	$scope.editExtension = function(extension, index) {
		var extBackUp = angular.copy(extension);
		extensionFactory.editExtension(extension, $scope.listChecks, $scope.listDiffProviders).then(function(ext) {
			$scope.service.extension[index] = ext;
		}, function() {
			$scope.service.extension[index] = extBackUp;
		});
	};

	/*-----------------------------------------*/

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