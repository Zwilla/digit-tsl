var digitTslWeb = angular.module("digitTslWeb", [ 'ui.grid', 'ui.bootstrap', 'ngAnimate', 'ngSanitize', 'ngCookies', 'nvd3', 'ui.toggle' ]);

digitTslWeb.config(function($httpProvider, datepickerConfig, $cookiesProvider) {

    datepickerConfig.startingDay = 1;
    datepickerConfig.showWeeks = false;

    if (!$httpProvider.defaults.headers.get) {
        $httpProvider.defaults.headers.get = {};
    };
});

/*------------------------ Properties ------------------------*/

/** List * */
var adrTypeProperties = [];
var countryCodeNameProperties = [];
var languagesProperties = [];
var mimeTypeProperties = [];
var schemeInfoTSLType = [];
var schemeInfoStatusDetermination = [];
var schemeInfoCommunityRule = [];
var pointerTLType = [];
var pointerCommunityRule = [];
var serviceStatusProperties = [];
var servicePreviousStatusProperties = [];
var qualifiersProperties = [];
var serviceTypeIdentifiersProperties = [];
var identifierQualifierTypeProperties = [];
var additionnalServiceInfoQualifierProperties = [];

/*-----------------------------------*/

/**
 * Check undefined/null/empty & init variable by type
 */
var initVar = function(value, type) {
    var result;
    if (value !== undefined && value !== null) {
        return value;
    } else {
        if (type === "array") {
            return [];
        } else if (type === "string") {
            return "";
        } else if (type === "numeric") {
            return 0;
        } else if (type === "date") {
            return null;
        } else if (type === "object") {
            return new Object();
        } else {
            console.error("Undefined variable (type:" + type + ")");
            return undefined;
        };
    };
};

/** Self implementation of string.StartsWith not supported by IE * */
var strStartsWith = function(v1,v2){
    if(v1!=undefined && v1!=null && v1.indexOf(v2)==0){
        return true;
    };
    return false;
};

/** Self implementation string.endsWith not supported by IE * */
var endsWith = function(str, suffix) {
    return str.indexOf(suffix, str.length - suffix.length) !== -1;
};

/*---------------------- Modal ------------------*/

/** Basic modal Controller * */
var ModalInstanceCtrl = function($scope, $modalInstance) {
    $scope.ok = function() {
        $modalInstance.close();
    };

    $scope.cancel = function(response) {
        $modalInstance.dismiss(response);
    };
};

