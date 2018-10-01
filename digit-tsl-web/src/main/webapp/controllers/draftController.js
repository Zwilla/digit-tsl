digitTslWeb.controller('draftController',[ '$scope','$http','$modal','$timeout','$window','$parse','httpFactory','$window','showModal','$location',
                                   		function($scope,$http, $modal,$timeout,$window,$parse,httpFactory,$window,showModal,$location) {

		    $scope.getApplicationProperties();
		    $scope.loadingDrafts = $scope.draftController_loading;
		    $scope.country = countryCodeNameProperties;
		    $scope.territory = "";

		    /** Invoke modal disclaimer * */
		    var disclaimer = function(){
		    	$scope.disclaimerLoading = true;
				var modalInstance = $modal.open({
					templateUrl: 'modalCloneDisclaimer',
					keyboard: false,
					backdrop : 'static',
					size: 'lg',
					scope:$scope,
					controller: modalDisclaimerController
				});
				return modalInstance;
		    };

		    /**
		     * Loading Status update Process
		     */
			var loadingStatusProcess = function(){
				$scope.loadingDrafts = $scope.clone_create;
				setTimeout(function () {
			        $scope.$apply(function () {
			        	$scope.loadingDrafts=$scope.clone_check;
			        });
			    }, 2000);
				setTimeout(function () {
			        $scope.$apply(function () {
			        	$scope.loadingDrafts=$scope.clone_uri;
			        });
			    }, 12000);
			};

		    /**
		     * Create new Draft From EU published version
		     */
		    $scope.createFromPublish = function(territory) {
		    	if($scope.territory!=null || $scope.territory!=""){
			    	$scope.loadDraft = false;
			    	$scope.loadingDrafts=$scope.draftController_initProcess;
			    	// Get TL version
			    	httpFactory.get("/api/tl/version/"+$scope.territory,$scope.draftController_versionError).then(function(version) {
			    		if(version!=5){
			    			var modalInstance = disclaimer();
			    		}else{
			    			loadingStatusProcess();
			    		};
			    		var clone = {
								territory : $scope.territory,
								cookieId : $scope.draftStoreId
						};
				    	httpFactory.put("/api/tl/clone/",clone,$scope.draftController_creatingDraftFailure).then(function(data) {
				    		$scope.drafts.push(data);
				    		if(version!=5){
					    		$scope.disclaimerLoading = false;
					    		$scope.loadingDrafts=$scope.draftController_redirection;
					    		modalInstance.result.then(function(status) {
									$window.location.href = '/tl-manager/tl/'+data.id;
				    			});
				    		}else{
				    			$window.location.href = '/tl-manager/tl/'+data.id;
				    		};
				    	}, function(){
				    	    if(modalInstance!=undefined && modalInstance!=null){
				    	        modalInstance.close();
				    	    }
							$scope.loadingDrafts="";
							$scope.territory="";
							$scope.loadDraft = true;
						});
			    	}, function(){
						$scope.loadingDrafts="";
						$scope.territory="";
						$scope.loadDraft = true;
					});
		    	};
		    };

		    /**
		     * Delete a Draft
		     */
		    $scope.deleteDraft = function(draft) {
		    	$scope.loadingDrafts = $scope.draftController_deleteProgress;
		    	$scope.loadDraft = false;

		    	var tlDraftDelete = {
                        tlId : draft.id,
                        cookie : $scope.draftStoreId,
                        rejected : false
                };

		    	if(draft.territoryCode=="EU"){
		    	    // LOTL
		    	    httpFactory.get("/api/draft/checkNotification/"+draft.id,$scope.draftController_notificationError).then(function(authorized) {
		    	        if(!authorized){
		    	            var validDeletion = true;
		    	            // Has notification in draft for current draft (can't use showModal.confirmation here because of three case OK/CANCEL/DETACH)
		    	            var modalHtml = '<div class="panel panel-primary" >' +
		    	            '<div class="panel-heading">' +
		    	                '<span>'+$scope.notificationInformation+'</span>'+
		    	                '</div>' +
		    	                    '<div class="panel-body">'+
		    	                        '<span class="block">'+$scope.draftController_deleteNotified1+'</span><br>'+
		    	                        '<span class="block">'+$scope.draftController_deleteNotified2+'</span>'+
		    	                        '<span class="block">'+$scope.draftController_deleteNotified3+'</span>'+
										'<span class="block">'+$scope.draftController_deleteNotified4+'</span>'+
		    	                    '</div>'+
		    	                '<div class="panel-footer" align="right">'+
		    	                    '<button class="btn btn-primary" ng-click="ok()" style="margin-right:10px;">Close</button>'+
		    	                    '<button class="btn btn-primary" ng-click="cancel(0)" style="margin-right:10px;">Detach</button>'+
		    	                    '<button class="btn btn-warning" ng-click="cancel(-1)">Cancel</button>'+
		    	                '</div></div>';


		    	            var modalInstance = $modal.open({
		    	              template: modalHtml,
		    	              controller: ModalInstanceCtrl,
		    	            });

		    	            modalInstance.result.then(function() {
		    	                // Validate notification in draft
		    	                tlDraftDelete.rejected= false;
		    	                deleteDraft(tlDraftDelete,draft);
		    	            }, function(response){
		    	                if(response==-1){
		    	                    $scope.loadingDrafts="";
		                            $scope.loadDraft = true;
		                            return;
		    	                }else{
		    	                    tlDraftDelete.rejected= true;
		    	                    deleteDraft(tlDraftDelete,draft);
		    	                };
		    	            });
		    	        }else{
		    	            deleteDraft(tlDraftDelete,draft);
		    	        };
		    	    }, function(){
		    	        $scope.loadingDrafts="";
                        $scope.loadDraft = true;
		    	    });
		    	}else{
		    	    // TL
		    	    deleteDraft(tlDraftDelete,draft);
		    	};
			  };

			  var deleteDraft = function(tlDraftDelete,draft){
			      httpFactory.post("/api/draft/delete",tlDraftDelete,$scope.draftController_draftDeleteFailure).then(function(data) {
                      $scope.drafts.splice( $scope.drafts.indexOf(draft), 1 );
                  })
                  .finally(function (){
                      $scope.loadingDrafts="";
                      $scope.loadDraft = true;
                  });
			  };

			  /**
			     * Upload file on the server
			     */
			  $scope.uploadFile = function(myFile,fileInput){
				  var file = myFile;
				  $scope.loadDraft = false;
				  $scope.loadingDrafts = $scope.draftController_loadingImport;
			      if($scope.testFile(file)){
					var fd = new FormData();
					fd.append('file', file);
					$http.post('/tl-manager/fileUpload/'+$scope.draftStoreId , fd, {
					    transformRequest: angular.identity,
					    headers: {'Content-Type': undefined}
					})
					.success(function(data, status, headers, config) {
						if(serviceResponseHandler(data)){
							$scope.drafts.push(data.content);
							// If migrated -> invoke
							// disclaimer
							if(data.content.migrate){
								var modalInstance = disclaimer();
								$scope.disclaimerLoading = false;
					    		$scope.loadingDrafts=$scope.draftController_redirection;
					    		modalInstance.result.then(function(status) {
									$window.location.href = '/tl-manager/tl/'+data.content.id;
				    			});
							}else{
							    $window.location.href = '/tl-manager/tl/'+data.content.id;
							};
						}else{
						    showModal.httpStatusHandler(data.responseStatus,$scope.draft_importError);
							$scope.loadingDrafts = "";
							$scope.loadDraft = true;
						};
					})
					.error(function(data, status, headers, config) {
					    showModal.httpStatusHandler(status,$scope.draft_importError);
						$scope.loadingDrafts = "";
						$scope.loadDraft = true;
					});
			      }else{
			    	$scope.loadingDrafts = "";
					$scope.loadDraft = true;
			      };
			      // Clean fileinput
			      fileInput[0].value = "";
			    };

			  /**
			     * Check type of file
			     */
			  $scope.testFile = function(myFile){
		        var file = myFile;
		        angular.element('#uploadFile').val='';
		        if(file.type!="text/xml"){
		            showModal.information($scope.draftController_xmlFile);
				    return false;
		        };
		        return true;
			  };
			  
			  /**
			   * Show warning pop-up when try to download XML/Report/SHA2 when TL check to run is set to false
			   */
			  $scope.exportCheckToRunError = function(){
			      showModal.information($scope.export_check_run);
			  };

	}]);

