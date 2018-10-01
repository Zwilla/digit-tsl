/**
 * ngConfirm : display check icon & open detail in modal on click Type : 2 attributes in html tag Parameter : * ngConfirm - string (value opt) : message to show || Undefined: Do
 * you confirm this action ? * confirmed - string : action to call -> my_function(param,param2)
 */
digitTslWeb.directive('ngConfirm', [ '$modal', 'appConstant', 'showModal', function($modal, appConstant, showModal) {
    return {
        link : function(scope, element, attr) {
            // On element click
            element.bind('click', function(event) {
                // Init message
                var message = attr.ngConfirm || appConstant.confirmationDirective.genericConfirmation;
                showModal.confirmation(message, appConstant.modalTitle.confirmation).then(function() {
                    scope.$eval(attr.confirmed);
                });
            });
        }
    };
} ]);
