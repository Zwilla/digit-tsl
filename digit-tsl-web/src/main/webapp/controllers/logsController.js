digitTslWeb.controller('logsController', function($scope, $modal, httpFactory, $q, showModal, $filter, appConstant) {

    $scope.loadingLogs = $scope.logController_loading;
    $scope.load = false;

    var head = [ 
        {label : $scope.tFileName, size : "300px;"},
        {label : $scope.tLastModificationDate, size : "200px;"},
        {label : $scope.tTime, size : "150px;"},
        {label : $scope.tSize, size : "200px;"},
        {label : "", size : "100px;"},
        {label : "", size : "100px;"}
    ];

    /**
     * GET all audit
     */
    var getAllAudit = function() {
        $scope.loadingLogs = $scope.logController_loading;
        $scope.load = false;
        httpFactory.get("/api/logs/get_all", $scope.logController_loading_error).then(function(logFiles) {
            $scope.logFiles = logFiles;

            $scope.tableOptions = {
                nbItemPerPage : 20,
                listObj : initTable(logFiles),
                thead : head,
                displayHeading : true
            };
            $scope.load = true;
        });
    };
    getAllAudit();
    
    $scope.delete = function(i){
        $scope.loadingLogs = $scope.logController_process;
        $scope.load = false;
        var message = $filter('replaceIn')($scope.confirm_delete,'%OBJ%',$scope.logFiles[i].fileName); 
        showModal.confirmation(message, appConstant.modalTitle.confirmation).then(function() {
            httpFactory.post("/api/logs/delete_file",$scope.logFiles[i].fileName, $scope.error_log_delete).then(function(deleted) {
                if(deleted){
                    $scope.logFiles.splice(i,1);
                    $scope.tableOptions.listObj = initTable($scope.logFiles);
                }else{
                    showModal.applicationError($scope.error_log_delete);
                }
                $scope.load = true;
            },function(){
                $scope.load = true;
            });
        }, function(){
            $scope.load = true;
        });
    }

    var initTable = function(logFiles) {
        var tmpTable = [];
        for (var i = 0; i < logFiles.length; i++) {
            var obj = new Object();
            obj.fileName = logFiles[i].fileName;
            obj.lastModificationDate = $filter('date')(logFiles[i].lastModificationDate, "yyyy-MM-dd");
            obj.lastModificationTime = $filter('date')(logFiles[i].lastModificationDate, "HH:mm:ss");
            var sizeKB = Math.round((logFiles[i].size / 1024) * 100) / 100;
            obj.size =  sizeKB +"KB";
            obj.download = "<a ng-href='/tl-manager/api/logs/download_file/" + logFiles[i].fileName + "/' class='fa fa-download uri blackColor' tooltip='"+$scope.tDownload+"' tooltip-placement='right'></a>";
            obj.manage = {
                    html : "<span class='fa fa-trash uri' tooltip='"+$scope.tDelete+"' tooltip-placement='right'></span>",
                    action : "delete(" + i + ")"
            };
            tmpTable.push(obj);
        }
        return tmpTable;
    }

});
