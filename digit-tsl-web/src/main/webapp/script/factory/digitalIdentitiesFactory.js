/**
 * DigitalIdService: manage browsing/edition of digital identity
 **/
digitTslWeb.factory('digitalIdService', [ '$http','$modal','$q','showModal','appConstant', function($http,$modal,$q,showModal,appConstant) {
	return {

		/**
		 * Add Certificate from local file
		 * return serviecDigitalId
		 */
		addCertificate : function(myFile) {
			var deferred = $q.defer();
			if(myFile!=null){
    			var file = myFile;
    			var fd = new FormData();
    			fd.append('file', file);
    			$http.post('/tl-manager/certificateInfo', fd, {
    				transformRequest : angular.identity,
    				headers : {
    					'Content-Type' : undefined
    				}
    			})
    			.success(function(data, status, headers, config) {
    				if(serviceResponseHandler(data)){
    					deferred.resolve(data.content);
    				}else{
    				    showModal.httpStatusHandler(status,appConstant.digitalIdentity.certificateFileInvalid);
    					deferred.reject(-1);
    				};
    			})
    			.error(function(data, status, headers, config) {
    			    showModal.httpStatusHandler(status,appConstant.digitalIdentity.certificateFileInvalid);
    				deferred.reject(-1);
    			});
			}else{
			    deferred.reject(-1);
			};
			return deferred.promise;
		},

		/**
		 * Add Certificate from B64
		 * return serviceDigitalId
		 */
		addDigitalB64 : function(label) {
			var deferred = $q.defer();

			//Call B64 modal pop-up
			var modalInstance = $modal.open({
				templateUrl: 'modalB64',
				backdrop : 'static',
				controller: modalB64Controller,
				resolve : {
					label : function() {
						return label;
					}
				}
			});

			modalInstance.result.then(function(b64) {
				//Send B64 value to get digitalIdentity
				$http.put('/tl-manager/certificateB64', b64)
				.success(function(data, status, headers, config) {
					if(serviceResponseHandler(data)){
						deferred.resolve(data.content);
					}else{
					    showModal.httpStatusHandler(status,appConstant.digitalIdentity.certificateFileInvalid);
					    deferred.reject(-1);
					};
				})
				.error(function(data, status, headers, config) {
				    showModal.httpStatusHandler(status,appConstant.digitalIdentity.certificateFileInvalid);
					deferred.reject(-1);
				})
			}, function () {
				deferred.reject(-1);
			});

			return deferred.promise;
		},

		/**
		 * Return DigitalIdentity
		 * Invoke modal Digital Identity for edition
		 * Parameter:
		 ** digitalType - boolean(true or undefined) : hide certificate management in history edition
		 */
		invokeModalDigitalIdentitie : function(digital,listchange,listcheck,digitalType){
			if(digitalType==undefined){
				digitalType="undefined"
			};
			var deferred = $q.defer();
			var modalInstance = $modal.open({
				templateUrl: 'modalDigitalIdentity',
				size : 'lg',
				backdrop : 'static',
				controller: modalDigitalIdentityController,
				resolve : {
					digital : function() {
						return digital;
					},
					listcheck : function() {
						return listcheck;
					},
					listchange : function() {
						return listchange;
					},
					digitalType : function(){
						return digitalType
					}
				}
			});

			modalInstance.result.then(function(digital) {
				deferred.resolve(digital);
			}, function () {
				deferred.reject(-1);
			});

			return deferred.promise;
		},

		/**
		 * Init new DigitalIdentity & Edit in a modal
		 */
		addDigitalIdentitie : function(listchange,listcheck,digitalType){
			var deferred = $q.defer();

			var digital = new Object();
			digital.certificateList = [];
			digital.isOther=false;

			this.invokeModalDigitalIdentitie(digital,listchange,listcheck,digitalType).then(function(digital) {
				deferred.resolve(digital);
			}, function(){
				deferred.reject(-1);
			});
			return deferred.promise;
		},

		/**
		 * Init digitalIdentity edition
		 */
		editDigitalIdentitie : function(digital,listchange,listcheck,digitalType,typeIdentifier){
			var deferred = $q.defer();
			//Init bool var to identify nothavingpki digital identity
			digital.isOther = false;
			if((digitalType=="service" || digitalType=="history") && typeIdentifier!=undefined){
			    if(typeIdentifier.indexOf("nothavingPKIid",1)!=-1){
			        digital.isOther=true;
			    };
			};
			digital.certificateList = initVar(digital.certificateList,"array");
			this.invokeModalDigitalIdentitie(digital,listchange,listcheck,digitalType).then(function(digital) {
				deferred.resolve(digital);
			}, function(){
				deferred.reject(-1);
			});
			return deferred.promise;
		},

		/**
		 * Init new other DigitalIdentity
		 */
		addDigitalOther : function(listchange,listcheck){
			var deferred = $q.defer();

			var digital = new Object();
			digital.other = [];
			digital.other[0] = " ";
			digital.isOther=true;

			this.invokeModalDigitalIdentitie(digital,listchange,listcheck,"history").then(function(digital) {
				deferred.resolve(digital);
			}, function(){
				deferred.reject(-1);
			});
			return deferred.promise;
		}

	};

} ]);