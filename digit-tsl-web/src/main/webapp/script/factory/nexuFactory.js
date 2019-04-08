/**
 * NexuFactory: manage NexU http request
 */
digitTslWeb.factory('nexuFactory', [
		'$http',
		'showModal',
		'$q',
		'appConstant',
		'httpFactory',
		function($http, showModal, $q, appConstant, httpFactory) {
			var runningPort;// = "9776";
			var targetURLScheme;// = "http";

			return {

				/**
				 * Get NexU information & run integration Show message error if
				 * NexU isn't running or version if not correct
				 */
				info : function() {
					var deferred = $q.defer();

					// Create callbacks
					successCallback = function(nexuParam) {
						runningPort = nexuParam.getRunningPort();
						targetURLScheme = nexuParam.getTargetURLScheme();
						deferred.resolve();
					};
					oldVersionCallback = function() {
						httpFactory.get("/api/signature/nexU/file_url", appConstant.nexuFactory.getUrlError).then(function(url) {
							showModal.signatureError(appConstant.nexuFactory.oldVersionError.replace("%NEXU_URL%", url));
						});
						deferred.reject(-1);
					};
					notInstalledCallback = function(n) {
						httpFactory.get("/api/signature/nexU/file_url", appConstant.nexuFactory.getUrlError).then(
								function(url) {
									httpFactory.get("/api/signature/nexU/signing_tool", appConstant.nexuFactory.getUrlError).then(
											function(signingToolUrl) {
												showModal.signatureError(appConstant.nexuFactory.notInstalledError.replace("%NEXU_URL%", url)
														.replace("%SIGNING_TOOL_URL%", signingToolUrl));
											});
								});
						deferred.reject(-1);
					};
					cannotLoadScriptCallback = function() {
						httpFactory.get("/api/signature/nexU/file_url", appConstant.nexuFactory.getUrlError).then(function(url) {
							showModal.signatureError(appConstant.nexuFactory.infoError.replace("%NEXU_URL%", url));
						});
						deferred.reject(-1);
					};

					// Run the deployment script
					var nexuIntegration = new NexU(successCallback, oldVersionCallback, notInstalledCallback, cannotLoadScriptCallback);
					return deferred.promise;
				},

				/**
				 * Get SmardCard certificates
				 */
				certificates : function(id) {
					var deferred = $q.defer();
					$http.post(targetURLScheme + "://localhost:" + runningPort + "/rest/certificates", null).success(
							function(data, status, headers, config) {
								if (data.success === false) {
									if (data.error === appConstant.nexuFactory.noProductFound) {
										showModal.signatureError(appConstant.nexuFactory.noProductFoundMessage);
									} else {
										showModal.signatureError(appConstant.nexuFactory.certificateError);
									}
									deferred.reject(-1);
								} else {
									var cert = data.response;
									cert.tokenId = {
										"id" : cert.tokenId.id
									};
									var getTbsObj = {
										tlId : id,
										certificateResponse : cert
									};
									deferred.resolve(getTbsObj);
								}
								;
							}).error(function(data, status, headers, config) {
						if (data.error === appConstant.nexuFactory.noProductFound) {
							showModal.signatureError(appConstant.nexuFactory.noProductFoundMessage);
						} else if (data.error === appConstant.nexuFactory.userCancelled) {
							showModal.signatureError(appConstant.nexuFactory.userCancelledMessage);
						} else {
							showModal.signatureError(appConstant.nexuFactory.certificateError);
						}
						;
						deferred.reject(-1);
					});
					return deferred.promise;
				},

				/**
				 * Sign
				 */
				sign : function(nexuSignObj, certObj) {
					var signError = appConstant.nexuFactory.signatureError;
					var deferred = $q.defer();
					$http.post(targetURLScheme + "://localhost:" + runningPort + "/rest/sign", nexuSignObj).success(
							function(data, status, headers, config) {
								var signObj = {
									tlId : certObj.tlId,
									certificateB64 : certObj.certificate,
									signatureValue : data.response.signatureValue
								};
								deferred.resolve(signObj);
							}).error(function(data, status, headers, config) {
						showModal.signatureError(signError);
						deferred.reject(-1);
					});
					return deferred.promise;
				}

			};
		} ]);
