//Specific AngularJS version used in browserController, JS file is located in > /digit-tsl-web/src/main/webapp/script/angular/angular-min.js
digitTslWeb.controller('browserController', [ '$scope','$modal','$window','$location','digitalIdService','trustedListFactory','showModal','httpFactory','extensionFactory','$filter','$q','nexuFactory','cookieFactory','appConstant',
		function($scope,$modal,$window,$location,digitalIdService,trustedListFactory,showModal,httpFactory,extensionFactory,$filter,$q,nexuFactory,cookieFactory,appConstant) {
	
			$scope.load=false;
			$scope.schemeInformationMenuShow='0';
			$scope.ptotsMenuShow='0';
			$scope.serviceProviderShow='0';
			$scope.ptotsEdit=false;
			$scope.providerEdit = false;
			$scope.loadingStatus = "";

			// Change list
			$scope.listDiffSchemeInfo = [];
			$scope.listDiffPointer = [];
			$scope.listDiffProviders = [];
			$scope.listDiffSignature = [];

			/* Tabset activity handler */
			$scope.operatorNameActivity = [];
			$scope.postalAddressActivity = [];
			$scope.electronicAddressActivity = [];
			$scope.schemeNameActivity = [];
			$scope.informationURIActivity = [];
			$scope.communityRuleActivity = [];
			$scope.legalNoticeActivity = [];

			$scope.draftEditionCheckVisible = false;
			$scope.tlChangesProd = null;
			$scope.tlccIsActive = false;
			
			/* TSP/Service filter */
			$scope.tspFilter = {value:''};
			$scope.noResultWarning = false;

			/**
			 * Loading Status update Process
			 */
			var loadingStatusProcess = function(){
				$scope.loadingStatus = $scope.clone_create;
				setTimeout(function () {
			        $scope.$apply(function () {
			        	$scope.loadingStatus=$scope.clone_check;
			        });
			    }, 2000);
				setTimeout(function () {
			        $scope.$apply(function () {
			        	$scope.loadingStatus=$scope.clone_uri;
			        });
			    }, 12000);
			};

			/** **************** TEMPLATE ***************** */

			var templateGeneric =
				'<div class="panel panel-primary">'+
					'<div class="panel-heading">'+
						'<div style="width: 97% !important; display: inline-block;">'+
							'<check disabled="myTLInfo.checkToRun" id="myTLInfo.schemeInformation.id+\'_\'+myTLInfo.id" listcheck="myTLInfo.listChecks" label="label" istreeparent="false" style="margin-right:3px;"></check>'+
							'<change id="myTLInfo.schemeInformation.id+\'_\'+myTLInfo.id" listdiff="myTLInfo.listdiff" label="label" istreeparent="false" style="margin-right:3px;"information="information"></change>'+
							'<span>{{myTLInfo.dbName}} ({{myTLInfo.schemeInformation.sequenceNumber}}) : {{label}}</span>'+
							'<span class="requiredIcon" tooltip="{{tRequiredInStandard}}" tooltip-placement="right">(*)</span>'+
						'</div>'+
						'<span class="fa fa-times cursor-pointer" ng-click="cancel()"></span>'+
						'<span ng-click="ok()" style="margin-left: 5px;" class="fa fa-check cursor-pointer" ></span>'+
					'</div>'+
					'<div class="panel-body">';

			/* Generic select for TSL simple information Modal */
			var selectGeneric = '<select class="col-sm-9"  ng-model="value" required><option ng-repeat="lP in listProperties" value="{{lP.label}}" ng-selected="lP.label == value" >{{lP.label}}</select>';

			/**
			 * Get TrustedList Loading signature / changes / checks
			 */
			$scope.tlDetail = function(varId) {
				if (typeof(varId)==='undefined'){ // varId undefined ->
													// loading page
					var endPath= $location.absUrl().split('tl-manager/tl/')[1];
					var id = endPath.split("/")[0];
				}else{ // varId not undefined -> after cloning a new draft
					var id = varId;
				};
				$scope.cssMain = "container disabled-click"
				$scope.loadingStatus = $scope.browserController_loadingTrustedList;
				$scope.tlIdDb = id;
				/** FACTORY * */
				// Trusted List
				var promise = $scope.draftStoreDeffered.promise;
				promise.then(function (){})
				.finally(function(){
					var tlCookie = {
							tlId : id,
							cookie : $scope.draftStoreId
					};

					trustedListFactory.getTl(tlCookie).then(function(tl) {
						$scope.myTl=tl;
						$scope.myTl.isInit = false;
						tlCookie.tlId = tl.id;
						$scope.getApplicationProperties($scope.myTl.schemeInformation.territory);
						$scope.myTl.userEditable = true;
						$scope.loadingStatus = $scope.browserController_loadingSignature
						// Signature Info
						trustedListFactory.getSignatureInfo(tlCookie).then(function(signature) {
							$scope.signatureInfo = signature;
							if($scope.myTl.checkToRun){
							    $scope.loadingStatus = $scope.browserController_loadingChanges
                            };
							// Scheme Info differences
							trustedListFactory.getAllDifference(tlCookie, $scope.myTl.checkToRun).then(function(list) {
							    if(list!=null){
    								$scope.listDiffSchemeInfo = list[0];
    								$scope.listDiffPointer = list[1];
    								$scope.listDiffProviders = list[2];
    								$scope.listDiffSignature = list[3];
							    };

							    if($scope.myTl.dbStatus=="DRAFT"){
							        $scope.information = {
							                type : "DRAFT",
							                currentTL : "",
							                comparedTL : "",
							        };
							    };
							    if($scope.myTl.checkToRun){
							        $scope.loadingStatus = $scope.browserController_loadingChecks;
                                };
								// Checks
								trustedListFactory.getChecks(tlCookie, true).then(function(checks) {
									$scope.listChecks = checks;
								})
								.finally(function() {
								    initNotified(tlCookie);
							    });
							}, function(){
							    if($scope.myTl.checkToRun){
							        $scope.loadingStatus = $scope.browserController_loadingChecks;
							    };
								// Checks
								trustedListFactory.getChecks(tlCookie, $scope.myTl.checkToRun).then(function(checks) {
									$scope.listChecks = checks;
								})
								.finally(function() {
								    initNotified(tlCookie);
							    });
							});
						}, function(){
							// Scheme Info differences
							trustedListFactory.getAllDifference(tlCookie, $scope.myTl.checkToRun).then(function(list) {
							    if(list!=null){
    								$scope.listDiffSchemeInfo = list[0];
    								$scope.listDiffPointer = list[1];
    								$scope.listDiffProviders = list[2];
    								$scope.listDiffSignature = list[3];
							    };
							    if($scope.myTl.checkToRun){
							        $scope.loadingStatus = $scope.browserController_loadingChecks;
                                };
								// Checks
								trustedListFactory.getChecks(tlCookie, $scope.myTl.checkToRun).then(function(checks) {
									$scope.listChecks = checks;
								})
								.finally(function() {
								    initNotified(tlCookie);
							    });
							}, function(){
								trustedListFactory.getChecks(tlCookie).then(function(checks) {
									$scope.listChecks = checks;
								})
								.finally(function() {
								    initNotified(tlCookie);
							    });
							});
						});
					}, function(response){
						if(response==-401){
						    showModal.applicationError($scope.browserController_notAuthorized);
						};
						unlockView();
					});
				});
			};
			$scope.tlDetail();

			var initNotified = function(tlCookie){
			    if($scope.myTl.dbStatus =="DRAFT" && $scope.myTl.schemeInformation.territory=="EU"){
                    $scope.loadingStatus = $scope.browserController_loadingNotifiedPointer;
                    trustedListFactory.notifiedPointer(tlCookie).then(function(notifiedPointer){
                        $scope.notifiedPointer = notifiedPointer;
                    })
                    .finally(function(){
                        unlockView();
                    });
                }else{
                    unlockView();
                };
			};

			/**
			 * Post the modification to do in the XML file typeEdit (schemeInfo /
			 * tlPointer / tlServiceProvider / tlService / tlServiceHistory)
			 * editionObject : tlId ; tlSchemeInfo; editAttr ; message : message
			 * in error modal fCall :complementary function call at the end of
			 * process for some case indexProvider : index of provider for
			 * checks
			 */
			var postModif = function(typeEdit,editionObject,message, fCall,indexProvider){
				// Init modification post
			    var tlCookie = {
                        tlId : $scope.myTl.tlId,
                        cookie : $scope.draftStoreId
                };

				$scope.cssMain = "container disabled-click"
				$scope.loadingStatus= $scope.browserController_loadingModification;
				$scope.load=false;
				$scope.newValue = "";
				if (typeof(fCall)==='undefined'){ fCall = null };
				editSucces=true;
				httpFactory.put('/api/tl/edit/'+typeEdit,editionObject,message).then(function(data) {
					// Store new value (used in some fCall)
					$scope.newValue = data;
					if($scope.signatureInfo.indication!="NOT_SIGNED"){
						// Update Signature info
						trustedListFactory.getSignatureInfo(tlCookie).then(function(signature) {
							$scope.signatureInfo= signature;
						});
					};
					if(fCall!=null){
						fCall();
					};

					// Define type of edition
					var deferred = $q.defer();
					var promise = deferred.promise;
					if(typeEdit=="tlSchemeInfo"){
						$scope.myTl.schemeInformation = $scope.newValue
						callService = "schemeInfo";
						deferred.resolve();
					}else if(typeEdit=="tlPointer"){
						callService = "pointers";
						deferred.resolve();
					}else if(typeEdit=="tlServiceProvider" || typeEdit=="tlService" || typeEdit=="tlServiceHistory" || typeEdit=="createNewStatus"){
						callService = "serviceProvider";
						deferred.resolve();
					};

					promise.then(function (){
					    if($scope.myTl.checkToRun){
					        $scope.loadingStatus = $scope.browserController_loadingChanges;
                        };
						trustedListFactory.getDifference(tlCookie,callService, $scope.myTl.checkToRun).then(function(diff) {
							if(typeEdit=="tlSchemeInfo"){
								$scope.listDiffSchemeInfo = diff;
							}else if(typeEdit=="tlPointer"){
								$scope.listDiffPointer = diff;
							}else{
								$scope.listDiffProviders = diff;
							};
						})
						.finally(function() {
					        if($scope.myTl.checkToRun){
					            // Run check
                                $scope.loadingStatus = $scope.browserController_loadingChecks;
                                trustedListFactory.getChecks(tlCookie, $scope.myTl.checkToRun).then(function(checks) {
                                    $scope.listChecks = checks;
                                })
                                .finally(function() {
                                    // Get & Update last edit date
                                    trustedListFactory.getLastEdit($scope.myTl.tlId).then(function(lastEdited) {
                                        $scope.myTl.lastEdited = lastEdited;
                                    })
                                    .finally(function() {
                                        unlockView();
                                    });
                                });
                            }else {
                                // Get & Update last edit date
                                trustedListFactory.getLastEdit($scope.myTl.tlId).then(function(lastEdited) {
                                    $scope.myTl.lastEdited = lastEdited;
                                })
                                .finally(function() {
                                    unlockView();
                                });
                            }
						});
					});
				}, function(response) {
					checkConflict(response);
				});
			};

			/**
			 * Post Modif for a Scheme Info Edition Initialize parameters to
			 * PostModif generic function
			 */
			var postModifSchemeInfo = function(editAttr , fCall){
				var tlInformationEdition = {
						tlId : $scope.tlIdDb ,
						tlSchemeInfoObj : $scope.myTl.schemeInformation,
						editAttribute : editAttr,
						lastEditedDate : $scope.myTl.lastEdited,
						cookie : $scope.draftStoreId,
						tslIdentifier : $scope.myTl.tslId,
						checkToRun : $scope.myTl.checkToRun
				};
				var message = $scope.browserController_errorUpdate+" "+editAttr;
				postModif("tlSchemeInfo",tlInformationEdition, message, fCall);
			};

			/**
			 * Post Modif for a Pointer Edition Initialize parameters to
			 * PostModif generic function
			 */
            var postModifTlPointer = function(pointer,index,message){
                var tlPointerEdition = {
                        tlId : $scope.tlIdDb ,
                        tlPointerObj : pointer,
                        editAttribute : pointer.id,
                        tlTerritoryCode : $scope.myTl.schemeInformation.territory,
                        lastEditedDate : $scope.myTl.lastEdited,
                        cookie : $scope.draftStoreId,
                        checkToRun : $scope.myTl.checkToRun
                };
                if(message==undefined){
                    var pointerName=  pointer.schemeTerritory +" ("+ pointer.mimeType +") : "+ pointer.tlLocation ;
                    var message = $scope.browserController_errorUpdate+" "+pointerName;
                };
                postModif("tlPointer",tlPointerEdition, message, function(){
                    if(index!=null){
                        $scope.myTl.pointers[index] = $scope.newValue;
                    }else{
                        $scope.myTl.pointers.push($scope.newValue);
                    };
                });
            };

			/**
			 * Post Modif for a Service Provider Edition Initialize parameters
			 * to PostModif generic function
			 */
			var postModifTlServiceProvider = function(serviceProvider,index,message){
				var tlServiceProviderEdition = {
						tlId : $scope.tlIdDb ,
						tlServiceProviderObj : serviceProvider,
						editAttribute : serviceProvider.id,
						tlTerritoryCode : $scope.myTl.schemeInformation.territory,
						lastEditedDate : $scope.myTl.lastEdited,
						cookie : $scope.draftStoreId,
						checkToRun : $scope.myTl.checkToRun
				};
				postModif("tlServiceProvider",tlServiceProviderEdition, message, function(){
					if(index!=null){
						$scope.myTl.serviceProviders[index] = $scope.newValue;
					}else{
						$scope.myTl.serviceProviders.push($scope.newValue);
					};
					$scope.newValue.open = '1';
				}, index);
			};

			/**
			 * Post Modif for a Service Initialize parameters to PostModif
			 * generic function
			 */
			var postModifTlService = function(service,parentIndex,message){
				var tlServiceEdition = {
						tlId : $scope.tlIdDb ,
						tlServiceObj : service,
						tlTerritoryCode : $scope.myTl.schemeInformation.territory,
						parentIndex: parentIndex,
						lastEditedDate : $scope.myTl.lastEdited,
						cookie : $scope.draftStoreId,
						checkToRun : $scope.myTl.checkToRun

				};
				postModif("tlService",tlServiceEdition, message, function(){
					if(parentIndex!=null && parentIndex[0]!=null){
						if(parentIndex[1]!=null){
							$scope.myTl.serviceProviders[parentIndex[0]].tspservices[parentIndex[1]] = $scope.newValue;
						}else{
							$scope.myTl.serviceProviders[parentIndex[0]].tspservices.push($scope.newValue);
						};
						$scope.myTl.serviceProviders[parentIndex[0]].open = '1';
						$scope.newValue.show = '1';
					};
				}, parentIndex[0]);
			};

			/**
			 * Post Modif for a History Index : array of provider/service &
			 * history index to update parentSize : array size of
			 * providers/services & history to create new id for new history
			 * Array index (0:provider - 1:service - 2:history)
			 */
			var postModifTlHistory = function(history,parentIndex,message){
				var tlServiceHistory = {
						tlId : $scope.tlIdDb ,
						tlHistoryObj : history,
						tlTerritoryCode : $scope.myTl.schemeInformation.territory,
						parentIndex : parentIndex,
						lastEditedDate : $scope.myTl.lastEdited,
						cookie : $scope.draftStoreId,
						checkToRun : $scope.myTl.checkToRun
				};
				postModif("tlServiceHistory",tlServiceHistory, message, function (){
					if(parentIndex!=undefined || parentIndex!=null){
						$scope.myTl.serviceProviders[parentIndex[0]].tspservices[parentIndex[1]].history = $scope.newValue;
						$scope.myTl.serviceProviders[parentIndex[0]].open='1';
						$scope.myTl.serviceProviders[parentIndex[0]].tspservices[parentIndex[1]].show='1';
						$scope.myTl.serviceProviders[parentIndex[0]].tspservices[parentIndex[1]].showHistory.open = '1';
						$scope.newValue.open='1';
					};
				}, parentIndex[0]);
			};

			/**
			 * Edit TL If no draft exist ask to create a new one Else If more
			 * than one Draft already exist ask to create a new methodToCall :
			 * edit method to call for update objSub : object to modify (used
			 * for serviceProvider)
			 */
			$scope.editTlDraft= function(methodToCall,index,objSub){
				$scope.cssMain = "container disabled-click"
				if($scope.myTl.dbStatus=="PROD"){
					var length = $scope.drafts.length;
					if(length >= 0){ // There is draft already created

						if(length==0){ // Initialize modal message
							var message = $scope.browserController_redirectDraft;
						}else{
							var message = length+" "+$scope.browserController_redirectDraftBis;
						};

						showModal.confirmation(message, appConstant.modalTitle.confirmation).then(function() {
							var deferred = $q.defer();
							var promise = deferred.promise;
							// Check draftStore
							if($scope.draftStoreId==null){
								// No cookie
								cookieFactory.createCookie().then(function(draftStoreId){
									$scope.draftStoreId = draftStoreId;
									deferred.resolve();
								}, function(){
									deferred.resolve();
									unlockView();
									return;
								})
							}else{
								// Cookie
								cookieFactory.verify($scope.draftStoreId).then(function(validity){
									if(validity){
										deferred.resolve();
									}else{
										// DraftStore invalid
										cookieFactory.createCookie().then(function(draftStoreId){
											$scope.draftStoreId = draftStoreId;
											deferred.resolve();
										}, function(){
											deferred.resolve();
											unlockView();
											return;
										});
									};
								}, function(){
									deferred.resolve();
									unlockView();
									return;
								});
							};

							promise.then(function (){
								// Disclaimer between V4 & V5
								if($scope.myTl.schemeInformation.tlIdentifier!=5){
									$scope.disclaimerLoading = true;
									var modalInstance = $modal.open({
										templateUrl: 'modalCloneDisclaimer',
										keyboard: false,
										backdrop : 'static',
										size: 'lg',
										scope:$scope,
										controller: modalDisclaimerController
									});
								}else{
									loadingStatusProcess();
								};

								var clone = {
										territory : $scope.myTl.schemeInformation.territory,
										cookieId : $scope.draftStoreId
								};
								$scope.load=false;
								httpFactory.put("/api/tl/clone/",clone,$scope.browserController_errorCloning).then(function(data) {
								    $scope.tlChangesProd = null;
									$scope.drafts.push(data);
									$scope.dbId = data.id;
									window.history.pushState("", "", "/tl-manager/tl/"+$scope.dbId );
									$scope.tlDetail($scope.dbId); // Load new
																	// draft
								})
								.finally(function (){
									unlockView();
									return;
								});
							}, function(){
								unlockView();
								return;
							});
						}, function(){
							unlockView();
							return;
						});

					}
				}else{ // If we're already in a draft
					$scope.backUpTL = angular.copy($scope.myTl);
					if($scope.signatureInfo==null || $scope.signatureInfo.indication==null || $scope.signatureInfo.indication=="NOT_SIGNED"){ // Modal
																																				// Alert
																																				// if
																																				// TL
																																				// Signature
																																				// is
                                                                                                                                                // Valid
						$scope[methodToCall](index,objSub);
					}else{
					    showModal.confirmation($scope.draft_editSigned, appConstant.modalTitle.confirmation).then(function() {
					        $scope[methodToCall](index,objSub);
                        });
					};
					unlockView();
				};
			};

			/**
			 * Invoke Modal TL Info Edit (Generic)
			 */
			$scope.invokeModalSimple= function(modalInstance,templateHtml, value, label,listProperties,tag){
				if (typeof(listProperties)==='undefined') listProperties = null;
				var tlInfo = angular.copy($scope.myTl);
				tlInfo.label=label;
				tlInfo.id = tag;
				tlInfo.listdiff = $scope.listDiffSchemeInfo;
				tlInfo.listChecks = $scope.listChecks;
				tlInfo.listProperties = listProperties;
				tlInfo.checkToRun = $scope.myTl.checkToRun;
				modalInstance = $modal.open({
					template : templateHtml,
					controller : modalTLInfoEdit,
					windowClass: 'modal-fit',
					backdrop : 'static',
					resolve : {
						myValue : function() {
							return value;
						},
						myTLInfo : function() {
							return tlInfo;
						}
					}
				});
				return modalInstance;
			};

			/*--------------------------- Signature -------------------------*/

			$scope.$watch("signatureInfo", function() {
			    if($scope.myTl!=undefined && $scope.myTl!=null && $scope.myTl.isInit){
			        var tlCookie = {
                        tlId : $scope.myTl.tlId,
                        cookie : $scope.draftStoreId
                    };

	                trustedListFactory.getLastEdit($scope.myTl.tlId).then(function(lastEdited) {
	                    $scope.myTl.lastEdited = lastEdited;
	                });
                    trustedListFactory.getChecks(tlCookie, $scope.myTl.checkToRun).then(function(checks) {
                        $scope.listChecks = checks;
                    });
                    trustedListFactory.getSignatureChanges(tlCookie, $scope.myTl.checkToRun).then(function(sigChanges) {
                        $scope.listDiffSignature = sigChanges;
                    });
			    }
		    },true);

			/*--------------------------- SCHEME INFORMATION -------------------------*/

			/**
			 * TSL Id
			 */
            $scope.editTslId = function(){
                var value = $scope.myTl.tslId;
                var templateHtml = templateGeneric +'<input class="col-sm-9" ng-model="value" style="margin-left:5px;"></div></div>';
                var modalInstance = $scope.invokeModalSimple(modalInstance,templateHtml,value,$scope.tlBrowser_tslId,undefined,$scope.TAG_TSL_IDENTIFIER);

                modalInstance.result.then(function(myValue) {
                    $scope.myTl.tslId=myValue;
                    postModifSchemeInfo($scope.TAG_TSL_IDENTIFIER)
                });
            };

			/**
			 * Issue Date
			 */
			$scope.editIssueDate = function(){
				var value = $scope.myTl.schemeInformation.issueDate;
				var date = new Date(value);
				var templateHtml = templateGeneric +'<div class="row"><div class="col-md-6">'+
				    '<p class="input-group">'+
				        '<input style="height:34px!important" type="text" class="form-control" datepicker-popup="{{format}}" ng-model="value" is-open="opened" datepicker-options="dateOptions" date-disabled="disabled(date, mode)" ng-required="true" close-text="Close" />'+
				        '<span class="input-group-btn"><button type="button" class="btn btn-default" ng-click="open($event)">'+
				            '<i class="fa fa-calendar"></i>'+
				        '</button></span></p></div></div></div></div>';
				var modalInstance = $scope.invokeModalSimple(modalInstance,templateHtml,date,$scope.tlBrowser_issueDate,undefined,$scope.TAG_ISSUE_DATE);

				modalInstance.result.then(function(myValue) {
					$scope.myTl.schemeInformation.issueDate=myValue;
					postModifSchemeInfo($scope.TAG_ISSUE_DATE)
				});
			};

			/**
			 * Next Update
			 */
			$scope.editNextUpdate = function(){
				var value = $scope.myTl.schemeInformation.nextUpdateDate;
				var date = new Date(value);
				var templateHtml = templateGeneric +'<div class="row" style="display:inline;">'+
					'<div class="col-md-6"><p class="input-group">'+
						'<input style="height:34px!important" type="text" class="form-control" datepicker-popup="{{format}}" ng-model="value" is-open="opened" datepicker-options="dateOptions" date-disabled="disabled(date, mode)" ng-required="true" close-text="Close" /><span class="input-group-btn"><button type="button" class="btn btn-default" ng-click="open($event)"><i class="fa fa-calendar"></i></button></span></p>'+
					'</div></div></div></div>'
				var modalInstance = $scope.invokeModalSimple(modalInstance,templateHtml,date,$scope.tlBrowser_expiryDate,undefined,$scope.TAG_NEXT_UPDATE);

				modalInstance.result.then(function(myValue) {
					$scope.myTl.schemeInformation.nextUpdateDate =myValue;
					postModifSchemeInfo($scope.TAG_NEXT_UPDATE)
				});
			};

			/**
			 * Sequence Number
			 */
			$scope.editSequenceNumber = function(){
				var value = $scope.myTl.schemeInformation.sequenceNumber;
				var templateHtml = templateGeneric +'<input class="col-sm-9" ng-model="value" style="margin-left:5px;"'+
				    ' onkeypress="return ((event.charCode >= 48 && event.charCode <= 57) || event.charCode==32 || event.keyCode==46 || event.keyCode==8 || event.keyCode==37 || event.keyCode==39)"/>'+
				'</div></div>';
				// Script to Sequence Number validation (catch keypress event)
				var modalInstance = $scope.invokeModalSimple(modalInstance,templateHtml,value,$scope.tlBrowser_sequenceNumber,undefined,$scope.TAG_SEQUENCE_NUMBER);

				modalInstance.result.then(function(myValue) {
					$scope.myTl.schemeInformation.sequenceNumber =myValue;
					postModifSchemeInfo($scope.TAG_SEQUENCE_NUMBER, function () {
						for(var i=0;i<$scope.drafts.length;i++){
							if($scope.drafts[i].id==$scope.tlIdDb){
								$scope.drafts[i].sequenceNumber= myValue; // Change
																			// the
																			// drafts
																			// sequence
																			// number
																			// value

							};
						};
				    });
				});
			};

			/**
			 * Tsl Type
			 */
			$scope.editTslType = function(){
				var value = $scope.myTl.schemeInformation.type;
				var templateHtml = templateGeneric +selectGeneric+'</div></div></div>'

				var modalInstance = $scope.invokeModalSimple(modalInstance,templateHtml,value,$scope.schemeInfo_type,schemeInfoTSLType,$scope.TAG_TSL_TYPE);

				modalInstance.result.then(function(myValue) {
					$scope.myTl.schemeInformation.type =myValue;
					postModifSchemeInfo($scope.TAG_TSL_TYPE);
				});
			}

			/**
			 * Status Determination
			 */
			$scope.editStatusDetermination = function(){
				var value = $scope.myTl.schemeInformation.statusDetermination;
				var templateHtml = templateGeneric +selectGeneric+'</div></div>'

				var modalInstance = $scope.invokeModalSimple(modalInstance,templateHtml,value,$scope.schemeInfo_statusDetermination, schemeInfoStatusDetermination,$scope.TAG_STATUS_DETERMINATION);

				modalInstance.result.then(function(myValue) {
					$scope.myTl.schemeInformation.statusDetermination =myValue;
					postModifSchemeInfo($scope.TAG_STATUS_DETERMINATION);
				});
			}

			/**
			 * Postal Address
			 */
			$scope.editPostalAddress = function (){
				if(!$scope.myTl.schemeInformation.schemeOpePostal){
					$scope.myTl.schemeInformation.schemeOpePostal = [];
				}

				var value = $scope.myTl.schemeInformation.schemeOpePostal;
				var modalInstance = $scope.invokeModalTabs(modalInstance,value,$scope.tlBrowser_postalAddress,"modalPostalAddressEdit",
						modalTabEditController,$scope.TAG_POSTAL_ADDRESSES,indexActive($scope.postalAddressActivity), false);

				modalInstance.result.then(function(tabs) {
					$scope.myTl.schemeInformation.schemeOpePostal=tabs;
					postModifSchemeInfo($scope.TAG_POSTAL_ADDRESSES);
				});
			};

			/**
			 * Distribution List
			 */
			$scope.editDistributionList= function (){
				if(!$scope.myTl.schemeInformation.distributionPoint){
					$scope.myTl.schemeInformation.distributionPoint = [];
				}
				var value = $scope.myTl.schemeInformation.distributionPoint;
				var modalInstance = $scope.invokeModalTabs(modalInstance,value,$scope.schemeInfo_distributionList,"modalElectronicAddressEdit",
						modalElectronicAddressEditController,$scope.TAG_DISTRIBUTION_LIST,0, false);

				modalInstance.result.then(function(tabs) {
					$scope.myTl.schemeInformation.distributionPoint=tabs;
					postModifSchemeInfo($scope.TAG_DISTRIBUTION_LIST);
				});
			};

			/**
			 * Information URI
			 */
			$scope.editInformationURI = function(){
				if(!$scope.myTl.schemeInformation.schemeInfoUri){
					$scope.myTl.schemeInformation.schemeInfoUri = [];
				}
				var value=$scope.myTl.schemeInformation.schemeInfoUri;
				var modalInstance = $scope.invokeModalTabs(modalInstance,value,$scope.tlBrowser_informationURI,"modalTabEdit",
						modalTabEditController,$scope.TAG_SCHEME_INFORMATION_URI,indexActive($scope.informationURIActivity), true);

				modalInstance.result.then(function(tabs) {
					$scope.myTl.schemeInformation.schemeInfoUri=tabs;
					postModifSchemeInfo($scope.TAG_SCHEME_INFORMATION_URI);
				});
			}

			/**
			 * Electronic Address
			 */
			$scope.editElectronicAddress = function(){
				if(!$scope.myTl.schemeInformation.schemeOpeElectronic){
					$scope.myTl.schemeInformation.schemeOpeElectronic = [];
				};
				var value = $scope.myTl.schemeInformation.schemeOpeElectronic;
				var modalInstance = $scope.invokeModalTabs(modalInstance,value,$scope.tlBrowser_electronicAddress,"modalElectronicAddressEdit",
						modalElectronicAddressEditController,$scope.TAG_ELECTRONIC_ADDRESS,indexActive($scope.electronicAddressActivity), false);

				modalInstance.result.then(function(tabs) {
					$scope.myTl.schemeInformation.schemeOpeElectronic=tabs;
					postModifSchemeInfo($scope.TAG_ELECTRONIC_ADDRESS);
				});
			};

			/**
			 * Community Rule
			 */
			$scope.editCommunityRule = function(){
				if(!$scope.myTl.schemeInformation.schemeTypeCommRule){
					$scope.myTl.schemeInformation.schemeTypeCommRule = [];
				}
				var value = $scope.myTl.schemeInformation.schemeTypeCommRule;
				$scope.myTl.listProperties = schemeInfoCommunityRule;
				var modalInstance = $scope.invokeModalTabs(modalInstance,value,$scope.schemeInfo_communityRule,"modalCommunityRuleEdit",
						modalTabEditController,$scope.TAG_SCHEME_TYPE_COMMUNITY_RULES,indexActive($scope.communityRuleActivity), false);

				modalInstance.result.then(function(tabs) {
					$scope.myTl.schemeInformation.schemeTypeCommRule=tabs;
					postModifSchemeInfo($scope.TAG_SCHEME_TYPE_COMMUNITY_RULES);
				});
			};

			/**
			 * Scheme Operator Name
			 */
			$scope.editSchemeOpeName = function(){
				if(!$scope.myTl.schemeInformation.schemeOpeName){
					$scope.myTl.schemeInformation.schemeOpeName = [];
				}
				var value = $scope.myTl.schemeInformation.schemeOpeName;
				var modalInstance = $scope.invokeModalTabs(modalInstance,value,$scope.schemeOpeName,"modalTabEdit",
						modalTabEditController,$scope.TAG_SCHEME_OPERATOR_NAME,indexActive($scope.operatorNameActivity), false);

				modalInstance.result.then(function(tabs) {
					$scope.myTl.schemeInformation.schemeOpeName=tabs;
					postModifSchemeInfo($scope.TAG_SCHEME_OPERATOR_NAME);
				});
			}

			/**
			 * Scheme Name
			 */
			$scope.editSchemeName = function(){
				if(!$scope.myTl.schemeInformation.schemeName){
					$scope.myTl.schemeInformation.schemeName = [];
				}
				var value = $scope.myTl.schemeInformation.schemeName;
				var modalInstance = $scope.invokeModalTabs(modalInstance,value,$scope.schemeInfo_schemeName,"modalTabEdit",
						modalTabEditController,$scope.TAG_SCHEME_NAME,indexActive($scope.schemeNameActivity), false);

				modalInstance.result.then(function(tabs) {
					$scope.myTl.schemeInformation.schemeName=tabs;
					postModifSchemeInfo($scope.TAG_SCHEME_NAME);
				});
			}

			/**
			 * Legal Notice
			 */
			$scope.editLegalNotice = function(){
				if(!$scope.myTl.schemeInformation.schemePolicy){
					$scope.myTl.schemeInformation.schemePolicy = [];
				}

				var value = $scope.myTl.schemeInformation.schemePolicy;
				var modalInstance = $scope.invokeModalTabs(modalInstance,value,$scope.schemeInfo_legalNotice,"modalTabEdit",
						modalTabEditController,$scope.TAG_POLICY_OR_LEGAL_NOTICE,indexActive($scope.legalNoticeActivity), false);

				modalInstance.result.then(function(tabs) {
					$scope.myTl.schemeInformation.schemePolicy=tabs;
					postModifSchemeInfo($scope.TAG_POLICY_OR_LEGAL_NOTICE);
				});
			}


			/**
			 * Modal : Scheme information edit Tabset value Parameter: -
			 * modalInstance - myTabs : tl object to edit - label : modal head
			 * title - templateUrl / controller : param to call modal - tag :
			 * used tag parent for change/checks - index: index of active tab in
			 * tabset - sortable : enable/disable manual sort in modal
			 */
			$scope.invokeModalTabs = function (modalInstance,myTabs,label,templateUrl,controller,tag,index,sortable){
				var tlInfo = $scope.myTl;
				tlInfo.label=label;
				tlInfo.tag = tag;
				tlInfo.listdiff= $scope.listDiffSchemeInfo;
				tlInfo.listChecks= $scope.listChecks;
				tlInfo.index = index;
				tlInfo.checkToRun = $scope.myTl.checkToRun;
				$scope.copyTabs = angular.copy(myTabs);
				modalInstance = $modal.open({
					templateUrl : "tl/"+templateUrl,
					controller : controller,
					windowClass: 'modal-fit',
					backdrop : 'static',
					resolve : {
						tabs : function() {
							return $scope.copyTabs;
						},
						myTLInfo : function() {
							return tlInfo;
						},
                        sortable : function() {
                            return sortable;
                        }
					}
				});

				return modalInstance;
			};

			/*----------------------------- Pointer -----------------------------*/

			/**
			 * Create new empty Pointer
			 */
			$scope.addPtot = function(){
				var pointer = {};
				pointer.head = "New Pointer";
				pointer.tlId = $scope.myTl.id;
				pointer.schemeOpeName = [];
				pointer.schemeTypeCommunity = [];
				pointer.serviceDigitalId = [];
				pointer.serviceDigitalId.digitalIdentification = [];
				pointer.countryName = $scope.myTl.dbCountryName;

				var modalInstance = invokeModalPtot(pointer);

				modalInstance.result.then(function(pointer) {
					postModifTlPointer(pointer,null,$scope.browserController_errorAddNewPointer);
				});
			};

			/**
			 * Edit Pointer
			 */
			$scope.editPtots = function(index) {
			    pointer = $scope.myTl.pointers[index];
			    if(($scope.myTl.schemeInformation.territory=="EU") && ($scope.notifiedPointer.length>0) ){
			        var isNotified = false;
			        for(var i=0;i<$scope.notifiedPointer.length;i++){
			            if(pointer.schemeTerritory==$scope.notifiedPointer[i]){
			                isNotified = true;
			            };
			        };

			        if(isNotified){
			            // Notified Pointer
			            showModal.confirmation($scope.browserController_editNotfied, appConstant.modalTitle.confirmation).then(function() {
			                initEditPtots(pointer,index);
                            deferred.resolve();
                        }, function(){
                            return;
                        });
			        }else{
			            initEditPtots(pointer,index);
			        };
			    }else{
			        // not notified pointer
			        initEditPtots(pointer,index);
			    };
			};

			var initEditPtots = function(pointer,index){
                pointer.tlId = $scope.myTl.id;
                pointer.open = '1';
				pointer.countryName = $scope.myTl.dbCountryName;
                $scope.originPointer = angular.copy(pointer);

                if(pointer.tlLocation!= undefined && pointer.tlLocation!=null){
                    pointer.head = pointer.schemeTerritory+" ("+pointer.mimeType+") "+$filter('cut')(pointer.tlLocation,true, 110,'...');
                }else{
                    pointer.head = $scope.browserController_editPointer;
                };

                var modalInstance = invokeModalPtot(pointer);

                modalInstance.result.then(function(tPtot) {
                    postModifTlPointer(tPtot,index);
                }, function() {
                    angular.copy($scope.originPointer, pointer);
                    pointer.open = '1';
                    pointer.communityRule ='0';
                    pointer.schemeOpshow='0';
                });
			}

			/**
			 * Delete Pointer
			 */
			$scope.deletePtot = function(pointer){
				var tlPointerEdition = {
						tlId : $scope.tlIdDb ,
						tlPointerObj : pointer,
						editAttribute : "",
						tlTerritoryCode : $scope.myTl.schemeInformation.territory,
						lastEditedDate : $scope.myTl.lastEdited,
						cookie : $scope.draftStoreId,
						checkToRun : $scope.myTl.checkToRun
				};
				$scope.myTl.pointers.splice($scope.myTl.pointers.indexOf(pointer),1);
				genericDeleteObject("Pointer","delPointer",tlPointerEdition,"pointers");
			};

			/**
			 * Invoke modal Pointer
			 */
			var invokeModalPtot = function(pointer){
			    pointer.checkToRun = $scope.myTl.checkToRun;
				var modalInstance = $modal.open({
					templateUrl : 'tl/ptot',
					controller : modalPtotController,
					windowClass: 'modal-fit',
					backdrop : 'static',
					resolve : {
					    territory : function(){
					        return $scope.myTl.schemeInformation.territory;
					    },
						ptot : function() {
							return pointer;
						},
						listChange : function(){
							return $scope.listDiffPointer;
						},
						properties : function(){
							return $scope.properties;
						},
						listChecks : function(){
							return $scope.listChecks;
						}
					}
				});
				return modalInstance;
			}

			/**
			 * Set the TL pointer in edition mode
			 */
			$scope.editPointerMode = function(){
				if($scope.ptotsEdit){
					$scope.ptotsEdit=false;
				}else{
					$scope.ptotsEdit=true;
				};
				$scope.ptotsMenuShow='1';
			};

			/*---------------------- Service Provider ----------------------*/

			/**
			 * Create new empty serviceProvider
			 */
			$scope.addProvider = function(){
				var provider = {};
				provider.head="New Trust Service Provider";
				provider.tlId = $scope.myTl.id;
				provider.tspservices = [];
				provider.tsptradeName= [];
				provider.tspname= [];
				provider.tspelectronic= [];
				provider.tspinfoUri= [];
				provider.tsppostal = [];
				provider.tspextension = [];

				var modalInstance = invokeModalmodalServiceProvider(provider);

				modalInstance.result.then(function(provider) {
					postModifTlServiceProvider(provider,null,$scope.browserController_errorAddNewProvider);
					$scope.providerEdit=false;
				});
			};

			$scope.editServiceProvider = function(indexProvider) {
				var serviceProvider = $scope.myTl.serviceProviders[indexProvider];
				serviceProvider.open = '1';
				serviceProvider.tlId = $scope.myTl.id;
				if(serviceProvider.tspservices==undefined || serviceProvider.tspservices==null){
					serviceProvider.tspservices = []
				};
				if(serviceProvider.tsppostal==undefined || serviceProvider.tsppostal==null){
					serviceProvider.tsppostal = [];
				};
				if(serviceProvider.tspname==undefined || serviceProvider.tspname==null){
					serviceProvider.tspname = [];
				};
				if(serviceProvider.tsptradeName==undefined || serviceProvider.tsptradeName==null){
					serviceProvider.tsptradeName = [];
				};
				if(serviceProvider.tspelectronic==undefined || serviceProvider.tspelectronic==null){
					serviceProvider.tspelectronic = [];
				};
				if(serviceProvider.tspinfoUri==undefined || serviceProvider.tspinfoUri==null){
					serviceProvider.tspinfoUri = [];
				};
				if(serviceProvider.tspextension==undefined || serviceProvider.tspextension==null){
					serviceProvider.tspextension = [];
				};

				if(serviceProvider.tspname[0]!=undefined && serviceProvider.tspname[0].value!=""){
					serviceProvider.head = serviceProvider.tspname[0].value;
				}else{
					serviceProvider.head=$scope.browserController_editProvider;
				};

				var modalInstance = invokeModalmodalServiceProvider(serviceProvider);

				modalInstance.result.then(function(sProvider) {
					$scope.myTl.serviceProviders[indexProvider] = sProvider;
					postModifTlServiceProvider($scope.myTl.serviceProviders[indexProvider],indexProvider,$scope.browserController_errorEditProvider);
				});
			};

			/**
			 * Delete Provider
			 */
			$scope.deleteProvider = function(serviceProvider,index){
				var tlServiceProviderEdition = {
						tlId : $scope.tlIdDb ,
						tlServiceProviderObj : serviceProvider,
						editAttribute : "",
						tlTerritoryCode : $scope.myTl.schemeInformation.territory,
						lastEditedDate : $scope.myTl.lastEdited,
						cookie : $scope.draftStoreId,
						checkToRun : $scope.myTl.checkToRun
				};
				$scope.myTl.serviceProviders.splice($scope.myTl.serviceProviders.indexOf(serviceProvider),1);
				genericDeleteObject("Service Provider","delServiceProvider",tlServiceProviderEdition,"serviceProvider",index);
			};

			/**
			 * Invoke modal Service Provider
			 */
			var invokeModalmodalServiceProvider = function(serviceProvider){
				if(serviceProvider.tspExtension==undefined || serviceProvider.tspExtension==null){
					serviceProvider.tspExtension = [];
				};
				serviceProvider.checkToRun = $scope.myTl.checkToRun;
				var modalInstance = $modal.open({
					templateUrl : 'tl/modalServiceProvider',
					controller : modalEditServiceProviderController,
					windowClass: 'modal-fit',
					backdrop : 'static',
					resolve : {
						serviceProvider : function() {
							return serviceProvider;
						},
						listChecks : function(){
							return $scope.listChecks;
						},
						listDiff : function(){
							return $scope.listDiffProviders;
						},
						properties : function(){
							return $scope.properties;
						}
					}
				});
				return modalInstance;
			};

			/**
			 * Set the TL Providers in edition mode
			 */
			$scope.editProviderMode = function(){
				if($scope.providerEdit){
					$scope.providerEdit=false;
				}else{
					$scope.providerEdit=true;
				};
				$scope.providerMenuShow='1';
			};

			/*---------------------- Service ----------------------*/

			/**
			 * Create new empty service
			 */
			$scope.addService = function(indexProvider){
				var parentIndex = [indexProvider,null]
				var provider = $scope.myTl.serviceProviders[indexProvider];
				if(provider.tspservices==undefined || provider.tspservices==null){
					provider.tspservices = [];
				};
				var service = {};
				service.head="New Trust Service";
				service.tlId = $scope.myTl.id;
				service.serviceName= [];
				service.tspdefinitionUri= [];
				service.typeIdentifier= "";
				service.currentStatus= "";
				service.currentStatusStartingDate= null;
				service.schemeDefinitionUri = [];
				service.supplyPoint = [];
				service.extension = [];
				service.history = [];
				service.digitalIdentification = [];

				var modalInstance = invokeModalService(service);

				modalInstance.result.then(function(service) {
					postModifTlService(service,parentIndex,$scope.browserController_errorAddService);
				});
			};

			$scope.editService = function(parentIndex) {
				var service = $scope.myTl.serviceProviders[parentIndex[0]].tspservices[parentIndex[1]];
				service.show = '1';
				service.tlId = $scope.myTl.id;

				if(service.serviceName==undefined || service.serviceName==null){
					service.serviceName = []
				};
				if(service.schemeDefinitionUri==undefined || service.schemeDefinitionUri==null){
					service.schemeDefinitionUri = [];
				};
				if(service.supplyPoint==undefined || service.supplyPoint==null){
					service.supplyPoint = [];
				};
				if(service.extension==undefined || service.extension==null){
					service.extension = [];
				};
				if(service.history==undefined || service.history==null){
					service.history = [];
				};
				if(service.digitalIdentification==undefined || service.digitalIdentification==null){
					service.digitalIdentification = [];
				};
				if(service.tspdefinitionUri==undefined || service.tspdefinitionUri==null){
					service.tspdefinitionUri = [];
				};

				if(service.serviceName[0]!=undefined && service.serviceName[0].value!=""){
					service.head = service.serviceName[0].value;
				}else{
					service.head="Edit Trust Service";
				};

				var modalInstance = invokeModalService(service);

				modalInstance.result.then(function(service) {
					$scope.myTl.serviceProviders[parentIndex[0]].tspservices[parentIndex[1]] = service;
					postModifTlService($scope.myTl.serviceProviders[parentIndex[0]].tspservices[parentIndex[1]],parentIndex,$scope.browserController_errorEditService);
				});
			};

			$scope.deleteService = function(service,index){
				var tlServiceEdition = {
						tlId : $scope.tlIdDb ,
						tlServiceObj : service,
						tlTerritoryCode : $scope.myTl.schemeInformation.territory,
						providerServiceSize : 0,
						lastEditedDate : $scope.myTl.lastEdited,
						cookie : $scope.draftStoreId,
						checkToRun : $scope.myTl.checkToRun
				};
				$scope.myTl.serviceProviders[index[0]].tspservices.splice($scope.myTl.serviceProviders[index[0]].tspservices.indexOf(service),1);
				genericDeleteObject("Service","delService",tlServiceEdition,"serviceProvider",index);
			};

			var invokeModalService = function(service){
			    service.checkToRun = $scope.myTl.checkToRun;
				var modalInstance = $modal.open({
					templateUrl : 'tl/modalService',
					controller : modalEditServiceController,
					windowClass: 'modal-fit',
					backdrop : 'static',
					resolve : {
						service : function() {
							return service;
						},
						listChecks : function(){
							return $scope.listChecks;
						},
						listDiff : function(){
							return $scope.listDiffProviders;
						},
						properties : function(){
							return $scope.properties;
						}
					}
				});
				return modalInstance;
			};

			$scope.editServiceMode = function(serviceProvider){
				if(serviceProvider.edit==false){
					serviceProvider.edit=true;
				}else{
					serviceProvider.edit=false;
				};
				serviceProvider.show = '1';
			};

			$scope.serviceNode = function(service){
				return serviceNode(service);
			};
			
			/**
			 * Update current service status & create new history entry
			 */
			$scope.newServiceStatus = function(tsp, service){
				showModal.confirmation($scope.browserController_confirmationNewStatus, appConstant.modalTitle.confirmation).then(function() {
					$scope.load = false;
					var tlNewStatus = {
							tlId : $scope.tlIdDb ,
							tspId : tsp.id,
							serviceId : service.id,
							cookie : $scope.draftStoreId,
							lastEditedDate : $scope.myTl.lastEdited,
							checkToRun : $scope.myTl.checkToRun
					};
					
					postModif("createNewStatus",tlNewStatus, $scope.browserController_errorNewStatus, function(){
						$scope.myTl = $scope.newValue;
						$scope.myTl.serviceProviders[tsp.index].open = '1';
						$scope.myTl.userEditable = true;
					}, tsp.index);
				});
			}


			/*---------------------- Service History ----------------------*/

			$scope.addHistory = function(index){
				if($scope.myTl.serviceProviders[index[0]].tspservices[index[1]].history==undefined || $scope.myTl.serviceProviders[index[0]].tspservices[index[1]].history==null){
					$scope.myTl.serviceProviders[index[0]].tspservices[index[1]].history = [];
				};
				var tmpService = initVar($scope.myTl.serviceProviders[index[0]].tspservices[index[1]],"object");
				var history = new Object();
				history.typeIdentifier = initVar(tmpService.typeIdentifier,"string");
				history.serviceName = initVar(tmpService.serviceName,"array");
				history.currentStatus = initVar(tmpService.currentStatus,"string");
				history.currentStatusStartingDate = initVar(tmpService.currentStatusStartingDate,"date");
				history.extension = initVar(tmpService.extension,"array");

				history.digitalIdentification = [];
				history.digitalIdentification.id = "SERVICE_DIGITAL_IDENTITY";
				if(tmpService.digitalIdentification!=null && tmpService.digitalIdentification[0].certificateList!=null){
					var tmpDigit = new Object;
					var cert = tmpService.digitalIdentification[0].certificateList[0];
					tmpDigit.subjectName = cert.certSubject;
					tmpDigit.x509ski = cert.certSkiB64;
					tmpDigit.id = "SERVICE_DIGITAL_IDENTITY";
					history.digitalIdentification.push(tmpDigit);
				};
				history.head="New Service History";
				var modalInstance = invokeModalHistory(history);

				modalInstance.result.then(function(history) {
					postModifTlHistory(history,index,$scope.browserController_errorAddHistory);
				});
			};

			$scope.editHistory = function(index,history) {
				history.open = '1';
				history.tlId = $scope.myTl.id;

				if(history.serviceName[0]!=undefined && history.serviceName[0].value!=""){
					history.head = history.serviceName[0].value;
				}else{
					history.head="Edit Service History";
				};
				var modalInstance = invokeModalHistory(history);

				modalInstance.result.then(function(history) {
					postModifTlHistory(history,index,$scope.browserController_errorEditHistory);
				});
			};

			$scope.deleteHistory = function(history,index){
				var tlServiceHistory = {
						tlId : $scope.tlIdDb ,
						tlHistoryObj : history,
						tlTerritoryCode : $scope.myTl.schemeInformation.territory,
						lastEditedDate : $scope.myTl.lastEdited,
						cookie : $scope.draftStoreId,
						checkToRun : $scope.myTl.checkToRun
				};
				$scope.myTl.serviceProviders[index[0]].tspservices[index[1]].history.splice($scope.myTl.serviceProviders[index[0]].tspservices[index[1]].history.indexOf(history),1);
				genericDeleteObject("Service History","delHistory",tlServiceHistory,"serviceProvider",index);
			};

			var invokeModalHistory = function(history){
			    history.checkToRun = $scope.myTl.checkToRun;
				var modalInstance = $modal.open({
					templateUrl : 'tl/modalHistory',
					controller : modalEditHistoryController,
					windowClass: 'modal-fit',
					backdrop : 'static',
					resolve : {
						history : function() {
							return history;
						},
						listChecks : function(){
							return $scope.listChecks;
						},
						listDiff : function(){
							return $scope.listDiffProviders;
						},
						properties : function(){
							return $scope.properties;
						}
					}
				});
				return modalInstance;
			};

			$scope.editHistoryMode = function(service){
				if(service.editHistory==false){
					service.editHistory=true;
				}else{
					service.editHistory=false;
				};
				service.showHistory.open = '1';
			};

			$scope.historyNode = function(history){
				if(history!=null){
					var span = "";
					var emptySpan = "<span class='emptySpan'></span>"
					if(history.currentStatusStartingDate!=undefined && history.currentStatusStartingDate!=null){
						var startingDate = $filter('date')(history.currentStatusStartingDate, "yyyy-MM-dd");
						span = span +"<span class='boldNode'>"+startingDate+"</span>"+emptySpan;
					};
					if(history.typeIdentifier!=undefined && history.typeIdentifier!=null){
						typeIdentifier = "";
						if(history.typeIdentifier.indexOf("TrstSvd")!=-1){
							var typeIdentifier = history.typeIdentifier.split("https://uri.etsi.org/TrstSvd/Svctype/")[1];
						}else if(history.typeIdentifier.indexOf("TrstSvc")!=-1){
							var typeIdentifier = history.typeIdentifier.split("https://uri.etsi.org/TrstSvc/Svctype/")[1];
						};
						span = span +typeIdentifier+emptySpan;
					};
					if(history.currentStatus!=undefined && history.currentStatus!=null){
						var currentStatus = history.currentStatus.split("https://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/")[1];
						span = span +currentStatus+emptySpan;
					};
					if(span!=""){
						span = " : "+span;
					};
					return span;
				};
			};

			/*--------------------------------- DELETE GENERIC ---------------------------------*/
			/**
			 * Delete Generic Pointer/ServiceProvider/Service/History index :
			 * index to re-initialize current tab open
			 * [1:providerIndex,2:serviceIndex,3:historyIndex]
			 */
			var genericDeleteObject = function(labelMessage,labelApi,deleteObject,labelChangeApi,index){
			    var tlCookie = {
		            tlId : $scope.myTl.tlId,
                    cookie : $scope.draftStoreId
                };

				$scope.load=false;
				$scope.loadingStatus = labelMessage+" deletion in progress.";
				$scope.cssMain = "container disabled-click";
				$scope.backUpTL = angular.copy($scope.myTl);
				httpFactory.put('/api/tl/edit/'+labelApi,deleteObject,labelMessage+" "+$scope.browserController_deletionFailure).then(function(data) {
				    // Update TL Object with new Id
				    if(labelChangeApi=="pointers"){
                        $scope.myTl.pointers = data;
                    }else{
                        $scope.myTl.serviceProviders = data;
                    };
					 $scope.loadingStatus = $scope.browserController_reloadingChanges;
					// Differences
					trustedListFactory.getDifference(tlCookie,labelChangeApi, $scope.myTl.checkToRun).then(function(diff) {
						if(labelChangeApi=="pointers"){
							$scope.listDiffPointer = diff;
						}else{
							$scope.listDiffProviders = diff;
							// Open tab
							if(index[0]!=null){
								$scope.myTl.serviceProviders[index[0]].open='1';
								if(index[1]!=null){
									$scope.myTl.serviceProviders[index[0]].tspservices[index[1]].show='1';
								};
							};
						};
					});
					$scope.loadingStatus = $scope.browserController_reloadingChecks;
					// Checks
					trustedListFactory.getChecks(tlCookie, $scope.myTl.checkToRun).then(function(checks) {
						$scope.listChecks = checks;
					});
					// Signature
					trustedListFactory.getSignatureInfo(tlCookie).then(function(signature) {
						$scope.signatureInfo = signature;
					});
					// Get & Update last edit date
                    trustedListFactory.getLastEdit($scope.myTl.tlId).then(function(lastEdited) {
                        $scope.myTl.lastEdited = lastEdited;
                    });
					unlockView();
				}, function(response) {
					checkConflict(response);
				 });
			};

			/*--------------------------------- CONFLICT MANAGEMENT ---------------------------------*/

			var checkConflict = function(response){
				if(response==-409){
					var message = "<div>"+$scope.browserController_errorConcurrence1+"</div>"+
						"<div>"+$scope.browserController_errorConcurrence2.replace("%NAME%", $scope.myTl.dbName)+"</div>"+
						"<div>"+$scope.browserController_errorConcurrence3.replace("%NAME%", $scope.myTl.dbName)+"</div>";

					showModal.applicationError(message).then(function(){
					    trustedListFactory.conflictTl($scope.myTl).then(function (tlTmp){
                            $window.location.href = '/tl-manager/tl/'+tlTmp.id
                        });
					});
				}else{
					$scope.myTl = $scope.backUpTL;
					unlockView();
				};
			}

			/*--------------------------------- Changes prod ---------------------------------*/

			$scope.changesProd = function(countryCode){
			    $scope.cssMain = "container disabled-click"
		        $scope.load=false;
                $scope.loadingStatus = $scope.browserController_loadingArchivedTrustedList;

			    trustedListFactory.archivedProds($scope.myTl.schemeInformation.territory).then(function(archivedProds){
			        unlockView();
    			    var modalInstance = $modal.open({
                        templateUrl: "modalChangesProd",
                        backdrop : 'static',
                        size : 'lg',
                        controller: modalChangesProdController,
                        resolve : {
                            myTl: function() {
                                return $scope.myTl;
                            },
                            archivedProds : function(){
                                return archivedProds;
                            }
                        }
                    });

                    modalInstance.result.then(function(tlChangesProd) {
                        $scope.tlChangesProd = tlChangesProd;

                        var currentTL = "[ "+$scope.myTl.dbName+" ("+$scope.myTl.schemeInformation.sequenceNumber+") ]";
                        var comparedTL = "[ "+tlChangesProd.name+" ("+tlChangesProd.sequenceNumber+") ]";

                        $scope.information = {
                                type : "PROD",
                                currentTL : currentTL,
                                comparedTL : comparedTL
                        };
                        getProdArchived(tlChangesProd.id);
                    });
			    }, function(){
			        unlockView();
			    });
			};

			var getProdArchived = function(prodChangeId){
		        $scope.load=false;
		        $scope.loadingStatus = $scope.browserController_loadingChanges
	            trustedListFactory.getChangesArchivedProd($scope.myTl.tlId, prodChangeId).then(function(list){
	                if(list!=null){
                        $scope.listDiffSchemeInfo = list[0];
                        $scope.listDiffPointer = list[1];
                        $scope.listDiffProviders = list[2];
                        $scope.listDiffSignature = list[3];
                    };
                    $scope.load=true;
                },function(){
                    $scope.load=true;
                });
			};

			/*--------------------------------- DOM MANAGEMENT ---------------------------------*/

			/**
			 * Show/hide tabset Hide if table is empty in prod
			 */
			$scope.tableVisibility = function(table){
				if($scope.myTl.dbStatus=="PROD"){
					if($scope.iconTableVisibility(table)){
						return true;
					};
				};
				return false;
			};

			/**
			 * Show/hide tabset open-close icon Hide if table is empty
			 */
			$scope.iconTableVisibility = function(table){
				if(table==undefined || table==null || table.length==0){
					return true;
				}else{
					return false;
				};
			};

			/**
			 * Tooltip Edit icon
			 */
			$scope.EditTooltip=function(label){
				if($scope.myTl.dbStatus=="PROD"){
				    return $scope.browserController_createDraft;
				};
				return $scope.browserController_edit+" "+ $filter('cut')(label,true, 50,'...');
			};

			$scope.pointerTooltip=function(pointer){
				if($scope.myTl.dbStatus=="PROD"){
					return $scope.browserController_createDraft;
				};
				if(pointer.mimeType==null){
				    pointer.mimeType="";
				}
				return $scope.browserController_edit+" "+pointer.schemeTerritory +" ("+pointer.mimeType+")";
			};

			/**
			 * Return active index of a tabset
			 */
			var indexActive = function(tab){
				if(tab!=undefined && tab!=null && tab.length>0){
					var index = tab.indexOf(true);
					if(index==-1){
						return 0;
					}else{
						return index;
					};
				};
				return 0;
			};

			$scope.initOpenTab = function(){
				if($scope.myTl.schemeInformation.territory=="EU"){
					$scope.ptotsMenuShow='1';
					if($scope.serviceProviderShow!='1'){
						$scope.serviceProviderShow='0';
					};
				}else{
					if($scope.ptotsMenuShow!='1'){
						$scope.ptotsMenuShow='0';
					};
					$scope.serviceProviderShow="1";
				};
			};

			$scope.schemeInfoMenuOpen = function(){
				$scope.schemeInformationMenuShow='1';
			};

			$scope.schemeInfoMenuClose = function(){
				$scope.schemeInformationMenuShow='0';
			};

			$scope.ptotsMenuOpen = function(){
				$scope.ptotsMenuShow='1';
			};

			$scope.ptotsMenuClose = function(){
				$scope.ptotsMenuShow='0';
			};

			$scope.serviceProviderOpen = function(){
				$scope.serviceProviderShow='1';
			};

			$scope.serviceProviderClose = function(){
				$scope.serviceProviderShow='0';
			};

			var unlockView = function(){
			    if($scope.myTl!=undefined){
			        $scope.myTl.isInit = true;
			    };
				$scope.loadingStatus = "";
				$scope.load=true;
				$scope.disclaimerLoading = false;
				$scope.cssMain = "container"
			};

			$scope.isOpen = function(obj){
				if($scope.edit==undefined && obj=="1"){
					return "isOpen";
				}else{
					return "";
				};
			};

			$scope.styleAvailable = function (status){
				return styleAvailableStatus(status);
			}

			// Return true if LOTL or undefined type
			$scope.isTl = function(){
				if($scope.myTl!=undefined){
					if($scope.myTl.schemeInformation.territory!="EU" || $scope.myTl.schemeInformation.territory==undefined || $scope.myTl.schemeInformation.territory==null){
						return true
					};
				};
				return false;
			};

			/**
			 * Switch checkToRun on/off Run check/change if switch on Disable &
			 * hide check/change if switch off
			 */
			$scope.switchCheckToRun = function(){
			    var tlCookie = {
                        tlId : $scope.myTl.id,
                        cookie : $scope.draftStoreId
                };
                trustedListFactory.switchCheckToRun(tlCookie).then(function(checkToRun) {
                    $scope.myTl.checkToRun = checkToRun;
                    $scope.loadingStatus = $scope.browserController_switchCheckToRun;
                    $scope.load = false;
                    if($scope.myTl.checkToRun){
                        $scope.loadingStatus = $scope.browserController_runAllChecks;
                        trustedListFactory.runAllRules(tlCookie).then(function(){
                            $window.location.reload();
                        },function(){
                            trustedListFactory.getLastEdit($scope.myTl.tlId).then(function(lastEdited) {
                                $scope.myTl.lastEdited = lastEdited;
                            });
                            $scope.load = true;
                        });
                    }else{
                        $window.location.reload();
                    };
                }, function(){
                    $scope.load = true;
                });
			};

			var draftEditionCheckVisible = function(){
			    trustedListFactory.draftEditionCheck().then(function (){
			        $scope.draftEditionCheckVisible = true;
			    });
			};
			draftEditionCheckVisible();

			$scope.exportCheckToRunError = function(){
			    showModal.information($scope.export_check_run);
			};

			/** Collapse/Expand Service * */

			$scope.expandAllService = function(provider){
			    var toExpand = null;
			    if(provider.expand!=undefined && provider.expand){
		            toExpand = 0;
			        provider.expand = false;
			    }else{
			        toExpand = 1;
                    provider.expand = true;
			    };
			    for(var i=0;i<provider.tspservices.length;i++){
			        provider.tspservices[i].show = toExpand;
			    };
			};

			$scope.iconExpandService = function(provider){
				if (provider.show=='1'){
					if(provider.expand!=undefined && provider.expand){
						provider.expandTooltip = $scope.serviceProvider_collapse;
						return "fa fa-compress";
					}else{
						provider.expandTooltip = $scope.serviceProvider_expand;
						return "fa fa-expand";
					};
				}else{
					return "display-none";
				};

			};
			
			/*--------------------------- Signature -------------------------*/
			
			$scope.filterNodes = function(){
				var key = angular.copy($scope.tspFilter.value).trim();
				$scope.splitFilter = "";
								
				var displayWarning = true;
				if(key){
					var splitKey = key.match(/([\w]|[\u00C0-\u00FF]|[,?:;.!&'\-/\\@*+=()]|[[U+0400–U+04FF]])+|"[^"]*"/g);
					for(var i=0;i<splitKey.length;i++){
						$scope.splitFilter = $scope.splitFilter + splitKey[i];
						if(i<(splitKey.length-1)){
							$scope.splitFilter = $scope.splitFilter + " - ";	
						}
						splitKey[i] = splitKey[i].replace(/"/g, '').toLowerCase();
					}
										
					for(var i=0;i<$scope.myTl.serviceProviders.length;i++){
						var tmpTSP = angular.copy($scope.myTl.serviceProviders[i]);
						tmpTSP = removeEmpty(tmpTSP);
						tmpTSP.tspservices=[];
						var servicesFound = false;
													
						if($scope.myTl.serviceProviders[i].tspservices!=null){
							for(var j=0;j<$scope.myTl.serviceProviders[i].tspservices.length;j++){
								tmpTSP.tspservices=[];
															
								// Clean service history & certificates
								// (base64/ASN1/CertInfo)
								var tmpService = angular.copy($scope.myTl.serviceProviders[i].tspservices[j]);
								tmpService.history=null;
								tmpService = removeEmpty(tmpService);
								if(tmpService.digitalIdentification!=null){
									for(var di=0;di<tmpService.digitalIdentification.length;di++){
										for(var ci=0;ci<tmpService.digitalIdentification[di].certificateList.length;ci++){
											tmpService.digitalIdentification[di].certificateList[ci].certB64 = null;
											tmpService.digitalIdentification[di].certificateList[ci].certEncoded = null;
											tmpService.digitalIdentification[di].certificateList[ci].certificateInfo = null;
											tmpService.digitalIdentification[di].certificateList[ci].asn1 = null;
										}
									}
								}
								tmpTSP.tspservices.push(tmpService);
															
								if(keyMatchJSON(splitKey, tmpTSP)){
									$scope.myTl.serviceProviders[i].tspservices[j].filter=false;
									servicesFound = true;
								}else{
									$scope.myTl.serviceProviders[i].tspservices[j].filter=true;
								}
							}
						}
												
						if(servicesFound){
							displayWarning=false;
							$scope.myTl.serviceProviders[i].filter=false;
						}else{
							$scope.myTl.serviceProviders[i].filter=true;
						}
						
					}
				}else{
					// Key empty => No filter
					for(var i=0;i<$scope.myTl.serviceProviders.length;i++){
						$scope.myTl.serviceProviders[i].filter=false;
						for(var j=0;j<$scope.myTl.serviceProviders[i].tspservices.length;j++){
							$scope.myTl.serviceProviders[i].tspservices[j].filter=false;
						}
					}
										
					// Update tmp warning value
					displayWarning = false;
				}
								
				if(displayWarning){
					$scope.noResultWarning = true;
				}else{
					$scope.noResultWarning = false;
				}
			}
			
			$scope.filterReset = function(){
				$scope.tspFilter = {value:''};
				$scope.filterNodes();
			}
			
			
			var keyMatchJSON = function(splitKey, obj){
				var tmpJson = JSON.stringify(obj).toLowerCase();
				for(var i=0;i<splitKey.length;i++){
					if(tmpJson.indexOf(splitKey[i])==-1){
						return false;
					}
				}
				return true;
			}

			/**
			 * Remove null/undefined JSON object
			 */
			const removeEmpty = function(obj) {
				Object.keys(obj).forEach(function(k) {
					(obj[k] && typeof obj[k] === 'object') && removeEmpty(obj[k]) ||
				    (!obj[k] && obj[k] !== undefined) && delete obj[k]
				});
				return obj;
			};
			
} ]);
