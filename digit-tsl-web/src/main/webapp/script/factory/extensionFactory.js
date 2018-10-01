/**
 * ExtensionFactory : manage extension browsing/edition
 */
digitTslWeb.factory('extensionFactory', [
        '$q',
        '$modal',
        'showModal',
        'appConstant',
        function($q, $modal, showModal, appConstant) {
            return {

                /**
                 * Return Extension (on modal validation); Initialize new extension depend off the typeExtension & Call the appropriate modal; Parameter: typeExtension (String) -
                 * additionnalServiceInformation - takenOverBy - qualificationExtension - expiredCertRevocationDate
                 */
                addExtension : function(typeExtension) {
                    var deferred = $q.defer();
                    var extension = new Object();
                    var listChecks = [];
                    var listDiff = [];
                    var typeModal = null;
                    if (typeExtension == "additionnalServiceInformation") {
                        // Init & call for additionnal Extension
                        extension.additionnalServiceInfo = new Object();
                        extension.additionnalServiceInfo.language = "en";
                        extension.additionnalServiceInfo.value = "";
                        typeModal = "modalAdditonnalExtension";
                    } else if (typeExtension == "takenOverBy") {
                        // Init & call for taken Over by Extension
                        extension.takenOverBy = new Object();
                        extension.takenOverBy.tspName = new Array();
                        extension.takenOverBy.operatorName = new Array();
                        extension.takenOverBy.OtherQualifier = new Array();
                        extension.takenOverBy.url = new Object();
                        extension.takenOverBy.url.language = "en";
                        typeModal = "modalTakenOverBy";
                    } else if (typeExtension == "qualificationExtension") {
                        // Init & call for qualification Extension
                        extension.qualificationsExtension = [];
                        extension.qualificationsExtension.push(this.initQualificationExtension());
                        typeModal = "modalQualificationExtension";
                    } else if (typeExtension == "expiredCertRevocationDate") {
                        extension.expiredCertsRevocationDate = null;
                        typeModal = "modalExpiredCertRevocationDate";
                    } else {
                        showModal.applicationError(appConstant.extensionFactory.addExtensionFailure);
                        deferred.reject(-1);
                    }

                    this.invokeModal(typeModal, extension, listChecks, listDiff, typeExtension).then(function(ext) {
                        deferred.resolve(ext);
                    }, function() {
                        deferred.reject(-1);
                    });
                    return deferred.promise;
                },

                /**
                 * Return Extension (on modal validation) Initialize extension edition depend off extension value/type & Call the appropriate modal
                 */
                editExtension : function(extension, listChecks, listDiff) {
                    var deferred = $q.defer();
                    var typeModal = null;
                    if ((extension != undefined) && (extension != null)) {
                        if ((extension.takenOverBy != undefined) && (extension.takenOverBy != null)) {
                            typeModal = "modalTakenOverBy";
                        } else if ((extension.qualificationsExtension != undefined) && (extension.qualificationsExtension != null)) {
                            this.initKeyUsageList(extension.qualificationsExtension);
                            this.initEditQualification(extension.qualificationsExtension);
                            typeModal = "modalQualificationExtension";
                        } else if ((extension.additionnalServiceInfo != undefined) && (extension.additionnalServiceInfo != null)) {
                            typeModal = "modalAdditonnalExtension";
                        } else if ((extension.expiredCertsRevocationDate != undefined) && (extension.expiredCertsRevocationDate != null)) {
                            typeModal = "modalExpiredCertRevocationDate";
                        } else if ((extension.anyType != undefined) && (extension.anyType != null)) {
                            showModal.information(appConstant.extensionFactory.anyTypeNotSupported);
                            deferred.reject(-1);
                        } else {
                            showModal.applicationError(appConstant.extensionFactory.addExtensionFailure);
                            deferred.reject(-1);
                        }

                        this.invokeModal(typeModal, extension, listChecks, listDiff, typeModal).then(function(ext) {
                            deferred.resolve(ext);
                        }, function() {
                            deferred.reject(-1);
                        });
                    } else {
                        showModal.applicationError(appConstant.extensionFactory.editExtensionFailure);
                        deferred.reject(-1);
                    }

                    return deferred.promise;
                },

                /**
                 * Invoke modal depend on the typeExtension
                 */
                invokeModal : function(typeExtension, extension, listChecks, listDiff, typeModal) {
                    var deferred = $q.defer();
                    var modalInstance = $modal.open({
                        templateUrl : typeExtension,
                        controller : modalEditExtensionController,
                        size : 'lg',
                        backdrop : 'static',
                        resolve : {
                            extension : function() {
                                return extension;
                            },
                            listDiff : function() {
                                return listDiff;
                            },
                            listChecks : function() {
                                return listChecks;
                            },
                            typeModal : function() {
                                return typeModal;
                            }
                        }
                    });

                    modalInstance.result.then(function(ext) {
                        deferred.resolve(ext);
                    }, function() {
                        deferred.reject(-1);
                    });
                    return deferred.promise;
                },

                /**
                 * Return the label of an extension depend on type
                 */
                extensionLabel : function(extension) {
                    if ((extension != undefined) && (extension != null)) {
                        if ((extension.takenOverBy != undefined) && (extension.takenOverBy != null)) {
                            return appConstant.extensionFactory.takenOverBy;
                        } else if ((extension.qualificationsExtension != undefined) && (extension.qualificationsExtension != null)) {
                            return appConstant.extensionFactory.qualificationExtension;
                        } else if ((extension.additionnalServiceInfo != undefined) && (extension.additionnalServiceInfo != null)) {
                            return appConstant.extensionFactory.additionnalService;
                        } else if ((extension.expiredCertsRevocationDate != undefined) && (extension.expiredCertsRevocationDate != null)) {
                            return appConstant.extensionFactory.expiredCertificate;
                        } else if ((extension.anyType != undefined) && (extension.anyType != null)) {
                            return appConstant.extensionFactory.anyType;
                        } else {
                            return appConstant.extensionFactory.extUndefined;
                        }
                    }
                },

                /**
                 * Initialize Qualification extension :KeyUsage init keyBit already existant init new keyBit to undefined
                 */
                initKeyUsage : function(keyUsageList) {
                    var newKeyUsage = false;
                    if ((keyUsageList == undefined) || (keyUsageList == null) || (keyUsageList.length == 0)) {
                        keyUsageList = [];
                        newKeyUsage = true;
                    }
                    var keyUsageLib = appConstant.extensionFactory.keyUsageList;
                    if (newKeyUsage) {
                        // Init all the new entries for the new KeyUsage
                        for (var i = 0; i < keyUsageLib.length; i++) {
                            var obj = new Object();
                            obj.value = keyUsageLib[i];
                            obj.isValue = 'undefined';
                            keyUsageList.push(obj);
                        }
                    } else {
                        for (var i = 0; i < keyUsageLib.length; i++) {
                            var alreadyInit = false;
                            // Test if the keyUsageBit is already present in the keyUsage
                            for (var j = 0; j < keyUsageList.length; j++) {
                                if (keyUsageLib[i] == keyUsageList[j].value) {
                                    if (keyUsageList[j].isValue == true) {
                                        keyUsageList[j].isValue = "true";
                                    } else if (keyUsageList[j].isValue == false) {
                                        keyUsageList[j].isValue = "false";
                                    } else {
                                        keyUsageList[j].isValue = "undefined";
                                    }
                                    alreadyInit = true;
                                }
                            }

                            // Create a new entry if doesn't exist
                            if (alreadyInit == false) {
                                var obj = new Object();
                                obj.value = keyUsageLib[i];
                                obj.isValue = "undefined";
                                keyUsageList.push(obj);
                            }
                        }
                    }
                    keyUsageList.sort(dynamicSort("value"));
                    return keyUsageList;
                },

                /**
                 * Init all KeyUsage list of an extension
                 */
                initKeyUsageList : function(qualificationExtension) {
                    if ((qualificationExtension != undefined) && (qualificationExtension != null)) {
                        for (var a = 0; a < qualificationExtension.length; a++) {
                            // Criteria key usage
                            var keyUsageList = qualificationExtension[a].criteria.keyUsage;
                            if ((keyUsageList == undefined) || (keyUsageList == null)) {
                                keyUsageList = [];
                            } else {
                                for (var b = 0; b < keyUsageList.length; b++) {
                                    keyUsageList[b].keyUsageBit = this.initKeyUsage(keyUsageList[b].keyUsageBit);
                                }
                            }

                            // Other Criteria
                            if (qualificationExtension[a].criteria.criteriaList != null) {
                                for (var b = 0; b < qualificationExtension[a].criteria.criteriaList.length; b++) {
                                    // Criteria key usage
                                    var keyUsageList = qualificationExtension[a].criteria.criteriaList[b].keyUsage;
                                    if ((keyUsageList == undefined) || (keyUsageList == null)) {
                                        keyUsageList = [];
                                    } else {
                                        for (var b = 0; b < keyUsageList.length; b++) {
                                            keyUsageList[b].keyUsageBit = this.initKeyUsage(keyUsageList[b].keyUsageBit);
                                        }
                                    }
                                }
                            }
                        }
                    }
                },

                initQualificationExtension : function() {
                    var qualification = new Object();
                    qualification.qualifTypeList = [];
                    qualification.qualifTypeList.push("");
                    qualification.criteria = new Object();
                    qualification.criteria.asserts = "";
                    qualification.criteria.description = "";
                    qualification.criteria.keyUsage = [];
                    qualification.criteria.policyList = [];
                    qualification.criteria.criteriaList = [];
                    qualification.criteria.otherList = new Object();
                    qualification.criteria.otherList.certDnaList = [];
                    qualification.criteria.otherList.extendedKeyUsageList = [];
                    return qualification;
                },

                initEditQualification : function(qualificationExtension) {
                    for (var i = 0; i < qualificationExtension.length; i++) {
                        var qualification = qualificationExtension[i];
                        if ((qualification.criteria.keyUsage == undefined) || (qualification.criteria.keyUsage == null)) {
                            qualification.criteria.keyUsage = [];
                        }
                        if ((qualification.criteria.policyList == undefined) || (qualification.criteria.policyList == null)) {
                            qualification.criteria.policyList = [];
                        }
                        if ((qualification.criteria.criteriaList == undefined) || (qualification.criteria.criteriaList == null)) {
                            qualification.criteria.criteriaList = [];
                        }
                        if ((qualification.criteria.otherList == undefined) || (qualification.criteria.otherList == null)) {
                            qualification.criteria.otherList = new Object();
                            qualification.criteria.otherList.certDnaList = [];
                            qualification.criteria.otherList.extendedKeyUsageList = [];
                        } else {
                            if ((qualification.criteria.otherList.certDnaList == undefined) || (qualification.criteria.otherList.certDnaList == null)
                                    || (qualification.criteria.otherList.certDnaList.length == 0)) {
                                qualification.criteria.otherList.certDnaList = [];
                            }
                            if ((qualification.criteria.otherList.extendedKeyUsageList == undefined) || (qualification.criteria.otherList.extendedKeyUsageList == null)
                                    || (qualification.criteria.otherList.extendedKeyUsageList.length == 0)) {
                                qualification.criteria.otherList.extendedKeyUsageList = [];
                            }
                        }
                    }
                },

                newCriteria : function() {
                    var criteria = new Object();
                    criteria.asserts = "";
                    criteria.description = "";
                    criteria.keyUsage = [];
                    criteria.policyList = [];
                    criteria.criteriaList = [];
                    criteria.otherList = new Object();
                    criteria.otherList.certDnaList = [];
                    criteria.otherList.extendedKeyUsageList = [];
                    return criteria;
                }

            };

        } ]);
