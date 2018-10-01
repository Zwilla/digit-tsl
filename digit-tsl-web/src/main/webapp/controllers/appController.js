digitTslWeb.controller('appController',['$scope','$http','$modal','$anchorScroll','$location','$window','httpFactory','accessRightFactory','$cookies','$q','cookieFactory','showModal','appConstant',
        function ($scope,$http, $modal,$anchorScroll,$location,$window,httpFactory,accessRightFactory,$cookies,$q,cookieFactory,showModal,appConstant){

        $scope.cssMain = "container"
        $scope.loadDraft = false;
        $scope.notificationList = [];
        $scope.loadDraftNotificationStatus = "";
        $scope.loadDraftNotification = false;
        $scope.listDiff = [];
        $scope.drafts = [];
        $scope.draftStoreId = null;
        $scope.draftStoreDeffered = $q.defer();

		initScope($scope);
		initMessages($scope);


		/*-------------- Menu & Page loading ----------------*/

		/**
         * Menu
         */
		$scope.initMenu = function() {
			$scope.getUserInfo().then(function() {});
			tlDraftReport();
		};

		/**
         * Get User information
         */
		$scope.getUserInfo = function(){
		    var deferred = $q.defer();
			httpFactory.get("/api/user/info",$scope.appController_errorUserLoading).then(function(data) {
				$scope.userInfo = data;
				deferred.resolve();
			}, function(){
			    deferred.reject(-1);
			});
            return deferred.promise;
		};

		/**
         * Draft
         */
		var tlDraftReport = function() {
			$scope.menuDraft = false;
			$scope.loadDraft = false;
			var promise = $scope.draftStoreDeffered.promise;
			cookieHandler();
			promise.then(function (){
				httpFactory.get("/api/list/tlDraft/"+$scope.draftStoreId,$scope.appController_errorDraftLoading).then(function(data) {
					$scope.drafts = data;
					$scope.draftStoreValidity = true;
				})
				.finally(function (){
					$scope.loadDraft = true;
					$scope.menuDraft = true;
				});
			}, function(){
				$scope.loadDraft = true;
				$scope.menuDraft = true;
			});
		};


		/*-----------------------------------------------*/


		/**
         * Get Contact modal
         */
		$scope.getContact = function(){
			var modalInstance = $modal.open({
				templateUrl: "modalContact",
				backdrop : 'static',
				controller: ModalInstanceCtrl
			});
		};


		/*---------------- Properties ------------------*/

		/**
         * Get application properties
         */
		$scope.getApplicationProperties = function(territory){
			resetProperties();
			httpFactory.get("/api/properties/all",$scope.appController_errorPropertieLoading).then(function(data) {
				$scope.myData = data;
			})
			.finally(function (){
				if($scope.myData!= undefined ){
					for(var i=0;i<$scope.myData.length;i++){
						var value=$scope.myData[i];
						affectPropertiesTo(value,territory);
					};
					countryCodeNameProperties.sort(dynamicSort("description"));
				}
			});
		};

		/**
         * Affect a properties to the right list w/ the Code List Set territory for scheme territory / community rules to filter lotl/tl - EU & other MS pointer properties in combo
         */
		var affectPropertiesTo = function(value,territory){
			if(value.codeList=="ADRTYPE"){
				adrTypeProperties.push(value);
			}else if(value.codeList=="COUNTRYCODENAME"){
				countryCodeNameProperties.push(value);
			}else if(value.codeList=="LANGUAGES"){
				languagesProperties.push(value);
			}else if(value.codeList=="MIMETYPE"){
				mimeTypeProperties.push(value);
			}else if(value.codeList=="QUALIFIERS"){
				qualifiersProperties.push(value);
			}else if(value.codeList=="SERVICE_STATUS"){
				serviceStatusProperties.push(value);
				servicePreviousStatusProperties.push(value);
			}else if(value.codeList=="SERVICE_TYPES_IDENTIFIERS"){
				serviceTypeIdentifiersProperties.push(value);
			}else if(value.codeList=="IDENTIFIER_QUALIFIER_TYPE"){
				identifierQualifierTypeProperties.push(value);
			}else if(value.codeList=="ADDITIONNAL_QUALIFIER"){
				additionnalServiceInfoQualifierProperties.push(value);
			}else if(value.codeList=="SERVICE_PREVIOUS_STATUS"){
				servicePreviousStatusProperties.push(value);
			}else{
				if(territory=="EU"){
					if(value.codeList=="LOTL_EU_TYPE"){
					    value.territory = "EU";
						schemeInfoTSLType.push(value);
						pointerTLType.push(value);
					}else if(value.codeList=="LOTL_EU_STATUSDETERM"){
						schemeInfoStatusDetermination.push(value);
					}else if(value.codeList=="LOTL_SCHMETYPECOMMUNITY"){
					    value.territory = "EU";
						schemeInfoCommunityRule.push(value);
						pointerCommunityRule.push(value);
					}else if(value.codeList=="TL_EU_TYPE"){
					    value.territory = "TL";
						pointerTLType.push(value);
					}else if(value.codeList=="TL_COMMUNITYRULE"){
					    value.territory = "TL";
						pointerCommunityRule.push(value);
                    };
				}else{
					if(value.codeList=="TL_EU_TYPE"){
					    value.territory = "TL";
						schemeInfoTSLType.push(value);
					}else if(value.codeList=="TL_STATUS_DETERM_TYPE"){
						schemeInfoStatusDetermination.push(value);
					}else if(value.codeList=="TL_COMMUNITYRULE"){
					    value.territory = "TL";
						schemeInfoCommunityRule.push(value);
					}else if(value.codeList=="LOTL_EU_TYPE"){
					    value.territory = "EU";
						pointerTLType.push(value);
					}else if(value.codeList=="LOTL_SCHMETYPECOMMUNITY"){
					    value.territory = "EU";
						pointerCommunityRule.push(value);
					};
				};
			};
		};

		var resetProperties = function(){
			adrTypeProperties = [];
			countryCodeNameProperties = [];
			languagesProperties = [];
			mimeTypeProperties = [];
			schemeInfoTSLType = [];
			schemeInfoStatusDetermination = [];
			schemeInfoCommunityRule = [];
			pointerTLType = [];
			pointerCommunityRule = [];
			serviceStatusProperties = [];
			qualifiersProperties = [];
			serviceTypeIdentifiersProperties = [];
			identifierQualifierTypeProperties = [];
			additionnalServiceInfoQualifierProperties = [];
			servicePreviousStatusProperties = [];
		};

		/*-----------------------------------------------*/


		/**
         * Check role Authorization
         */
		$scope.authorized = function(label){
			return accessRightFactory.authorized($scope.userInfo,label);
		};

		$scope.isUser = function(label){
		    return accessRightFactory.isUser($scope.userInfo,label);
		};

		/*---------------- Signature ------------------*/

		/**
         * Initialize the signature process
         */
		$scope.signTl = function(myTl,fromDraft,signatureInfo){
	        if(!myTl.checkToRun){
	            // Checks/changes are disabled
                showModal.information($scope.sign_check_run).then(function(){});
	        }else{
	            // Checks/changes are enabled
    			var tlInfo = new Object();
    			tlInfo.id = myTl.id;
    			tlInfo.sigStatus = myTl.sigStatus;
    			tlInfo.fromDraft = fromDraft;
    			if(fromDraft){ // Sign frow draft view
    				tlInfo.dbName = myTl.name;
    				tlInfo.sequenceNumber = myTl.sequenceNumber;
    				tlInfo.signatureStatus = myTl.sigStatus;
    			}else{ // Sign from browser
    				tlInfo.dbName = myTl.dbName;
    				tlInfo.sequenceNumber = myTl.schemeInformation.sequenceNumber;
    				if(signatureInfo==null){
    				    tlInfo.signatureStatus = "";
    				}else{
    				    tlInfo.signatureStatus = signatureInfo.indication;
    				}
    			};
    			if(tlInfo.signatureStatus==null || tlInfo.signatureStatus=="NOT_SIGNED"){ // If TL is not signed -> sign directly
    				updateSignature(tlInfo,myTl,fromDraft,signatureInfo);
    			}else{ // else show a warning message about the signature
    					// already existing
    				var modalHtml = '<div class="modal-content"><div class="panel panel-primary"><div class="panel-heading"><span>Application: Confirmation</span></div><div class="panel-body">'+$scope.appController_alreadySign+'</div>'+footerModal2B+"</div>";
    				var modalInstance = $modal.open({
    					template: modalHtml,
    					backdrop : 'static',
    					controller: ModalInstanceCtrl
    				});

    				modalInstance.result.then(function(result) {
    					updateSignature(tlInfo,myTl,fromDraft,signatureInfo);
    				});
    			};
	        };
		};

		/**
         * Open modal Signature & Update Status
         */
		var updateSignature = function(tlInfo,myTl,fromDraft,signatureInfo){
			tlInfo.userInfo = $scope.userInfo;
			var modalInstanceSign = $modal.open({
				templateUrl : 'modalSign',
				controller : modalSignController,
				backdrop : 'static',
				resolve : {
					myTLInfo : function() {
						return tlInfo;
					},
					myTl : function() {
						return myTl;
					}
				}
			});

			modalInstanceSign.result.then(function(status) {
				if(status){
					if(fromDraft){
						myTl.sigStatus = status.indication;
					}else{
						signatureInfo.indication = status.indication;
						signatureInfo.signedBy = status.signedBy;
						signatureInfo.signingDate = status.signingDate;
						signatureInfo.signatureFormat = status.signatureFormat;
						signatureInfo.digestAlgo = status.digestAlgo;
						signatureInfo.encryptionAlgo = status.encryptionAlgo;
						signatureInfo.subIndication = status.subIndication;
					};
				};
			});
		};


		/*---------------- Style CSS Handler ------------------*/

		$scope.styleNotificationStatus = function(status){
            if(status==undefined || status==null){
                return "<span class='invisible-icon'>";
            }else{
                if(status=="INDRAFT" || status=="PROCESSED"){
                    return '<span class="fa-stack">'+
                        '<i class="fa fa-circle fa-stack-1x white" ></i>'+
                        '<i class="fa fa-check-circle fa-stack-1x successColor"></i>'+
                    '</span>';
                }else{
                    return '<span class="fa-stack">'+
                        '<i class="fa fa-circle fa-stack-1x white" ></i>'+
                        '<i class="fa fa-times-circle fa-stack-1x errorColor"></i>'+
                        '</span>';
                };
            };
        };

		$scope.styleTbStatus = function(status){
			if(status==undefined || status==null){
	    		return "<span class='invisible-icon'>";
	    	}else{
				 if (status=="SUCCESS"){
					 return '<span class="fa-stack">'+
						'<i class="fa fa-circle fa-stack-1x white" ></i>'+
						'<i class="fa fa-check-circle fa-stack-1x successColor"></i>'+
					'</span>';
				}else if(status=="WARNING"){
					 return '<span class="fa-stack">'+
						'<i class="fa fa-circle fa-stack-1x white" ></i>'+
						'<i class="fa fa-times-circle fa-stack-1x warnColor"></i>'+
					'</span>';
				}else{
					return '<span class="fa-stack">'+
							'<i class="fa fa-circle fa-stack-1x white" ></i>'+
							'<i class="fa fa-times-circle fa-stack-1x errorColor"></i>'+
						'</span>';
				};
	    	};
	    };

		$scope.styleDate = function(date){
			var dateIn = new Date(date);
			var dateNow = new Date();
			var oneDay = 24*60*60*1000; // hours*minutes*seconds*milliseconds

		    if (dateIn<dateNow){
		    	return "errorColor";
		    }else{
		    	var diffDays = Math.round(Math.abs((dateIn.getTime() - dateNow.getTime())/(oneDay)))
		    	if (diffDays<30){
		    		return "warnColor";
		    	};
		    };
		    return "";
	    };

	    /* --------------------------------------------------- */

		/**
         * Go to top of page
         */
		$scope.goToTop = function() {
	      $location.hash('top');
	      $anchorScroll();
	      $location.hash('');
	    };



	    /** Cookies * */
	    var cookieHandler = function(){
	        $scope.draftStoreId = "";
	    	// Get cookie
	    	cookieFactory.getCookie().then(function(draftStoreId){
	    		$scope.draftStoreId = draftStoreId;
		    	// Check URL location
		    	var url = $location.absUrl();
		    	if(url.indexOf("/tl-manager/drafts/")!=-1){
		    		// MyDraft
		    		var endPath= $location.absUrl().split('/tl-manager/drafts/')[1];
					var pathDraftStoreId= endPath.split("/")[0];
					if(pathDraftStoreId==null || pathDraftStoreId==""){
					    // access draft/
					    if($scope.draftStoreId!=null){
					        $window.location.href = '/tl-manager/drafts/'+$scope.draftStoreId;
					    }else{
    						cookieFactory.createCookie().then(function(draftStoreId){
    							$scope.draftStoreId = draftStoreId;
    							$window.location.href = '/tl-manager/drafts/'+$scope.draftStoreId;
    						}, function(){
    							$scope.draftStoreDeffered.reject(-1);
    						});
					    }
					}else{
					    // access draft/{draftStore}
						cookieFactory.verify(pathDraftStoreId).then(function(validity) {
							if(validity){
								// Valid
								if($scope.draftStoreId==null){
									cookieFactory.setCookie(pathDraftStoreId).then(function(pathDraftStoreId){
									    showModal.draftStoreInformation(appConstant.cookieFactory.createCookie).then(function(){
									        $scope.draftStoreId = pathDraftStoreId;
									        $scope.draftStoreDeffered.resolve();
						                });
									});
								}else if($scope.draftStoreId==pathDraftStoreId){
									// Path Id is Cookie Id; load draftStore
									$scope.draftStoreDeffered.resolve();
								}else{
									// Path Id is different than Cookie Id; pop-up
									showModal.confirmation(appConstant.cookieFactory.replaceStore,appConstant.cookieFactory.repository).then(function(){
										// Replace cookie with path id
										cookieFactory.setCookie(pathDraftStoreId);
										$scope.draftStoreId = pathDraftStoreId;
										$scope.draftStoreDeffered.resolve();
									}, function(){
										// Redirect to cookie draftStore
										$window.location.href = '/tl-manager/drafts/'+$scope.draftStoreId;
									});
								};
							}else{
								// Invalid
								if($scope.draftStoreId==null){
									// No cookie
									cookieFactory.createCookie().then(function(draftStoreId) {
										showModal.confirmation(appConstant.cookieFactory.invalidUrl,appConstant.cookieFactory.repository).then(function(){
											$scope.draftStoreId = draftStoreId;
											$window.location.href = '/tl-manager/drafts/'+$scope.draftStoreId;
										});
									}, function(){
										$scope.draftStoreDeffered.reject(-1);
									});
								}else if($scope.draftStoreId==pathDraftStoreId){
									// Cookie id = Path url
									showModal.draftStoreInformation(appConstant.cookieFactory.invalidDraftStore).then(function(){
										cookieFactory.createCookie().then(function(draftStoreId) {
											$scope.draftStoreId = draftStoreId;
											$window.location.href = '/tl-manager/drafts/'+$scope.draftStoreId;
										}, function(){
											$scope.draftStoreDeffered.reject(-1);
										});
									});
								}else{
									// Cookie id different Path url
									showModal.draftStoreInformation(appConstant.cookieFactory.invalidUrl).then(function(){
			    						$window.location.href = '/tl-manager/drafts/'+$scope.draftStoreId;
			    					});
								};
							};
						}, function(){
							$scope.draftStoreDeffered.reject(-1);
						});
					};
		    	}else{
		    	    // Outside MyDraft
                    if(endsWith(url,"tl-manager/")){
		    			// Root tl-manager/
		    			if($scope.draftStoreId!=null){
		    				// Has cookie
		    				$window.location.href = '/tl-manager/drafts/'+$scope.draftStoreId;
		    			}else{
		    				// No cookie
		    				cookieFactory.createCookie().then(function(draftStoreId){
	    						$scope.draftStoreId = draftStoreId;
	    						$window.location.href = '/tl-manager/drafts/'+$scope.draftStoreId;
							}, function(){
								$scope.draftStoreDeffered.reject(-1);
							});
		    			}
		    		}else{
		    			// Application
			    		if($scope.draftStoreId!=null){
			    			cookieFactory.verify($scope.draftStoreId).then(function(validity) {
			    				if(validity){
			    					$scope.draftStoreDeffered.resolve();
			    				}else{
			    					$scope.draftStoreDeffered.reject(-1);
			    				};
			    			}, function(){
			    				$scope.draftStoreDeffered.reject(-1);
			    			});
						}else{
							$scope.draftStoreDeffered.reject(-1);
						};
		    		};
		    	};
	    	});
	    };

	    $scope.newDraftStore = function(){
	    	showModal.confirmation(appConstant.cookieFactory.newStore,appConstant.cookieFactory.repository).then(function(){
	    		cookieFactory.createCookie().then(function(draftStoreId){
					$scope.draftStoreId = draftStoreId;
					$window.location.href = '/tl-manager/drafts/'+$scope.draftStoreId;
				});
			});
	    };

	}]);





