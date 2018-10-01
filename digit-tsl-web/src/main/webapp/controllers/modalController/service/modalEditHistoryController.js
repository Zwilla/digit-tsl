/************ Browse a Service Provider Extension *************/
function modalEditHistoryController($scope, $modalInstance,$modal, history,listChecks,listDiff,properties,httpFactory,digitalIdService,
		tabsetFactory,extensionFactory,showModal) {
	initMessages($scope);
	initScope($scope);

	$scope.history = angular.copy(history);
	$scope.listChecks=listChecks;
	$scope.listDiffProviders = listDiff;
	$scope.languages = languagesProperties;
	$scope.serviceTypeIdentifiers = serviceTypeIdentifiersProperties.sort(dynamicSort("label"));
	$scope.serviceStatus = servicePreviousStatusProperties.sort(dynamicSort("label"));
	$scope.properties = properties;

	$scope.ok = function() {
		$modalInstance.close($scope.history);
	};

	$scope.cancel = function() {
		$modalInstance.dismiss();
	};

	/* Table */
	$scope.addTable = function(table) {
		var obj = new Object();
		obj.id = Math.floor(Math.random() * 100 + 1);
		table.push(obj);
	};

	$scope.deleteTable = function(table,item) {
		var index = table.indexOf(item);
		table.splice(index, 1);
	};

	/*--------------- Type Identifier -----------------*/

	$scope.changeTypeIdentifier = function(){
        //if service type identifier is changed to a nothavingpki
        if($scope.history.typeIdentifierBis.indexOf($scope.service_nothavingpki,1)>-1){
            if($scope.history.digitalIdentification[0]!=null && ($scope.history.digitalIdentification[0].certificateList!=undefined && $scope.history.digitalIdentification[0].certificateList.length>0
                    || $scope.history.digitalIdentification[0].subjectName || $scope.history.digitalIdentification[0].x509ski)){
                showModal.applicationError($scope.service_warnNonOtherPki);
                $scope.history.typeIdentifierBis = $scope.history.typeIdentifier;
                return;
            };
        }
        $scope.history.typeIdentifier = $scope.history.typeIdentifierBis;
    };

	var initTypeIdentifier = function(){
		if(history.typeIdentifier!=undefined && history.typeIdentifier!=null){
			$scope.history.typeIdentifierBis = history.typeIdentifier;
		} else{
			$scope.history.typeIdentifierBis = "";
		};
	};
	initTypeIdentifier();

	/*------------------------------ Digital Identification (Factory) ----------------------------*/

	$scope.deleteDigitalIdentitie = function(digital){
		$scope.history.digitalIdentification.splice($scope.history.digitalIdentification.indexOf(digital),1);
	};

	$scope.addDigitalIdentitie = function() {
		if($scope.history.typeIdentifier.indexOf($scope.service_nothavingpki,1)!=-1){
			digitalIdService.addDigitalOther(listDiff,listChecks,"history").then(function(other) {
				$scope.history.digitalIdentification.push(other);
			});
		}else{
			digitalIdService.addDigitalIdentitie(listDiff,listChecks,"history").then(function(tmpDigital) {
				$scope.history.digitalIdentification.push(tmpDigital);
			});
		};
	};

	$scope.editDigitalIdentitie = function(digital,index){
		var digitalBackUp = angular.copy(digital);
		digitalBackUp.checkToRun = $scope.history.checkToRun;
		digitalIdService.editDigitalIdentitie(digitalBackUp,listDiff,listChecks,"history",$scope.history.typeIdentifier).then(function(tmpDigital) {
			$scope.history.digitalIdentification[index] = tmpDigital;
		});
	};

	/*--------------- Extension (Factory) -----------------*/

	$scope.labelExtension = function(extension){
		return extensionFactory.extensionLabel(extension);
	};

	$scope.browserExtension  = function(extension,serviceName){
		extensionFactory.browseExtension(extension,serviceName);
	};

	$scope.addExtension = function(typeExtension){
		extensionFactory.addExtension(typeExtension).then(function(ext) {
			$scope.history.extension.push(ext);
		});
	};

	$scope.editExtension = function(extension,index){
		var extBackUp = angular.copy(extension);
		extensionFactory.editExtension(extension,$scope.listChecks,$scope.listDiffProviders).then(function(ext) {
			$scope.history.extension[index] = ext;
		}, function(){
			$scope.history.extension[index] = extBackUp;
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