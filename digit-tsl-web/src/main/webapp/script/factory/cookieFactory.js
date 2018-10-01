/**
 * cookieFactory: manage draftStore cookie
 */
digitTslWeb.factory('cookieFactory', function($modal,
        $cookies, $q, httpFactory, showModal,appConstant) {
	var self = this;

	return {
		/**
		 * Return boolean Check if user has right to smthg SUP : always allowed
		 */
		verify : function(draftStoreId) {
			var deferred = $q.defer();
			httpFactory.get("/api/draft/checkDraftStore/" + draftStoreId,appConstant.cookieFactory.validityFailure).then(function(validity) {
				deferred.resolve(validity);
			}, function() {
				deferred.reject(-1);
			});
			return deferred.promise;
		},

		getCookie : function() {
			var deferred = $q.defer();
			var cookie = $cookies.get("tl-manager");
			if ((cookie == undefined) || (cookie == null)) {
				deferred.resolve("no-draft-store");
			} else {
				deferred.resolve(cookie);
			};
			return deferred.promise;
		},

		createCookie : function() {
			var deferred = $q.defer();
			$cookies.remove("tl-manager");
			httpFactory.get("/api/draft/createDraftStore",appConstant.cookieFactory.createFailure).then(function(draftStoreId) {


			    var now = new Date(),
			    exp = new Date(now.getFullYear()+10, now.getMonth(), now.getDate());
				$cookies.put("tl-manager",draftStoreId,{
                  expires: exp
                });
				showModal.draftStoreInformation(appConstant.cookieFactory.createCookie).then(function(){
					deferred.resolve(draftStoreId);
				});
			}, function(){
				deferred.reject(-1);
			});

			return deferred.promise;
		},

		setCookie : function(draftStoreId){
		    var deferred = $q.defer();
			$cookies.remove("tl-manager");
			 var now = new Date(),
             exp = new Date(now.getFullYear()+10, now.getMonth(), now.getDate());
             $cookies.put("tl-manager",draftStoreId,{
               expires: exp
             });
             deferred.resolve(draftStoreId);
             return deferred.promise;
		}

	};

});