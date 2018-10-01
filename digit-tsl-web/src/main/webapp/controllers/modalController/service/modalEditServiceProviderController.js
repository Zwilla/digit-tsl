/******** Browse a Service Provider ********/
function modalEditServiceProviderController($scope, $modalInstance, $modal, serviceProvider,listChecks,listDiff,properties,httpFactory,
		tabsetFactory,electronicUriFactory) {
	
	initMessages($scope);
	
	$scope.serviceProvide = angular.copy(serviceProvider);
	var tspelectronicBis = angular.copy($scope.serviceProvide.tspelectronic);
	$scope.listChecks=listChecks;
	$scope.listDiffProviders=listDiff;
	$scope.languages = languagesProperties;
	$scope.countryName = countryCodeNameProperties;
	$scope.AddressType= adrTypeProperties;
	$scope.properties = properties;	
	initScope($scope);
	
	/* MODAL FUNCTION */
	$scope.ok = function() {
		rebuildElectronicTab();
		initExtension();
		$modalInstance.close($scope.serviceProvide);
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};
	
	/*------------- Add & Delete table object ------------------*/
	
	$scope.addTable = function(table) {
		var obj = new Object();
		obj.id = Math.floor((Math.random() * 100) + 1);
		table.push(obj);
	};
	
	$scope.deleteTable = function(table,item) {
		var index = table.indexOf(item);
		table.splice(index, 1);
	};
	
	/**
	 * Add Postal Address
	 */
	$scope.addTab = function(lang,tab) {
		var obj = Object();
		obj.id = Math.floor((Math.random() * 100) + 1);
		obj.language = lang;
		tab.push(obj);
	}
	
	/*----------------- Electronic address & URI mangement -----------------*/
	/**
	 * initialize table (electronicUriFactory)
	 **/
	var initElectronicTabBis = function(){
		$scope.serviceProvide.tspelectronic = electronicUriFactory.initTabBis(tspelectronicBis);
	};
	initElectronicTabBis();
	
	/**
	 * rebuild table (electronicUriFactory)
	 */
	var rebuildElectronicTab = function(){
		$scope.serviceProvide.tspelectronic = electronicUriFactory.rebuildTab($scope.serviceProvide.tspelectronic);
	};
	
	/**
	* add obj to table (electronicUriFactory)
	**/
	$scope.addElectronic = function(label) {
		$scope.serviceProvide.tspelectronic.push(electronicUriFactory.addTab(label,$scope.serviceProvide.tspelectronic));
	};

	/**
	 * Delete
	 */
	$scope.deleteElectronic = function(tab) {
		$scope.serviceProvide.tspelectronic.splice($scope.serviceProvide.tspelectronic.indexOf(tab), 1);
	};
	
	/*------------------------------*/
	
	/**
	 * TabsetFocus (Factory)
	 */
	$scope.activeTab = function(index,tab){
		tab = tabsetFactory.activeTab(index,tab);
	};
	
	/**
	 * Calendar
	 */
	$scope.open = function($event) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope.opened = true;
	};

	$scope.format = 'yyyy-MM-dd HH:mm:ss';
	
	
	$scope.styleBrowserMode = "fa fa-edit cursor-pointer editIcon";
	
	/**
	 * Extension management 
	 */
	
	var initBool = function(bool){
		if(bool==true){
			return "true";
		}else if(bool==false){
			return "false";
		}else if(bool=="true"){
			return true;
		}else if(bool=="false"){
			return false;
		};
	};
	
	var initExtension = function(){
		if($scope.serviceProvide.tspextension!=undefined && $scope.serviceProvide.tspextension!=null){
			for(var i=0;i<$scope.serviceProvide.tspextension.length;i++){
				$scope.serviceProvide.tspextension[i].critical = initBool($scope.serviceProvide.tspextension[i].critical); 
			};
		};
	};
	initExtension();
};