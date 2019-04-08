digitTslWeb.controller('cronRetentionController', function($scope, $modal, httpFactory, $q, $filter, $window, showModal, appConstant, cookieFactory) {

    $scope.loadingRetention = appConstant.retentionController.loading_data;
    $scope.load = false;

    // ---- Data table 'header' ---- //

    var head_draftstore = [ 
        {label : $scope.tDraftStoreId, size : "500px;"},
        {label : $scope.tLastAccess, size : "400px"},
        {label : "Countries", size : "300px"}
    ];

    var head_draft_tl = [ 
        {label : $scope.digitalIdentification_id, size : "50px;"},
        {label : $scope.schemeTerritory, size : "100px;"},
        {label : $scope.tlBrowser_sequenceNumber, size : "150px;"},
        {label : $scope.tLastAccess, size : "150px"},
        {label : $scope.tDraftStoreId, size : "350px"}
    ];

    // ---- ---- //

    var launchRequest = function() {
        $scope.load = false;
        httpFactory.get("/api/retention/get_cron_retention", appConstant.retentionController.loading_failure).then(function(result) {
        	$scope.nextCron = result.nextCron;
        	$scope.lastAccessDate = result.lastAccessDate;
            initList(result);
            $scope.load = true;
        });
    };
    launchRequest();

    /**
     * Init 'Paginator' data according to the $retentionCritieria.target choosen.
     * 
     */
    var initList = function(result) {
        $scope.draftstoreTableOptions = {
            nbItemPerPage : 20,
            listObj : null,
            thead : head_draftstore,
            displayHeading : true
        };
        
        $scope.draftTableOptions = {
                nbItemPerPage : 20,
                listObj : null,
                thead : head_draft_tl,
                displayHeading : true
        };
        
        if(result.draftstores!=null){
        	$scope.draftstoreTableOptions.listObj=[];
        	//Init draftstore table
        	for(var i=0;i<result.draftstores.length;i++){
        		 var obj = new Object();
                 obj.id = result.draftstores[i].draftStoreId;
                 obj.lastVerification = $filter('date')(result.draftstores[i].lastVerification, "yyyy-MM-dd hh:mm:ss");
                 //TOOD: Liste des pays
                 obj.countries="";
                 for(var j=0;j<result.draftstores[i].tls.length;j++){
                	 var cc = result.draftstores[i].tls[j].territoryCode;
                	 if(obj.countries.indexOf(cc)==-1){
                		 obj.countries= obj.countries+cc+"; ";
                	 }
                 }
                 
                 // Push
                 $scope.draftstoreTableOptions.listObj.push(obj);
        	}
        }
        
        
        if(result.draftTL!=null && result.draftTL.tls!=null){
        	$scope.draftTableOptions.listObj=[];
        	//Init draftstore table
        	for(var i=0;i<result.draftTL.tls.length;i++){
        		 var obj = new Object();
                 obj.tlId = result.draftTL.tls[i].id;
                 obj.territoryCode = result.draftTL.tls[i].territoryCode;
                 obj.sequenceNumber = result.draftTL.tls[i].sequenceNumber;
                 obj.lastAccessDate = $filter('date')(result.draftTL.tls[i].lastAccessDate, "yyyy-MM-dd hh:mm:ss");
                 obj.id = result.draftTL.tls[i].draftStoreId;

                 // Push
                 $scope.draftTableOptions.listObj.push(obj);
        	}
        }

    }

});
