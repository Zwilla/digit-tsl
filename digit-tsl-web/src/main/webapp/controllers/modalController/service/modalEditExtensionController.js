/** ****** Extension modal edition ******* */
function modalEditExtensionController($scope, $modalInstance, $modal, extension, listChecks, listDiff, typeModal, extensionFactory, showModal) {
    initMessages($scope);

    $scope.extension = extension;
    $scope.listChecks = listChecks;
    $scope.listDiff = listDiff;
    $scope.countryName = countryCodeNameProperties;
    $scope.languages = languagesProperties;
    $scope.qualifiers = qualifiersProperties;
    $scope.identifierType = identifierQualifierTypeProperties;
    $scope.additionnalQualifier = additionnalServiceInfoQualifierProperties;
    $scope.isQualificationExtension = false;

    initScope($scope);
    
    /* MODAL FUNCTION */
    $scope.ok = function() {
        if (typeModal === "expiredCertRevocationDate" && $scope.extension.expiredCertsRevocationDate == null) {
            showModal.applicationError($scope.extension_expiredCertDateNotFilled);
        } else {
            initBool($scope.extension.critical);
            initKeyUsageBool();
            $modalInstance.close($scope.extension);
        }
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };

    $scope.addTable = function(table) {
        if(table==null){
            table = []
        }
        var obj = new Object();
        obj.id = Math.floor((Math.random() * 100) + 1);
        table.push(obj);
        return table;
    };

    $scope.addTableString = function(table) {
        table.push("");
    };

    $scope.deleteTab = function(table, item) {
        var index = table.indexOf(item);
        table.splice(index, 1);
    };

    /* Init a table */
    $scope.initTable = function(table) {
        if (table == undefined || table == null) {
            return [];
        }
    };

    /**
     * Calendar
     */
    $scope.open = function($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    };

    $scope.format = 'yyyy-MM-dd HH:mm:ss';

    /** ------------ Qualification Extension ------------* */

    $scope.isUserDefined = function() {
        for (var i = 0; i < $scope.additionnalQualifier.length; i++) {
            if ($scope.additionnalQualifier[i].label == $scope.extension.additionnalServiceInfo.value) {
                return false;
            }
        }
        return true;
    };

    $scope.additionnalUserDefined = function() {
        if ($scope.extension.additionnalServiceInfo.value != undefined && $scope.extension.additionnalServiceInfo.value != null) {
            if (!$scope.isUserDefined()) {
                $scope.extension.additionnalServiceInfo.value = "";
            }
        }
    };

    /** ------------ Qualification Extension ------------* */

    /* Qualification Extension table */

    $scope.addCriteria = function(criteria) {
        criteria.criteriaList.push(extensionFactory.newCriteria());
    };

    $scope.addQualification = function() {
        extension.qualificationsExtension.push(extensionFactory.initQualificationExtension());
    }

    $scope.addPolicyList = function(policyList) {
        if (policyList == undefined || policyList == null) {
            policyList = [];
        }
        var obj = new Object();
        obj.policyBit = [];
        policyList.push(obj);
    };

    $scope.addPolicyIdentifier = function(policyBit) {
        if (policyBit == undefined || policyBit == null) {
            policyBit = [];
        }
        var bit = new Object();
        bit.id = Math.floor((Math.random() * 100) + 1);
        bit.documentationReferences = [];
        policyBit.push(bit);
    };

    $scope.addKeyUsageList = function(criteria) {
        if (criteria.keyUsage == undefined || criteria.keyUsage == null) {
            criteria.keyUsage = [];
        }
        var obj = new Object();
        obj.keyUsageBit = extensionFactory.initKeyUsage(null);
        obj.show = "1";
        criteria.keyUsage.push(obj);
    };

    $scope.initPolicyList = function(criteria) {
        if (criteria.policyList == undefined || criteria.policyList == null) {
            criteria.policyList = [];
        } else {
            for (var i = 0; i < criteria.policyList.length; i++) {
                for (var j = 0; j < criteria.policyList[i].policyBit.length; j++) {
                    if (criteria.policyList[i].policyBit[j].documentationReferences == null) {
                        criteria.policyList[i].policyBit[j].documentationReferences = [];
                    }
                }
            }
        }
        return criteria;
    };

    $scope.initOtherList = function(otherList) {
        if (otherList == undefined || otherList == null) {
            otherList = new Object();
            otherList.certDnaList = [];
            otherList.extendedKeyUsageList = [];
        } else {
            if (otherList.certDnaList == undefined || otherList.certDnaList == null) {
                otherList.certDnaList = [];
            }
            if (otherList.extendedKeyUsageList == undefined || otherList.extendedKeyUsageList == null) {
                otherList.extendedKeyUsageList = [];
            }
        }
        return otherList;
    };

    /**
     * Init bool to true/false for select
     */
    var initBool = function(value) {
        if (value != undefined && value != null) {
            if (value == "false" || value == false) {
                return false;
            } else if (value == "true" || value == true) {
                return true;
            } else {
                return null;
            }
        } else {
            return null;
        }
    };
    $scope.extension.critical = initBool($scope.extension.critical);

    /**
     * Init keyUsage list
     */
    var initKeyUsageBool = function() {
        if ($scope.isQualificationExtension != false) {
            for (var i = 0; i < $scope.extension.qualificationsExtension.length; i++) {
                // Init Criteria keyUsage
                if ($scope.extension.qualificationsExtension[i].criteria != undefined && $scope.extension.qualificationsExtension[i].criteria != null) {
                    if ($scope.extension.qualificationsExtension[i].criteria.keyUsage != undefined && $scope.extension.qualificationsExtension[i].criteria.keyUsage != null) {
                        for (var j = 0; j < $scope.extension.qualificationsExtension[i].criteria.keyUsage.length; j++) {
                            if ($scope.extension.qualificationsExtension[i].criteria.keyUsage[j].keyUsageBit != null) {
                                for (var k = 0; k < $scope.extension.qualificationsExtension[i].criteria.keyUsage[j].keyUsageBit.length; k++) {
                                    $scope.extension.qualificationsExtension[i].criteria.keyUsage[j].keyUsageBit[k].isValue = initBool($scope.extension.qualificationsExtension[i].criteria.keyUsage[j].keyUsageBit[k].isValue);
                                }
                            }
                        }
                    }

                    // Init CriteriaList keyUsage
                    if ($scope.extension.qualificationsExtension[i].criteria.criteriaList != undefined && $scope.extension.qualificationsExtension[i].criteria.criteriaList != null) {
                        for (var a = 0; a < $scope.extension.qualificationsExtension[i].criteria.criteriaList.length; a++) {
                            if ($scope.extension.qualificationsExtension[i].criteria.criteriaList[a].keyUsage != null) {
                                for (var j = 0; j < $scope.extension.qualificationsExtension[i].criteria.criteriaList[a].keyUsage.length; j++) {
                                    if ($scope.extension.qualificationsExtension[i].criteria.criteriaList[a].keyUsage[j].keyUsageBit != null) {
                                        for (var k = 0; k < $scope.extension.qualificationsExtension[i].criteria.criteriaList[a].keyUsage[j].keyUsageBit.length; k++) {
                                            $scope.extension.qualificationsExtension[i].criteria.criteriaList[a].keyUsage[j].keyUsageBit[k].isValue = initBool($scope.extension.qualificationsExtension[i].criteria.criteriaList[a].keyUsage[j].keyUsageBit[k].isValue);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    };
    initKeyUsageBool();

    /*--- DOM MANAGEMENT ---*/

    $scope.initShow = function(obj) {
        if (obj != undefined && obj.show == undefined) {
            obj.show = '0';
        }
        ;
    };

};