/** Modal Controller with param message * */
var ModalInstanceCtrlMessage = function($scope, $modalInstance, message) {
    $scope.message = message;

    $scope.ok = function() {
        $modalInstance.close();
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
};

/** Return confirm modal html with dynamic message * */
var confirmModalHtml = function() {
    var modalHtml = '<div class="panel panel-primary" >' +
        '<div class="panel-heading">' + '<span>Application: Confirmation</span>'+
            '</div>' + '<div class="panel-body">'+
                '<span style="word-break: break-word" ng-bind="message"></span>'+
            '</div>'+footerModal2B;
    return modalHtml;
};

/** Modal footer with 1 action button * */
var footerModal1B = '<div class="panel-footer" align="right"><button class="btn btn-warning" ng-click="ok()">OK</button></div></div>'
/** Modal footer with 2 action button * */
var footerModal2B = '<div class="panel-footer" align="right"><button class="btn btn-primary" ng-click="ok()" style="margin-right:10px;">OK</button><button class="btn btn-warning" ng-click="cancel()">Cancel</button></div></div>';

/*----------------------------------------------*/

/**
 * Service Http.Response Handler
 */
serviceResponseHandler = function(data) {
    if (data.responseStatus === "200" || data.responseStatus=="202") {
        return true;
    } else {
        return false;
    };
};

/*------------------- Dynamic Sort array ----------------*/

/** Sort array by one property * */
var dynamicSort = function(property) {
    var sortOrder = 1;
    if (property[0] === "-") {
        sortOrder = -1;
        property = property.substr(1);
    }
    return function(a, b) {
        var result = a[property] < b[property] ? -1 : a[property] > b[property] ? 1 : 0;
        return result * sortOrder;
    };
};

/** Sort array by multiple property * */
var dynamicSortMultiple = function() {
    var props = arguments;
    return function(obj1, obj2) {
        var i = 0, result = 0, numberOfProperties = props.length;
        while (result === 0 && i < numberOfProperties) {
            result = dynamicSort(props[i])(obj1, obj2);
            i++;
        };
        return result;
    };
};

/*------------------- CSS ----------------------*/

var styleAvailableStatus = function(status) {
    if (status !== "AVAILABLE") {
        if (status == "UNSUPPORTED" || status == "UNAVAILABLE") {
            return "errorColor";
        } else {
            return "warnColor";
        };
    };
    return "";
};

/**
 * Show/hide row Hide if value is null in prod
 */
var objVisibility = function(value, myTl) {
    return myTl!=undefined && myTl!=null && myTl.dbStatus == "PROD" && !value;
};

/*------------------- Filter ---------------------*/

/**
 * Troncate a String an add '...' at the end
 */
digitTslWeb.filter('cut', function() {
    return function(value, wordwise, max, tail) {
        if (!value) {
            return '';
        }

        max = parseInt(max, 10);
        if (!max) {
            return value;
        }
        if (value.length <= max) {
            return value;
        }

        value = value.substr(0, max);
        if (wordwise) {
            var lastspace = value.lastIndexOf(' ');
            if (lastspace !== -1) {
                value = value.substr(0, lastspace);
            }
        }

        return value + (tail || ' …');
    };
});

/**
 * Convert millisecondes to time (HH:mm:ss)
 */
digitTslWeb.filter('millSecondsToTimeString', function() {
    return function(milliseconds) {
        var seconds = parseInt(milliseconds / 1000 % 60);
        var minutes = parseInt(milliseconds / (1000 * 60) % 60);
        var hours = parseInt(milliseconds / (1000 * 60 * 60) % 24);
        var days = parseInt(milliseconds / (1000 * 60 * 60) / 24);


        hours = hours < 10 ? "0" + hours : hours;
        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        if(days>0){
            var out = days +"d - "+ hours + "h:" + minutes + "m:" + seconds + "s";
        }else{
            var out = hours + "h:" + minutes + "m:" + seconds + "s";
        }
        return out;
    };
});

/**
 * Capitalize
 */
digitTslWeb.filter('capitalize', function() {
    return function(str) {
        if (str !== null) {
            var toLower = str.toLowerCase();
            return toLower[0].toUpperCase() + toLower.slice(1);
        }
    };
});

/**
 * Labelize Tag/Enum Ex: MY_TAG_TEST -> My Tag Test
 */
digitTslWeb.filter('labelizeTag', function() {
    return function(str) {
        if (str !== null && str.indexOf("_") > -1) {
            var splitArray = str.split("_");
            var result = "";
            for (var i = 0; i < splitArray.length; i++) {
                var toLower = splitArray[i].toLowerCase();
                result = result + toLower[0].toUpperCase() + toLower.slice(1) + " ";
            }
            ;
            return result;
        } else {
            var toLower = str.toLowerCase();
            return toLower[0].toUpperCase() + toLower.slice(1);
        };
    };
});

/**
 * Replace code by string
 */
digitTslWeb.filter('replaceIn', function() {
    return function(value, key, replaceStr) {
        var re = new RegExp(key, 'g');
        return value.replace(re,replaceStr);
    };
});

/*--------------------------------- Directive Functions  ---------------------------------*/

digitTslWeb.directive('compile', [ '$compile', function($compile) {
    return function(scope, element, attrs) {
        scope.$watch(function(scope) {
            return scope.$eval(attrs.compile);
        }, function(value) {
            element.html(value);
            $compile(element.contents())(scope);
        })
    };
} ]);


/*--------- USED IN CHECK & CHANGE DIRECTIVE -------------*/

/**
 * Set idHr to label w/ margin and line-skip Transform with ngSanitize in view
 */
var explodeLocation = function(value) {
    var htmlLocation = "";
    if (value) {
        var res = value.split("||");
        var margin = 1;
        if (res !== null) {
            htmlLocation = '<span class="check-label" >' + res[0] + '</span>'
            for (var i = 1; i < res.length; i++) {
                htmlLocation = htmlLocation + '<div class="check-margin">' + '<span class="fa fa-share check-icon"></span><span>' + res[i] + '</span>';
                margin = margin + 1;
            };
            for (var j = 0; j < margin; j++) {
                htmlLocation = htmlLocation + '</div>';
            };
        };
    };
    return htmlLocation;
};

/**
 * Check if a difference correspond to the ID in function of the id & the boolean istreeparent
 */
var isInList = function(difference, id, istreeparent) {
    return difference.id !== null && (difference.id == id || istreeparent && strStartsWith(difference.id,id + '_'));
};
