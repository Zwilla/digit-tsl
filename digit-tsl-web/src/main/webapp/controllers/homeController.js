digitTslWeb.controller('homeController',['$scope','$modal','$q','$location','$timeout','httpFactory','showModal',
	   function ($scope,$modal,$q,$location,$timeout,httpFactory,showModal){

			$scope.load=false;
			$scope.loadingTrustBackbone= $scope.homeController_loadingBackbone;

			/**
			 * GET TrustBackbone Report
			 */
			$scope.tlReport = function() {
				httpFactory.get("/api/list/tlReport",$scope.homeController_backboneLoadingFailure).then(function(data) {
					$scope.messages = data;
					httpFactory.get("/api/audit/news",$scope.homeController_newsLoadingFailure).then(function(news) {
					    $scope.news = news;
	                })
	                .finally(function(){
	                    $scope.load=true;
	                    var cc = $location.search().cc;
	                    if(cc){
	                    	for(var i=0;i<data.length;i++){
	                    		if(data[i].territoryCode==cc){
	                    			$timeout(function(tl) {
	                    				$scope.availabilityModal(tl);
	                    			}, 500, true, data[i]);
	                    		}
	                    	}
	                    }
	                });
				}, function(){
				    $scope.load=true;
				});
			};

		    /*----------------- Dynamic Columns Management -----------------*/

		    /**
			 * Get TrustBB dynamic columns
			 */
		    var getDynamicColumns = function(){
		    	httpFactory.get("/api/system/columns/",$scope.homeController_errorColumnsFailure).then(function(data) {
		    		$scope.dynamicColumns = data;
		    		for(var i=0;i<$scope.dynamicColumns.length;i++){
		    			if($scope.dynamicColumns[i].name=="AVAILABILITY"){
		    				$scope.availabilityShow = $scope.dynamicColumns[i].visible;
		    			}else if($scope.dynamicColumns[i].name=="SIGSTATUS"){
		    				$scope.sigStatusShow=$scope.dynamicColumns[i].visible;
		    			}else if($scope.dynamicColumns[i].name=="CONTACTS"){
		    				$scope.contactsShow = $scope.dynamicColumns[i].visible;
		    			};
		    		};
		    	});
		    };
		    getDynamicColumns();

		    /*----------------- Contact -----------------*/

		    $scope.initContact = function(tl){
		        tl.contactAddress = "";
		        if(tl.contact!=null && tl.contact.electronicAddress!=null){
		            for(var i=0;i<tl.contact.electronicAddress.uri.length;i++){
		                tl.contactAddress = tl.contactAddress + tl.contact.electronicAddress.uri[i]+"; ";
		            };
		        };
		    };

		    $scope.detailContact = function(tl){
		    	var modalInstance = $modal.open({
					templateUrl: 'modalTlsoContact',
					backdrop : 'static',
					size : 'lg',
					controller: modalTlsoContactController,
					resolve : {
						tl : function() {
							return tl;
						}
					}
				});
		    }


		    $scope.detailAllContact = function(){
		        var allContact = "";
		        for(var i=0;i<$scope.messages.length;i++){
		            allContact = allContact + $scope.messages[i].contactAddress;
		        };
		        var templateHtml =
		            '<div class="panel panel-primary">'+
		                '<div class="panel-heading">'+
		                    '<span>TLSO Contact</span> <span class="fa fa-times cursor-pointer" ng-click="ok()" style="margin-left: 5px; float: right"></span>'+
		                '</div>'+
		                '<div class="panel-body">'+
		                    '<textarea readonly style="margin-top: 10px; width: 100%; height: 400px; resize: none;">'+allContact+'</textarea>'+
		                '</div>'+
		                '</div>';
		        var modalInstance = $modal.open({
                    template: templateHtml,
                    backdrop : 'static',
                    size : 'lg',
                    controller: ModalInstanceCtrl
                });
		    };

		    /*--------------------- AVAILABILITY ------------------------------*/

		    $scope.availabilityModal = function(tl){
                availability.country = tl.territoryCode;
                var modalInstance = $modal.open({
                    templateUrl: "modalAvailability",
                    backdrop : 'static',
                    windowClass: 'modal-check',
                    controller: modalAvailabilityChartController,
                    resolve : {
                        tl : function(){
                            return tl;
                        }
                    }
                });
		    };

		    $scope.switchCarret = function(carret){
		        if(carret){
		            return "fa fa-chevron-circle-up";
		        }else{
		            return "fa fa-chevron-circle-down";
		        }
		    }


    }]);