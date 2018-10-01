/** ****** TrustedList : Electronic Address Edition ******* */
function modalElectronicAddressEditController($scope, $modalInstance, tabs, myTLInfo,$modal,tabsetFactory,electronicUriFactory) {
	initMessages($scope);

	$scope.myTabs = tabs;
	$scope.myTabsBIS = [];
	$scope.myTLInfo = myTLInfo;
	$scope.AddressType= adrTypeProperties;
	$scope.languages = languagesProperties;
	$scope.adrType = "";
	$scope.adrLink = "";
	
	/**
	 * Validate
	 */
	$scope.ok = function() {
		$scope.myTabs = [];
		if($scope.myTabsBIS!=undefined){
				rebuildTab();
		};
		$modalInstance.close($scope.myTabs);
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};
	
	/**
	 * initialize table (electronicUriFactory)
	 **/
	var initTabBis = function(){
		$scope.myTabsBIS = electronicUriFactory.initTabBis($scope.myTabs);
	};
	initTabBis();
	
	/**
	 * rebuild table (electronicUriFactory)
	 */
	var rebuildTab = function(){
		$scope.myTabs = electronicUriFactory.rebuildTab($scope.myTabsBIS);
	};
	
	/**
	* add obj to table (electronicUriFactory)
	**/
	$scope.addTab = function(label) {
		$scope.myTabsBIS.push(electronicUriFactory.addTab(label,$scope.myTabsBIS));
	};

	/**
	 * Delete
	 */
	$scope.deleteTab = function(tab) {
		$scope.myTabsBIS.splice($scope.myTabsBIS.indexOf(tab), 1);
	};
	
	/**
	 * TabsetFocus (Factory)
	 */
	$scope.activeTab = function(index){
		$scope.myTabsBIS = tabsetFactory.activeTab(index,$scope.myTabsBIS);
	};
	$scope.activeTab(myTLInfo.index);
};