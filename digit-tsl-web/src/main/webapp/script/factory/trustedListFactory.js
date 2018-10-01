/**
 * TrustedListFactory : interface between browserController & httpFactory
 */
digitTslWeb.factory('trustedListFactory', [ '$q', 'httpFactory', 'appConstant', 'showModal', '$window', function($q, httpFactory, appConstant, showModal, $window) {
    return {

        /**
         * Get TrustedList By ID
         */
        getTl : function(tlCookie) {
            var value = tlCookie.tlId;
            if (!isNaN(value) && function(x) {
                return (x | 0) === x;
            }(parseFloat(value))) {
                return httpFactory.put('/api/tl/', tlCookie, appConstant.trustedListFactory.getTlError);
            } else {
                if (/^([a-zA-Z]{2})$/.test(tlCookie.tlId)) {
                    return httpFactory.put('/api/tl/trustMark/' + tlCookie.tlId, appConstant.trustedListFactory.getTlError);
                } else {
                    showModal.information(appConstant.trustedListFactory.tlIdIncorrect).then(function() {
                        $window.location.href = '/tl-manager/home';
                    });
                }
            }
        },

        /**
         * Get Signature Information By ID
         */
        getSignatureInfo : function(tlCookie) {
            return httpFactory.post('/api/tl/signatureInfo', tlCookie, appConstant.trustedListFactory.getSignatureError);
        },

        /**
         * Get All changes Type : schemeInfo / pointers
         */
        getAllDifference : function(tlCookie, checkToRun) {
            var deferred = $q.defer();
            if (checkToRun) {
                return httpFactory.post('/api/changes/', tlCookie, appConstant.trustedListFactory.getChangeError);
            } else {
                deferred.resolve();
            }
            return deferred.promise;
        },

        /**
         * Get changes Type : schemeInfo / pointers
         */
        getDifference : function(tlCookie, type, checkToRun) {
            var deferred = $q.defer();
            if (checkToRun) {
                return httpFactory.post('/api/changes/' + type, tlCookie, appConstant.trustedListFactory.getChangeError);
            } else {
                deferred.resolve();
            }
            return deferred.promise;
        },

        /**
         * Get Checks
         */
        getChecks : function(tlCookie, checkToRun) {
            var deferred = $q.defer();
            if (checkToRun) {
                httpFactory.get("/api/tlcc/isActive", appConstant.trustedListFactory.tlccActiveError).then(function(isActive) {
                    if (isActive) {
                        return httpFactory.post('/api/checks/errors', tlCookie, appConstant.trustedListFactory.getCheckError).then(function(checks) {
                            deferred.resolve(checks);
                        }, function() {
                            deferred.reject(-1);
                        });
                    } else {
                        deferred.resolve([]);
                    }
                }, function() {
                    deferred.reject(-1);
                });
            } else {
                deferred.resolve();
            }
            return deferred.promise;
        },

        getLastEdit : function(tlId) {
            return httpFactory.get('/api/tl/edtDate/' + tlId, appConstant.trustedListFactory.getLastEditedError);
        },

        conflictTl : function(tl) {
            return httpFactory.put('/api/tl/conflict/', tl, appConstant.trustedListFactory.conflictTlFailure);
        },

        notifiedPointer : function(tlCookie) {
            return httpFactory.post('/api/tl/getNotifiedPointer', tlCookie, appConstant.trustedListFactory.notifiedPointerFailure);
        },

        switchCheckToRun : function(tlCookie) {
            return httpFactory.put('/api/tl/switchCheckToRun', tlCookie, appConstant.trustedListFactory.switchCheckToRunError);
        },

        runAllRules : function(tlCookie) {
            return httpFactory.put('/api/checks/cleanAndRunAllRules', tlCookie, appConstant.trustedListFactory.getAllCheckError);
        },

        draftEditionCheck : function() {
            return httpFactory.get('/api/tl/draftEditionCheck', appConstant.trustedListFactory.getDraftEditionCheckError);
        },

        archivedProds : function(countryCode) {
            return httpFactory.get('/api/tl/archivedsProd/' + countryCode, appConstant.trustedListFactory.getArchivedProdsError);
        },

        getChangesArchivedProd : function(prodId, archiveId) {
            return httpFactory.get('/api/changes/production/' + prodId + '/' + archiveId, appConstant.trustedListFactory.getChangesArchivedProdsError);
        },

        getSignatureChanges : function(tlCookie, checkToRun) {
            var deferred = $q.defer();
            if (checkToRun) {
                return httpFactory.post('/api/changes/signature', tlCookie, appConstant.trustedListFactory.signatureChangeError);
            } else {
                deferred.resolve();
            }
            return deferred.promise;
        },

    };

} ]);
