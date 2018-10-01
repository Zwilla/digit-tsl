digitTslWeb.controller('userManagementController', [ '$scope', '$modal', 'httpFactory', 'showModal', 'appConstant', function($scope, $modal, httpFactory, showModal, appConstant) {

    $scope.ecasNameToAdd = "";

    $scope.getApplicationProperties(""); // Get Application properties
    $scope.countryCodeNameProperties = countryCodeNameProperties.sort();
    $scope.loadUser = false;

    var initGetUser = function() {
        $scope.loadUserStatus = $scope.userController_loadUsers;
        httpFactory.get("/api/list/roles", $scope.userManagementController_roleInitFailure).then(function(data) {
            $scope.roles = data;
            $scope.loadUserStatus = $scope.userController_loadRoles;
            httpFactory.get("/api/list/users", $scope.userManagementController_userInitFailure).then(function(data) {
                $scope.users = data;
                $scope.loadUser = true;
            }, function() {
                $scope.loadUser = true;
            });
        }, function() {
            $scope.loadUser = true;
        });
    };
    initGetUser();

    var refreshUsers = function() {
        httpFactory.get("/api/list/users", $scope.userManagementController_userInitFailure).then(function(data) {
            $scope.users = data;
            $scope.loadUser = true;
        });
    }

    $scope.initCheck = function(user, role) {
        for (var i = 0; i < user.role.length; i++) {
            if (role.code == user.role[i].code) {
                return true;
            }
        }
        return false;
    };

    $scope.isSup = function(user) {
        for (var i = 0; i < user.role.length; i++) {
            if (user.role[i].code == "SUP") {
                return true;
            } else {
                return false;
            }
        }
    };

    $scope.addOrRemoveRole = function(user, role) {
        for (var i = 0; i < user.role.length; i++) {
            if (role.code == user.role[i].code) {
                removeUserRole(user.id, role.id);
                return;
            }
        }
        addUserRole(user.id, role.id);
        return;
    };

    var removeUserRole = function(userId, roleId) {
        var userDto = {
            userId : userId,
            roleId : roleId
        };
        httpFactory.post('/api/user/removeRole/', userDto, $scope.userManagementController_removeUserRoleFailure).then(function() {
            showModal.information(appConstant.userController.updateSuccessful);
            $scope.loadUser = true;
        }, function() {
            refreshUsers();
        });
    };

    var addUserRole = function(userId, roleId) {
        var userDto = {
            userId : userId,
            roleId : roleId
        };
        httpFactory.post('/api/user/addRole/', userDto, $scope.userManagementController_addUserRoleFailure).then(function() {
            showModal.information(appConstant.userController.updateSuccessful);
            $scope.loadUser = true;
        }, function() {
            refreshUsers();
        });
    };

    $scope.reallyDelete = function(user) {
        httpFactory.post('/api/user/delUser/', user.id, $scope.userManagementController_deleteUserFailure).then(function() {
            showModal.information(appConstant.userController.updateSuccessful);
            $scope.users.splice($scope.users.indexOf(user), 1);
        }, function() {
            refreshUsers();
        });
    };

    $scope.addUser = function() {
        var modalInstance = $modal.open({
            templateUrl : "modalAddUser",
            controller : modalAddUserController,
            resolve : {
                countryCodeNameProperties : function() {
                    return countryCodeNameProperties;
                }
            }
        });

        modalInstance.result.then(function(userForm) {
            httpFactory.post('/api/user/addUser/', userForm, $scope.userManagementController_addUserFailure).then(function(data) {
                $scope.users.push(data);
                showModal.information(appConstant.userController.userAdded.replace("%NAME%", data.ecasId));
                $scope.ecasNameToAdd = "";
            });
        });



    };

    $scope.updateTerritory = function(user) {
        var userForm = {
            ecasId : user.ecasId,
            country : user.codeTerritory
        };
        httpFactory.post('/api/user/updateTerritory/', userForm, $scope.userManagementController_updateTerritoryFailure).then(function() {
            showModal.information(appConstant.userController.updateSuccessful);
            $scope.loadUser = true;
        }, function() {
            refreshUsers();
        });
    };

} ]);
