/********* Modal to Sign a TrustedList ************/
function modalSignController($scope, $modalInstance,  myTLInfo,myTl,$modal,httpFactory,nexuFactory,showModal,accessRightFactory,appConstant) {
    initMessages($scope);

    $scope.myTLInfo = myTLInfo;
    $scope.myTl = myTl;
    $scope.signatureProccessStatus = "";
    $scope.loadSign=false;
    $scope.certSelect = null;


    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };

    $scope.authorized = function(label){
        return accessRightFactory.authorized($scope.myTLInfo.userInfo,label);
    };

    /**
     * Reset Seal checkBox
     */
    $scope.resetCert= function(){
        $scope.myTLInfo.selectedSealSC=undefined;
        $scope.certSelect = "seal";
    }

    /**
     * Reset SmartCard CheckBox
     */
    $scope.resetSmartCard=function(){
        $scope.myTLInfo.selectedSeal=undefined;
        $scope.certSelect = "smartcard";
    }

    $scope.ok = function() {
        if($scope.certSelect=="smartcard"){ //User choosed a smartcard certificate w/ NexU
            $scope.signatureProccessStatus = appConstant.nexuLoading.checkVersion;
            $scope.loadSign = false;
            nexuFactory.info().then(function() {
                $scope.signatureProccessStatus = appConstant.nexuLoading.getSmart;
                nexuFactory.certificates($scope.myTLInfo.id).then(function(certObj) {
                    $scope.signatureProccessStatus = appConstant.nexuLoading.getTbs;
                    httpFactory.post('/api/signature/nexU/getTbs/', certObj,appConstant.nexuLoading.sigError).then(function(nexuSignObj) {
                        $scope.signatureProccessStatus = appConstant.nexuLoading.finalize;
                        nexuFactory.sign(nexuSignObj,certObj).then(function(signObj) {
                            httpFactory.post('/api/signature/nexU/sign/', signObj,appConstant.nexuLoading.sigError).then(function(signature) {
                                $modalInstance.close(signature);
                            },function() {
                                $modalInstance.close();
                            });
                        },function() {
                            $modalInstance.close();
                        });
                    },function() {
                        $modalInstance.close();
                    });
                },function() {
                    $modalInstance.close();
                });
            },function() {
                $modalInstance.close();
            });
        }else if($scope.myTLInfo.selectedSeal!=undefined){ //User choosed a seal certificate
            $scope.loadSign = false;
            var tlSignatureInformation = {
                    tlId : $scope.myTLInfo.id,
                    sealName : $scope.myTLInfo.selectedSeal,
            };
            httpFactory.post('/api/signature/signTl/', tlSignatureInformation,appConstant.nexuLoading.sigError).then(function(data) {
                $modalInstance.close(data);
            },function() {
                $modalInstance.close();
            });
        }else{ //User didn't choose any certificate
            showModal.applicationError(appConstant.nexuLoading.chooseCert);
        };
    };

    /**
     * Get Seal list
     * (execute on loading)
     */
    var initSealList = function(){
        httpFactory.get("/api/signature/sealList",appConstant.nexuFactory.getSealError).then(function(data) {
            if(data.length==0){
                $scope.certSelect="smartcard";
                $scope.ok();
            }else{
                $scope.listSeal = data;
                $scope.loadSign=true;
            }
        });
    };

    /**
     * Run signature validation with NexU (smartcard)
     * automatically if TL is not a LOTL
     */
    var initSignature = function(){
        if($scope.authorized("SIG")){
            initSealList();
        }else{
            $scope.certSelect="smartcard";
            $scope.ok();
        };
    };
    initSignature();

};