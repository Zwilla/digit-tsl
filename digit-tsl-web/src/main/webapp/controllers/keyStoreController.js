digitTslWeb.controller('keyStoreController',['$scope','$modal','digitalIdService','httpFactory','showModal','$timeout',
	   function ($scope,$modal,digitalIdService,httpFactory,showModal,$timeout){

	$scope.loadKeyStore = false;
	$scope.loadingKeyStoreStatus = $scope.keyStoreController_loadingKeyStore;

	/**
	 * Get KeyStore certificate
	 * (execute on loading)
	 */
	var getKeyStore = function(){
	    $scope.loadingKeyStoreStatus = $scope.keyStoreController_loadingKeyStore;
		httpFactory.get("/api/keyStore/certificate",$scope.keyStoreController_signingFailure).then(function(data) {
			$scope.certificates = data;
		})
		.finally(function (){
			$scope.loadKeyStore = true;
		});
	};
	getKeyStore();

	/**
	 * Delete Certificate
	 * w/ confirmation
	 */
	$scope.deleteCertificate = function(certificate){
	    $scope.loadKeyStore = false;
	    $scope.loadingKeyStoreStatus = $scope.keyStoreController_deleteCertificateKeyStore;
		httpFactory.put('/api/keyStore/delete',certificate.id,$scope.keyStoreController_deleteFailure).then(function(data) {
			$scope.certificates.splice( $scope.certificates.indexOf(certificate), 1 );
		}).finally(function(){
		    $scope.loadKeyStore = true;
		});
	};

	/**
	 * Add Certificate
	 */
	var pushCertificate = function(certificate){
		if(certAlreadyExist(certificate)){
			showModal.applicationError($scope.keyStoreController_errorCertAlreadyPresent);
		}else{
		    $scope.loadKeyStore = false;
		    $scope.loadingKeyStoreStatus = $scope.keyStoreController_addCertificateKeyStore;
			httpFactory.post("/api/keyStore/add",certificate.certEncoded,$scope.keyStoreController_addCertFailure).then(function(data) {
				getKeyStore();
			});
		};
	};

	var certAlreadyExist = function(certificate){
		for(var i=0;i<$scope.certificates.length;i++){
			if($scope.certificates[i].certB64==certificate.certB64){
				return true;
			};
		};
		return false;
	};

	/* Add Certificate B64(Factory)*/
	$scope.addDigitalB64 = function() {
		var promise = digitalIdService.addDigitalB64($scope.keyStoreController_lotlSigningCert);
		promise.then(function(data) {
			pushCertificate(data.certificateList[0]);
		});
	};

	/* Add Certificate .cert File (Factory)*/
	$scope.addCertificate = function(myFile,fileInput) {
		var promise = digitalIdService.addCertificate(myFile);
		promise.then(function(data) {
			pushCertificate(data.certificateList[0]);
		})
		.finally(function (){
			fileInput[0].value = "";
		});
	};

	$scope.convertAsn1 = function(tCertificate){
		tCertificate = convertAsn1(tCertificate);
	};

	/** CSS **/

	$scope.isOpen = function(obj){
		if(obj==true){
			return "isOpen";
		}else{
			return "";
		};
	};

	/** Simulate click to trigger upload pop-up **/
	$scope.uploadSimulateClick = function(){
	    $timeout(function() {
	        angular.element('#uploadFile').trigger('click');
	    }, 0);
	};

}]);