digitTslWeb.controller('dataChecksController',['$scope','$modal','httpFactory', function ($scope,$modal,httpFactory){
		
	$scope.loadChecks = false;
	$scope.loadingStatusChecks = $scope.dataChecksController_loading;
	
	/**
	 * Get Application Checks
	 * (Execute on page loading)
	 */
	var getChecks = function(){
		httpFactory.get("/api/checks/all",$scope.dataChecksController_errorCheckLoading).then(function(data) {
			$scope.dataChecks = data;
			listFilterConstruction(data);
		})
		.finally(function (){
			$scope.loadChecks=true;
		});
	};
	getChecks();
	
	/**
	 * Create Code List from application check propertie code list
	 * Init filter on 1st element
	 */
	var listFilterConstruction = function(data){
		$scope.listFilter = [];
		for(var i=0;i<data.length;i++){
			//First entry
			if($scope.listFilter.length==0){
				$scope.listFilter.push(data[i].target);
			}else{
				var bool = false;
				for(var j=0;j<$scope.listFilter.length;j++){
					if($scope.listFilter[j]==data[i].target){
						bool=true; //Code List is already in list
					}
				};
				if(!bool){
					$scope.listFilter.push(data[i].target);
				};
			};
		};
		$scope.listFilter.sort();
		$scope.filter= $scope.listFilter[0];
	};
	
	/** 
	 * Edit Check
	 **/
	$scope.editCheck = function(check){
		var refCheck= angular.copy(check);
		var modalInstance = $modal.open({
			  templateUrl: "management/modalCheckEdit",
			  controller: modalCheckEditController,
			  backdrop : 'static',
			  size : 'lg',
			  resolve : {
				  check : function() {
						return refCheck;
				  },
				  filter : function(){
					  return $scope.filter;
				  }
				}
			});
			
		modalInstance.result.then(function() {
			angular.copy(refCheck, check);
			httpFactory.put('/api/checks/edit',check,$scope.dataChecksController_checkEditionFailure);
		});
		
	};
	
	/** Filter **/
	$scope.filterCode = function(check){
		if(check.target==$scope.filter){
			return true;
		}else{
			return false;
		};
	};
	
	$scope.changefilter = function(filtre){
		$scope.filter=filtre;
	};
	
	$scope.classFilter = function(value){
		if($scope.filter==value){
			return "select-pointer isSelect";
		}else{
			return "select-pointer";
		};
	};
	
}]);