/** ****** TrustedList : Pop-up with archived prod TL to enable/disable changes ******* */
function modalChangesProdController($scope, $modalInstance, $filter, myTl, archivedProds, trustedListFactory, httpFactory, showModal, appConstant) {

    initMessages($scope);
    $scope.myTl = myTl;
    $scope.load_change_prod = false;
    $scope.archivedProds = archivedProds;
    
    var head = [ 
        {label : $scope.tName,                  size : "160px"},
        {label : $scope.tlBrowser_issueDate,    size : "200px"},
        {label : $scope.tlBrowser_expiryDate,   size : "200px"},
        {label : $scope.tlBrowser_firstScanDate,size : "210px"},
        {label : "",                            size : "50px"},
        {label : "",                            size : "50px"}
    ];

    $scope.ok = function(index) {
        var message = $filter('replaceIn')($scope.confirm_changes_comparator,'%TL%',(archivedProds[index].name+' (Sn'+archivedProds[index].sequenceNumber+') ')); 
        showModal.confirmation(message, appConstant.modalTitle.confirmation).then(function() {
            $modalInstance.close(archivedProds[index]);
        });
    };

    $scope.cancel = function() {
        $modalInstance.dismiss();
    };

    var initList = function() {
        var dirList = [];
        for (var i = 0; i < archivedProds.length; i++) {
            var obj = new Object();
            obj.name = archivedProds[i].name + "(" + archivedProds[i].sequenceNumber + ")";
            obj.issueDate = $filter('date')(archivedProds[i].issueDate, "yyyy-MM-dd");
            obj.nextUpdate = $filter('date')(archivedProds[i].nextUpdate, "yyyy-MM-dd");
            obj.firstScanDate = $filter('date')(archivedProds[i].firstScanDate, "yyyy-MM-dd HH:mm:ss");
            obj.manage = {
                html : "<a class='fa fa-clone uri' tooltip='"+$scope.tCompare_tls+"' tooltip-placement='bottom' ></a>",
                action : "ok(" + i + ")"
            };
            obj.download = "<a ng-href='/tl-manager/api/tl/download/" + archivedProds[i].id + "/prod' class='fa fa-download uri' tooltip='"+$scope.tDownload+"' tooltip-placement='bottom'></a>";
            dirList.push(obj);
        }

        $scope.tableOptions = {
            nbItemPerPage : 10,
            listObj : dirList,
            thead : head,
            displayHeading : true
        };
        $scope.load_change_prod = true;
    }
    initList();

};