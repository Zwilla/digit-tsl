/** ****** Browse a PointerToOtherTSL ******* */
function modalPtotController($scope, $modalInstance, $modal, ptot, territory, listChange, properties, listChecks, digitalIdService, httpFactory, appConstant, showModal) {
    initMessages($scope);

    $scope.publishedPointer = angular.copy(ptot);
    $scope.tPtot = ptot;
    $scope.languages = languagesProperties;
    $scope.mimeType = mimeTypeProperties;
    $scope.countryName = countryCodeNameProperties;
    $scope.tlCommunityRule = pointerCommunityRule;
    $scope.tlTypePointer = pointerTLType;
    $scope.listdiff = listChange;
    $scope.properties = properties;
    $scope.listChecks = listChecks;

    initScope($scope);

    /* MODAL FUNCTION */
    $scope.ok = function() {
        //Look through service digital identity
        if ($scope.tPtot.serviceDigitalId.length > 0) {
            var doubleCertificate = [];
            for (var i = 0; i < $scope.tPtot.serviceDigitalId.length; i++) {
                for (var j = 0; j < $scope.tPtot.serviceDigitalId[i].certificateList.length; j++) {
                    //If certificate is detected more than once in all digital identites & certificate isn't already in doubleCertificate list
                    if (isMoreThanOnce($scope.tPtot.serviceDigitalId[i].certificateList[j], $scope.tPtot.serviceDigitalId) && doubleCertificate.indexOf($scope.tPtot.serviceDigitalId[i].certificateList[j].certSubjectShortName)===-1) {
                        doubleCertificate.push($scope.tPtot.serviceDigitalId[i].certificateList[j].certSubjectShortName);
                    }
                }
            }
            //If there is at least one certificate find twice; display warning message
            if (doubleCertificate.length > 0) {
                //Format message 
                if(doubleCertificate.length===1){
                    var warningMessage = "<div>"+appConstant.pointerController.digitalDouble+"</div>";
                }else{
                    var warningMessage = "<div>"+appConstant.pointerController.digitalMultipleDouble+"</div>";   
                }
                for (var k = 0; k < doubleCertificate.length; k++) {
                    warningMessage += "<div>  - "+doubleCertificate[k]+"</div>";
                }
                //Display warning
                showModal.information(warningMessage, appConstant.modalTitle.warning);
            } else {
                $modalInstance.close($scope.tPtot);
            }
        } else {
            $modalInstance.close($scope.tPtot);
        }
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };

    /**
     * Add Scheme Operator & Community Rule
     */
    $scope.addSchemeOperator = function() {
        tmp = new Object();
        tmp.id = Math.floor(Math.random() * 100 + 1);
        $scope.tPtot.schemeOpeName.push(tmp);
    };

    $scope.addCommunityRule = function() {
        tmp = new Object();
        tmp.id = Math.floor(Math.random() * 100 + 1);
        $scope.tPtot.schemeTypeCommunity.push(tmp);
    };

    /**
     * Delete Scheme Operator & Community Rule
     */
    $scope.deleteSchemeOperator = function(item) {
        var index = $scope.tPtot.schemeOpeName.indexOf(item);
        $scope.tPtot.schemeOpeName.splice(index, 1);
    };

    $scope.deleteCommunityRule = function(item) {
        var index = $scope.tPtot.schemeTypeCommunity.indexOf(item);
        $scope.tPtot.schemeTypeCommunity.splice(index, 1);
    };

    /*------------------------------ Digital Identification (Factory) ----------------------------*/

    $scope.deleteDigitalIdentitie = function(digital) {
        $scope.tPtot.serviceDigitalId.splice($scope.tPtot.serviceDigitalId.indexOf(digital), 1);
    };

    $scope.addDigitalIdentitie = function() {
        digitalIdService.addDigitalIdentitie(listChange, listChecks, "pointer").then(function(tmpDigital) {
            $scope.tPtot.serviceDigitalId.push(tmpDigital);
        });
    };

    $scope.editDigitalIdentitie = function(digital, index) {
        var digitalBackUp = angular.copy(digital);
        digitalBackUp.checkToRun = $scope.tPtot.checkToRun;
        digitalIdService.editDigitalIdentitie(digitalBackUp, listChange, listChecks, "pointer").then(function(tmpDigital) {
            $scope.tPtot.serviceDigitalId[index] = tmpDigital;
        });
    }
    /*---------------------------------------------------------------------------*/

    /**
     * Filter properties (scheme territory / community rules ) depend on draft LOTL / TL & pointer type
     */
    $scope.filterEUProperties = function(propertie) {
        if ($scope.tPtot.countryName == "European Union") {
            if ($scope.tPtot.schemeTerritory == "EU") {
                if (propertie.territory == "EU") {
                    return true;
                } else {
                    return false;
                }
            } else if ($scope.tPtot.schemeTerritory == null || $scope.tPtot.schemeTerritory == "") {
                return true;
            } else {
                if (propertie.territory == "EU") {
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
    };

    $scope.getLOTLKeyStore = function() {
        showModal.confirmation(appConstant.pointerController.getLOTLDigital, appConstant.modalTitle.confirmation).then(function() {
            httpFactory.get("/api/keyStore/certPointer", appConstant.pointerController.getLOTLDigitalError).then(function(data) {
                $scope.tPtot.serviceDigitalId = data;
            });
        });
    };

    //Count the number of occurence of a certificate in all digital identity; return true if find more than once
    var isMoreThanOnce = function(certificate, serviceDigitalId) {
        var found = 0;
        for (var i = 0; i < serviceDigitalId.length; i++) {
            for (var j = 0; j < serviceDigitalId[i].certificateList.length; j++) {
                if (serviceDigitalId[i].certificateList[j].certB64 === certificate.certB64) {
                    found = found + 1;
                }
            }
        }
        return (found > 1);
    }

    /*------------------------------ CSS/HTML ----------------------------*/

    $scope.showMagicWand = function() {
        if (territory === "EU") {
            if ($scope.tPtot.schemeTerritory === "EU") {
                return true;
            } else {
                return false;
            }
        }
        return true;

    }
};