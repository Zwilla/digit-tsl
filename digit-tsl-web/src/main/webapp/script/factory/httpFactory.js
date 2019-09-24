/**
 * HttpFactory : manage http request (get/post/put) & handle error Response
 * status != 200 -> Error
 */
digitTslWeb.factory('httpFactory', [ '$http', 'showModal', '$q', 'appConstant', function($http, showModal, $q, appConstant) {
	return {

		// contextRoot : "/tl-manager",

		/**
		 * GET Generic
		 */
		get : function(url, errMessage) {
			var deferred = $q.defer();
			$http.get("/tl-manager" + url).success(function(data, status, headers, config) {
				if (serviceResponseHandler(data)) {
					deferred.resolve(data.content);
				} else {
					if (data.errorMessage != null && data.errorMessage != '') {
						showModal.httpStatusHandler(data.responseStatus, data.errorMessage);
					} else {
						showModal.httpStatusHandler(data.responseStatus, errMessage);
					}
					deferred.reject(-1);
				}
				;
			}).error(function(data, status, headers, config) {
				if (data.errorMessage) {
					showModal.httpStatusHandler(status, data.errorMessage);
				} else {
					showModal.httpStatusHandler(status, errMessage);
				}
				deferred.reject(-1);
			});
			return deferred.promise;
		},

		/**
		 * POST Generic
		 */
		post : function(url, obj, errMessage) {
			var deferred = $q.defer();
			$http.post("/tl-manager" + url, obj).success(function(data, status, headers, config) {
				if (serviceResponseHandler(data)) {
					deferred.resolve(data.content);
				} else {
					if (data.errorMessage) {
						showModal.httpStatusHandler(data.responseStatus, data.errorMessage);
					} else {
						showModal.httpStatusHandler(data.responseStatus, errMessage);
					}
					deferred.reject(-1);
				}
				;
			}).error(function(data, status, headers, config) {
				if (data.errorMessage) {
					showModal.httpStatusHandler(status, data.errorMessage);
				} else {
					showModal.httpStatusHandler(status, errMessage);
				}
				deferred.reject(-1);
			});
			return deferred.promise;
		},

		/**
		 * POST File Generic
		 */
		postFile : function(url, obj, errMessage) {
			var deferred = $q.defer();
			$http.post("/tl-manager" + url, obj, {
				transformRequest : angular.identity,
				headers : {
					'Content-Type' : undefined
				}
			}).success(function(data, status, headers, config) {
				if (serviceResponseHandler(data)) {
					deferred.resolve(data.content);
				} else {
					if (data.errorMessage) {
						showModal.httpStatusHandler(data.responseStatus, data.errorMessage);
					} else {
						showModal.httpStatusHandler(data.responseStatus, errMessage);
					}
					deferred.reject(-1);
				}
				;
			}).error(function(data, status, headers, config) {
				if (data.errorMessage) {
					showModal.httpStatusHandler(status, data.errorMessage);
				} else {
					showModal.httpStatusHandler(status, errMessage);
				}
				deferred.reject(-1);
			});
			return deferred.promise;
		},

		/**
		 * PUT Generic
		 */
		put : function(url, obj, errMessage) {
			var deferred = $q.defer();
			$http.put("/tl-manager" + url, obj).success(function(data, status, headers, config) {
				if (serviceResponseHandler(data)) {
					deferred.resolve(data.content);
				} else {
					if (data.responseStatus == 409) {
						deferred.reject(-409);
					} else {
						if (data.errorMessage) {
							showModal.httpStatusHandler(data.responseStatus, data.errorMessage);
						} else {
							showModal.httpStatusHandler(data.responseStatus, errMessage);
						}
						deferred.reject(-1);
					}
					;
				}
				;
			}).error(function(data, status, headers, config) {
				if (data.errorMessage) {
					showModal.httpStatusHandler(status, data.errorMessage);
				} else {
					showModal.httpStatusHandler(status, errMessage);
				}
				deferred.reject(-1);
			});
			return deferred.promise;
		},

		/**
		 * PUT without response (store file)
		 */
		store : function(url, obj, errMessage) {
			var deferred = $q.defer();
			$http.put("/tl-manager" + url, obj).success(function(data, status, headers, config) {
				if (status == 200) {
					deferred.resolve(data.content);
				} else {
					showModal.httpStatusHandler(data.responseStatus, errMessage);
					deferred.reject(-1);
				}
				;
			}).error(function(data, status, headers, config) {
				showModal.httpStatusHandler(status, errMessage);
				deferred.reject(-1);
			});
			return deferred.promise;
		},

	};

} ]);