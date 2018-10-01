/**
 * uri : display a clickable uri
 * Type : html tag
 * Parameter :
 ** value - string  : uri & value displayed if cutvalue is undefined
 ** cutvalue - string (opt): truncate value of uri
 */
digitTslWeb.directive('uri',[ '$window',function($window){
	return {
		restrict: 'EA',
        replace: false,
        transclude: true,
        scope : {
        	value: '=',
        	cutvalue : '='
        },
		template :
				'<span ng-if="isURI(value)" ng-dblclick="clickURI(value)" target="_blank" class="uri" >{{label}}</span>'+
				'<span ng-if="!isURI(value)">{{value}}</span>',
		link : function($scope,element,attrs){

			/* Define if value start like an uri */
			$scope.isURI = function(value){
				if(strStartsWith(value,"http")){
					return true;
				}else if(strStartsWith(value,"www")){
					$scope.value = "http://"+value;
					return true;
				};
				return false;
			};

			var initLabel = function(){
				if(($scope.cutvalue!=undefined) || ($scope.cutvalue!=null)){
					$scope.label = $scope.cutvalue;
				}else{
					$scope.label = $scope.value;
				};
			};
			initLabel();

			$scope.clickURI = function (value){
				$window.open(value,'_newtab');
			}
		}
	}
}]);
