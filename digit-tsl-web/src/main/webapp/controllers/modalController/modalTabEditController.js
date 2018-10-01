/** ****** TrustedList : Generic Tab Edition(Generic)******* */
function modalTabEditController($scope, $modalInstance, tabs, myTLInfo, sortable, tabsetFactory) {
    initMessages($scope);

    $scope.myTabs = tabs;
    $scope.myTLInfo = myTLInfo;
    $scope.languages = languagesProperties;
    $scope.countryName = countryCodeNameProperties;

    $scope.sortable = sortable;

    /**
     * Validate
     */
    $scope.ok = function() {
        tabs = $scope.myTabs;
        $modalInstance.close($scope.myTabs);
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };

    /**
     * Add entry
     */
    $scope.addTab = function(lang) {
        var tab = Object();
        tab.id = Math.floor((Math.random() * 100) + 1);
        tab.language = lang;
        tab.value = "";
        $scope.myTabs.push(tab);
    }

    /**
     * Delete entry
     */
    $scope.deleteTab = function(tab) {
        $scope.myTabs.splice($scope.myTabs.indexOf(tab), 1);
    };

    /**
     * TabsetFocus (Factory)
     */
    $scope.activeTab = function(index) {
        $scope.myTabs = tabsetFactory.activeTab(index, $scope.myTabs);
    };
    $scope.activeTab(myTLInfo.index);

    $scope.move = function(from, to) {
        $scope.myTabs.splice(to, 0, $scope.myTabs.splice(from, 1)[0]);
        $scope.activeTab(to);
    }
};