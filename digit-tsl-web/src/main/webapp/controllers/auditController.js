digitTslWeb.controller('auditController',['$scope','$modal','httpFactory','$q','showModal','$filter','$window',
	   function ($scope,$modal,httpFactory,$q,showModal,$filter, $window){

			$scope.loadingAudit = $scope.auditController_loading;

			var initCriteria = function(){
			    httpFactory.get("/api/audit/criteriaList",$scope.auditController_criteria_loading_error).then(function(criterias) {
			        $scope.criterias = criterias;
			    });
			};
			initCriteria();

			$scope.filterAudit = function(){
			    if($scope.auditSearch.target==""){
			        $scope.auditSearch.target = null;
			    }
			    if($scope.auditSearch.action==""){
                    $scope.auditSearch.action = null;
                }
			    
			    if($scope.auditSearch.startDate!=null){
			        $scope.auditSearch.startDate.setHours(0, 0, 1);
			    }
			    if($scope.auditSearch.endDate!=null){
			        $scope.auditSearch.endDate.setHours(23, 59, 59);
			    }
			    
			    httpFactory.post("/api/audit/search",$scope.auditSearch,$scope.auditController_search_error).then(function(datas) {
                    initTable(datas);
                })
                .finally(function(){
                    $scope.load=true;
                });

			};
			
			var resetSearchDTO = function(){
			    var lastWeek = new Date();
                lastWeek.setDate(lastWeek.getDate() - 1 );
                
                $scope.auditSearch = {
                        countryCode : "",
                        target : null,
                        action : null,
                        startDate : lastWeek,
                        endDate : new Date(),
                        maxResult : 0
                };
			}

			$scope.resetSearch = function(){
			    resetSearchDTO();
			    getAllAudit();
			};
			
			/**
             * GET all audit
             */
            var getAllAudit = function() {
                $scope.load=false;
                resetSearchDTO();
                $scope.filterAudit();
            };
            getAllAudit();
            
            $scope.browseTL = function(xmlId){
                httpFactory.get("/api/audit/"+xmlId+"/get_tl_id", $scope.error_tl_not_found).then(function(tlId) {
                    if(tlId==null){
                        showModal.applicationError($scope.error_tl_not_found);
                    }else{
                        $window.location.href = '/tl-manager/tl/'+tlId;
                    }
                });
            }

			/**
             * Init tableOptions
             */
			var initTable = function(datas){
			    var head = [{label : $scope.tbDate, size : "140px;"},
			                {label : $scope.tCC, size : "45px;"},
			                {label : $scope.check_target, size : "130px;"},
			                {label : $scope.tAction, size : "160px"},
			                {label : $scope.tbStatus, size : "80px"},
			                {label : $scope.tUsername, size : "110px" },
			                {label : $scope.tbInfo, size : "430px"}];
                var list = [];
                for(var i=0;i<datas.length;i++){
                    var obj = new Object();
                    obj.date = $filter('date')(datas[i].date, "yyyy-MM-dd H:mm:ss");
                    obj.cc = datas[i].countryCode;
                    obj.target = datas[i].target;
                    obj.action = datas[i].action;
                    obj.status = datas[i].status;
                    obj.username = datas[i].username;
                    obj.infos = datas[i].infos
                    if(datas[i].target==="PROD_TL"){
                        obj.download = { 
                                html : "<span class='fa fa-search-plus uri blackColor' tooltip='"+$scope.tBrowse+"'></span>",
                                action : "browseTL(" + datas[i].fileId + ")"
                        };
                        
                    }else{
                        obj.download = "";
                    }
                    list.push(obj);
                }
                $scope.tableOptions = {
                    nbItemPerPage : 20,
                    listObj : list,
                    thead : head,
                    displayHeading : true
                };
			};


			/**
             * Calendar
             */
		    $scope.open = function($event) {
		        $event.preventDefault();
		        $event.stopPropagation();
		        $scope.opened = true;
		    };
		    
		    $scope.openEnd = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.openedEnd = true;
            };

}]);