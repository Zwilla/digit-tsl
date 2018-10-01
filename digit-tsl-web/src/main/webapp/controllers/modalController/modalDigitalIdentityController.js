/** ****** Modal to browse a Service Qualification Extension - CriteriaList ******* */
function modalDigitalIdentityController($scope, $modalInstance,$modal, digital,listcheck,listchange,digitalType,digitalIdService,showModal) {
	initMessages($scope);

	$scope.digital = digital;
	$scope.listcheck = listcheck;
	$scope.listchange = listchange;
	// Boolean to show/hide certificate (case for history)
	$scope.digitalType = digitalType;

	$scope.ok = function() {
		if(isGoodSki()){
			$modalInstance.close($scope.digital);
		};
	};

	$scope.cancel = function() {
		$modalInstance.dismiss();
	};

	/*------------ Certificate ------------*/

	/* Add Certificate B64(Factory) */
	$scope.addDigitalB64 = function() {
		digitalIdService.addDigitalB64("Certificate").then(function(digital) {
			$scope.digital.certificateList.push(digital.certificateList[0]);
			initSubjectNameSki(digital);
		});
	};

	/* Add Certificate .cert File (Factory) */
	$scope.addCertificate = function(myFile,fileInput) {
		digitalIdService.addCertificate(myFile).then(function(digital) {
			$scope.digital.certificateList.push(digital.certificateList[0]);
			initSubjectNameSki(digital);
		})
		.finally(function (){
			fileInput[0].value = "";
		});
	};

	// Other tag
	$scope.addOther = function(){
	    if($scope.digital.other==null){
	        $scope.digital.other = [];
	        $scope.digital.other.push(" ");
	    }else{
	        $scope.digital.other.push(" ");
	    };
	};

	$scope.deleteOther =function(index){
	    $scope.digital.other.splice(index,1);
	}

	$scope.deleteCertificate = function(tCertificate){
		$scope.digital.certificateList.splice($scope.digital.certificateList.indexOf(tCertificate),1);
	};

	$scope.convertAsn1 = function(tCertificate){
		tCertificate = convertAsn1(tCertificate);
	};

	/**
     * Init subject Name & Ski If there is no certificate in digital identity already
     */
	var initSubjectNameSki = function(digitalId){
	    if($scope.digitalType!="pointer"){
    		if($scope.digital.certificateList.length==1){
    			if(!$scope.digital.subjectName){
    				$scope.digital.subjectName = digitalId.subjectName;
    			};
    			if(!$scope.digital.x509ski){
    				$scope.digital.x509ski = digitalId.x509ski;
    			};
    		};
	    };
	};

	var isGoodSki = function(){
	    if($scope.digitalType!="pointer"){
    		if($scope.digital.x509ski!=undefined && $scope.digital.x509ski!=null && $scope.digital.x509ski!=""){
    			if(/^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$/.test($scope.digital.x509ski)){
    				return true;
    			}else{
    				showModal.applicationError($scope.tWrongB64)
    				return false;
    			};
    		}else{
    			$scope.digital.x509ski="";
    			return true;
    		};
	    }else{
	        return true;
	    }
	};

};