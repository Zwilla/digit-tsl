/** Handle Modal information/error/confirmation in all the application **/
digitTslWeb.factory('showModal', [ '$modal','appConstant','$q', function($modal,appConstant,$q) {
	return {
		applicationError: function(message) {
			if(!message){
				message = appConstant.errorFactory.errorOccured;
			};
			var deferredModal = $q.defer();
			return this.callGenericModal(message,appConstant.modalTitle.applicationError,deferredModal,"");
	    },

	    signatureError : function(message){
	    	if(!message){
				message = appConstant.errorFactory.errorOccured;
			};
			var deferredModal = $q.defer();
            return this.callGenericModal(message,appConstant.modalTitle.signatureError,deferredModal,"");
	    },

	    draftStoreInformation : function(message){
			var deferredModal = $q.defer();
			return this.callGenericModal(message,appConstant.modalTitle.repository,deferredModal,"lg");
		},

		information : function(message){
		    var deferredModal = $q.defer();
		    return this.callGenericModal(message,appConstant.modalTitle.information,deferredModal,"");
		},

		//Handle bad request response
		httpStatusHandler : function(responseStatus, message){
            var deferredModal = $q.defer();
            if(responseStatus==0){
                return;
            }
            if(responseStatus==500){
                //Internal Error
                title=appConstant.httpStatus.titleApplicationError;
            }else if(responseStatus==400){
                //Bad Request
                title=appConstant.httpStatus.titleBadRequest;
            }else if(responseStatus==404){
                //Not found
                title=appConstant.httpStatus.titleNotFound;
                message = appConstant.httpStatus.tlNotFound;
            }else if(responseStatus==401){
                //Unauthorized
                title = appConstant.httpStatus.titleNotAuthorized;
                message = appConstant.httpStatus.notAuthorized;
            }else if(responseStatus==403){
                //Forbidden
                title = appConstant.httpStatus.titleForbidden;
            }else{
                //Uncaught
                title = appConstant.httpStatus.titleError;
            };
            this.callGenericModal(message,title,deferredModal,"");
        },

        /**
         * Generic call with specified title, message & size
         */
		callGenericModal : function(message,title,deferredModal,size){
		    var modalHtml =
		        '<div class="panel panel-primary" >' +
		            '<div class="panel-heading">' +
		                '<span>Application: '+title+'</span>'+
	                '</div>' +
	                '<div class="panel-body">'+
                        '<span style="word-break: break-word" ng-bind-html="message"></span>'+
                    '</div>'+footerModal1B;

            var modalInstance = $modal.open({
              template: modalHtml,
              backdrop : 'static',
              keyboard: false,
              size : size,
              controller: ModalInstanceCtrlMessage,
              resolve : {
                  message : function() {
                      return message;
                  }
              }
            });

            modalInstance.result.then(function() {
                deferredModal.resolve();
            });
            return deferredModal.promise;
		},

		confirmation : function(message,title){
            var deferredModal = $q.defer();
            var modalHtml =
                '<div class="panel panel-primary">'+
                '<div class="panel-heading">'+
                    '<span>Application: '+title+'</span>'+
                '</div>'+
                '<div class="panel-body">'+
                    '<div class="row">'+
                        '<div ng-bind-html="message"></div>'+
                    '</div>'+
                '</div>'+footerModal2B;

            var modalInstance = $modal.open({
                template: modalHtml,
                backdrop : 'static',
                keyboard: false,
                size : 'lg',
                controller: ModalInstanceCtrlMessage,
                resolve : {
                    message : function() {
                        return message;
                    }
                }
            });

            modalInstance.result.then(function() {
                deferredModal.resolve();
            }, function(){
                deferredModal.reject();
            });
            return deferredModal.promise;
        }
	};
} ]);