/** ****** Modal add User ******* */
function modalAddUserController($scope, $modalInstance, showModal, countryCodeNameProperties) {

    initMessages($scope);

    $scope.ecasId = "";
    $scope.territory = "EU";
    $scope.countryCodeNameProperties = countryCodeNameProperties;

    $scope.ok = function() {
        if (($scope.ecasId == "") || ($scope.territory == "")) {
            showModal.information($scope.userManagementController_fillFields);
        } else {
            var userForm = {
                ecasId : $scope.ecasId,
                country : $scope.territory
            };
            $modalInstance.close(userForm);
        }
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
};