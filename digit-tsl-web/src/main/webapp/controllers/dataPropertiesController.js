digitTslWeb.controller('dataPropertiesController',['$scope','$modal','httpFactory', function ($scope,$modal,httpFactory){
		
	$scope.loadProperties = false;
	$scope.loadingStatusProperties = $scope.dataPropertiesController_loading;
	
	var modalPropertie = 
		'<div class="panel panel-primary">'+
			'<div class="panel-heading">'+
				'<span style="width: 95% !important; display: inline-block;">'+$scope.dataPropertiesController_editPropertie+' {{label}}</span>'+
				'<span class="fa fa-times cursor-pointer" ng-click="cancel()"></span>'+
				'<span style="margin-left: 5px;" class="fa fa-check cursor-pointer" ng-click="ok()"></span>'+
			'</div>'+
			'<div class="panel-body">'+
				'<div class="row">'+
					'<span class="scheme-information col-sm-2 text-right">'+$scope.dataPropertiesController_value+'</span>'+
					'<input ng-model="propertie.label" class="col-sm-9" style="margin-left:10px;"/>'+
				'</div>'+
				'<div class="row">'+
					'<span class="scheme-information col-sm-2 text-right">'+$scope.dataPropertiesController_description+'</span>'+
					'<input ng-model="propertie.description" class="col-sm-9" style="margin-left:10px;"/>'+
				'</div>'+
			'</div>'
		'</div>';
	
	/**
	 * Get Application properties
	 * (Execute on page loading)
	 */
	var getProperties = function(){
		httpFactory.get("/api/properties/all",$scope.dataPropertiesController_errorPropertieLoading).then(function(data) {
			listFilterConstruction(data);
			$scope.dataProperties= data;
		})
		.finally(function (){
			$scope.loadProperties=true;
		});
	};
	getProperties();
	
	/**
	 * Create Code List from application properties code list
	 * Init filter to 1st element of list
	 */
	var listFilterConstruction = function(data){
		$scope.listFilter = [];
		for(var i=0;i<data.length;i++){
			//First entry
			if($scope.listFilter.length==0){
				$scope.listFilter.push(data[i].codeList);
			}else{
				var bool = false;
				for(var j=0;j<$scope.listFilter.length;j++){
					if($scope.listFilter[j]==data[i].codeList){ 
						bool=true; //Code List is already in list
					};
				};
				if(!bool){
					$scope.listFilter.push(data[i].codeList);
				};
			};
		};
		$scope.listFilter.sort();
		$scope.filter= $scope.listFilter[0];
	};
	
	/** 
	 * Add new propertie 
	 **/
	$scope.addPropertie = function(){
		var propertie = new Object();
		propertie.codeList= $scope.filter;
		var modalInstance = $modal.open({
			  template: modalPropertie,
			  controller: modalPropertieEditController,
			  backdrop : 'static',
			  size : 'lg',
			  resolve : {
				  propertie : function() {
						return propertie;
				  },
				  filter : function(){
					  return $scope.filter;
				  }
				}
			});
		
		modalInstance.result.then(function() {
			httpFactory.put('/api/properties/add',propertie,$scope.dataPropertiesController_propertieAddFailure).then(function(data) {
				$scope.dataProperties.push(data);
			});
		});
	};
	
	/**
	 * Delete propertie
	 */
	$scope.deletePropertie = function(propertie){
		httpFactory.put('/api/properties/delete',propertie,$scope.dataPropertiesController_propertieDeleteFailure).then(function(data) {
			$scope.dataProperties.splice( $scope.dataProperties.indexOf(propertie), 1 );
		});
	};
	
	/** Filter **/
	$scope.filterCode = function(propertie){
		if(propertie.codeList==$scope.filter){
			return true;
		}else{
			return false;
		}
	};
	
	$scope.changefilter = function(filtre){
		$scope.filter=filtre;
	};
	
	$scope.classFilter = function(value){
		if($scope.filter==value){
			return "select-pointer isSelect";
		}else{
			return "select-pointer";
		}
	};
	
}]);