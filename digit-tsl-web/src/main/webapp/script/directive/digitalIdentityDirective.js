/**
 * Digitalidentity : display digital identity (+ change/checks)
 * Type : html tag
 * Parameter :
 ** digital - object : digital identity
 ** listcheck/listchange - array[object]: list used to display changes/checks
 ** edit - boolean (true or undefined) : used to apply different css style between browser & edition
 ** tl - object : trustedlist used to apply different css style between prod & draft
 */
digitTslWeb.directive('digitalidentity',['$filter','appConstant', function($filter,appConstant){
	return {
		restrict: 'EA',
        replace: true,
        transclude: true,
        scope : {
        	digital : '=',
        	listcheck : '=',
        	listchange : '=',
        	edit : '=',
        	tl : '=',
        	checktorun : '='
        },
		templateUrl : 'digitalIdentity',
		link : function($scope,element,attrs){

			//Init: display of digital id & type (pki/nothavingpki)
			var initShow = function(){
				if($scope.digital!=undefined && $scope.digital!=null){
					$scope.digital.show = false;
					if($scope.digital.other!=undefined && $scope.digital.other!=null){
						$scope.digital.isOther=true;
					}else{
						$scope.digital.isOther=false;
					};
				};
			};
			initShow();

			//Init: node title in function of digital identity type/value
			$scope.digitName = function(){
				if($scope.digital!= undefined || $scope.digital!=null){
					if($scope.digital.subjectName!= null && $scope.digital.subjectName!=""){
						$scope.digital.name = $scope.digital.subjectName;
					}else if($scope.digital.certificateList!=undefined && $scope.digital.certificateList!=null && $scope.digital.certificateList[0]!=null){
						var tmpCert = $scope.digital.certificateList[0];
						$scope.digital.name = tmpCert.certSubjectShortName + ": "+$filter('date')(tmpCert.certNotBefore, "yyyy-MM-dd H:mm:ss") +" - "+$filter('date')(tmpCert.certAfter, "yyyy-MM-dd H:mm:ss");
					}else if($scope.digital.x509ski!=null && $scope.digital.x509ski!=""){
						$scope.digital.name = $scope.digital.x509ski;
					}else if($scope.digital.other!=undefined && $scope.digital.other!=null && $scope.digital.other[0]!=null){
						$scope.digital.name = $scope.digital.other[0];
					}else{
						$scope.digital.name = appConstant.digitalIdentity.undefinedDigitalIdentity;
					};
				};
			};
			$scope.digitName();

			//Convert certificate to ASN1
			$scope.convertAsn1 = function(tCertificate){
				tCertificate = convertAsn1(tCertificate);
			};

			/*----- DOM MANAGEMENT -----*/

			$scope.isOpen = function(obj){
				if($scope.edit==undefined && obj=="1"){
					return "isOpen";
				}else{
					return "";
				};
			};

			$scope.carretIsOpen = function(){
				if($scope.edit==undefined){
					return "white";
				}else{
					return "";
				};
			};

			$scope.objVisibility = function(value){
				//myApp.js
				return objVisibility(value,$scope.tl);
			};

			$scope.isNotPointer = function(digitalId){
			    if(digitalId!=undefined && digitalId!=null){
			        return false;
			    }else{
			        return digitalId.indexOf(appConstant.digitalIdentity.pointerToOthers) >= 0;
			    };
			};
		}
	}
}]);
