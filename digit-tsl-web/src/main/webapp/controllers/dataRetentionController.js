digitTslWeb.controller('dataRetentionController', function($scope, $modal, httpFactory, $q, $filter, $window, showModal, appConstant, cookieFactory) {

    $scope.loadingRetention = appConstant.retentionController.loading_data;
    $scope.load = false;

    // Get Application properties
    $scope.getApplicationProperties("");
    $scope.countryCodeNameProperties = countryCodeNameProperties.sort();

    var result = [];

    // ---- Data table 'header' ---- //

    var head_draftstore = [ 
        {label : $scope.tDraftStoreId, size : "500px;"},
        {label : $scope.tLastAccess, size : "250px"},
        {label : $scope.tNbTlAttached, size : "250px"},
        {label : $scope.tManagement, size : "100px;"}
    ];

    var head_draft_tl = [ 
        {label : $scope.digitalIdentification_id, size : "50px;"},
        {label : $scope.schemeTerritory, size : "100px;"},
        {label : $scope.tlBrowser_sequenceNumber, size : "150px;"},
        {label : $scope.tLastAccess, size : "150px"},
        {label : $scope.tDraftStoreId, size : "300px"},
        {label : $scope.tManagement, size : "50px;"}
    ];

    var head_prod_tl = [ 
        {label : $scope.digitalIdentification_id, size : "50px;"},
        {label : $scope.schemeTerritory, size : "100px;"},
        {label : $scope.tlBrowser_sequenceNumber, size : "100px;"},
        {label : $scope.tlBrowser_firstScanDate, size : "100px"},
        {label : $scope.tArchived, size : "50px"},
        {label : $scope.tManagement, size : "50px;"}
    ];

    // ---- ---- //

    var launchRequest = function() {
        $scope.load = false;
        httpFactory.post("/api/retention/get", $scope.retentionCriteria, appConstant.retentionController.loading_failure).then(function(apiResult) {
            initList(apiResult);
            $scope.load = true;
        });
    };

    var resetCriteria = function() {
        var twoMonthAgo = new Date();
        twoMonthAgo.setDate(1);
        twoMonthAgo.setMonth(twoMonthAgo.getMonth()-2);

        $scope.retentionCriteria = {
            target : "DRAFTSTORE",
            territoryCode : "",
            date : twoMonthAgo
        }
    };

    $scope.resetSearch = function() {
        resetCriteria();
        launchRequest();
    }

    $scope.getData = function() {
        launchRequest();
    };

    // Initialize : criteria & get data
    $scope.resetSearch();

    /**
     * Delete draftstore $index in results (confirmation)
     */
    $scope.draftStoreDelete = function(index) {
        var message = $filter('replaceIn')($scope.confirm_delete, '%OBJ%', "draftstore '" + result[index].draftStoreId + "'");
        showModal.confirmation(message, appConstant.modalTitle.confirmation).then(function() {
            httpFactory.post("/api/retention/clean_draftstore", result[index], appConstant.retentionController.delete_draftstore_failure).then(function() {
                launchRequest();
            });
        });
    }

    /**
     * Delete trusted list $index in results (confirmation)
     */
    $scope.tlDelete = function(index) {
        var message = $filter('replaceIn')($scope.confirm_delete, '%OBJ%', "trusted list '" + result[index].territoryCode + ' (Sn' + result[index].sequenceNumber + ")" + "'");
        showModal.confirmation(message, appConstant.modalTitle.confirmation).then(function() {
            httpFactory.post("/api/retention/clean_trustedlist", result[index], appConstant.retentionController.delete_draftstore_failure).then(function() {
                launchRequest();
            });
        });
    }

    /**
     * 'Browse' trusted list $index in results. Replace current draftstore & redirect
     */
    $scope.tlBrowse = function(index) {
        // Confirm
        showModal.confirmation($scope.retentionController_redirect_draftstore_change, appConstant.modalTitle.confirmation).then(function() {
            //Replace draftstore
            $scope.draftStoreId = result[index].draftStoreId;
            cookieFactory.setCookie($scope.draftStoreId);
            //Redirect
            $window.location.href = '/tl-manager/tl/' + result[index].id;
        });
    }

    /**
     * Init 'Paginator' data according to the $retentionCritieria.target choosen.
     * 
     */
    var initList = function(apiResult) {
        $scope.tableOptions = {
            nbItemPerPage : 20,
            listObj : null,
            thead : null,
            displayHeading : true
        };

        var listObj = [];
        if ($scope.retentionCriteria.target === "DRAFTSTORE") {
            // INIT Draftstore data
            result = apiResult;

            $scope.tableOptions.thead = head_draftstore;
            for (var i = 0; i < result.length; i++) {
                var obj = new Object();
                var draftStoreLink = "/tl-manager/drafts/" + result[i].draftStoreId;
                obj.id = "<a class='uri' ng-href='" + draftStoreLink + "'>" + result[i].draftStoreId + "</a>";
                obj.lastVerification = $filter('date')(result[i].lastVerification, "yyyy-MM-dd hh:mm:ss");
                obj.nbTL = result[i].tls.length;
                // Delete
                obj.deleteButton = {
                    html : "<span class='fa fa-trash cursor-pointer' style='margin-left: 30px;' tooltip='" + $scope.tDelete + ' ' + $scope.tDraftStore
                            + "' tooltip-placement='right' align='center'></span>",
                    action : "draftStoreDelete(" + i + ")"
                };

                // Push
                listObj.push(obj);
            }
        } else if ($scope.retentionCriteria.target === "DRAFT") {
            // INIT Draft trusted list
            result = apiResult[0].tls;

            $scope.tableOptions.thead = head_draft_tl;
            for (var i = 0; i < result.length; i++) {
                var obj = new Object();
                obj.tlId = result[i].id;
                obj.territoryCode = result[i].territoryCode;
                // SequenceNumber + Browse
                obj.browseButton = {
                    html : "<a class='uri' tooltip='" + $scope.tBrowse + ' ' + $scope.tTrustedList + "' tooltip-placement='right'>" + result[i].sequenceNumber + "</a>",
                    action : "tlBrowse(" + i + ")"
                };
                obj.lastAccessDate = $filter('date')(result[i].lastAccessDate, "yyyy-MM-dd hh:mm:ss");
                // DraftStore + redirect
                var draftStoreLink = "/tl-manager/drafts/" + result[i].draftStoreId;
                obj.id = "<a class='uri' ng-href='" + draftStoreLink + "'>" + result[i].draftStoreId + "</a>";

                // Delete
                obj.deleteButton = {
                    html : "<span class='fa fa-trash cursor-pointer' style='margin-left: 20px;' tooltip='" + $scope.tDelete + ' ' + $scope.tTrustedList
                            + "' tooltip-placement='right'></span>",
                    action : "tlDelete(" + i + ")"
                };

                // Push
                listObj.push(obj);
            }
        } else if ($scope.retentionCriteria.target === "PRODUCTION") {
            // INIT Production trusted list
            result = apiResult[0].tls;

            $scope.tableOptions.thead = head_prod_tl;
            for (var i = 0; i < result.length; i++) {
                var obj = new Object();
                obj.tlId = result[i].id;
                obj.territoryCode = result[i].territoryCode;
                // Sequence Number + Browse
                var tlLink = "/tl-manager/tl/"+result[i].id;
                obj.sequenceNumber = "<a class='uri' ng-href='"+tlLink+"' tooltip='" + $scope.tBrowse + ' ' + $scope.tTrustedList + "'>"+result[i].sequenceNumber+"</a>"; 
                obj.lastAccessDate = $filter('date')(result[i].lastAccessDate, "yyyy-MM-dd hh:mm:ss");
                if (result[i].archive) {
                    obj.archive = "<span class='fa fa-archive cursor-pointer' style='margin-left: 20px;' tooltip='" + $scope.tArchived + "' tooltip-placement='right'></span>"
                } else {
                    obj.archive = "<span class='fa fa-arrow-circle-o-up cursor-pointer' style='margin-left: 20px;color:green' tooltip='" + $scope.tCurrentTL
                            + "' tooltip-placement='right' ></span>"
                }

                // Delete
                if (result[i].archive) {
                    obj.deleteButton = {
                        html : "<span ng-if='" + result[i].archive + "' class='fa fa-trash cursor-pointer' style='margin-left: 20px;' tooltip='" + $scope.tDelete + ' '
                                + $scope.tTrustedList + "' tooltip-placement='right'></span>",
                        action : "tlDelete(" + i + ")"
                    };
                } else {
                    // Can't delete current prod TL)
                    obj.deleteButton = "";
                }

                // Push
                listObj.push(obj);
            }
        }

        $scope.tableOptions.listObj = listObj;
    }

    /**
     * Calendar
     */
    $scope.open = function($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    };

});
