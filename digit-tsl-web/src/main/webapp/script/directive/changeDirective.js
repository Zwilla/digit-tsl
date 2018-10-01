/**
 * Change : display change icon & open detail in modal on click
 * Type : html tag
 * Parameter :
 ** id - string : id of current node/element
 ** listdiff - array[object] : list of changes
 ** label - string : modal title
 ** istreeparent - boolean : show/hide child changes on node
 */
digitTslWeb.directive('change',[ '$modal','$filter', function($modal,$filter){
	return {
		restrict: 'EA',
        replace: true,
        transclude: true,
        scope : {
        	id: '=',
        	listdiff : '=',
        	label : '=',
        	istreeparent : '@',
        	information : '='
        },
		template : '<span ng-click="changeModal(id,listdiff,label,istreeparent,information)" ng-class="style" ng-id="id"></span>',
		link : function($scope,element,attrs){
		    $scope.istreeparent = $scope.istreeparent=="true";

			/**
			 * Show icon
			 * Return css class to be used
			 **/
			var changeGlyphShow = function(id,listdiff,istreeparent){
			    $scope.style="invisible";
			    if(!listdiff || !id){
			        return;
			    };

				for(var i =0;i<listdiff.length;i++){
					if(isInList(listdiff[i],id,istreeparent)){
					    $scope.style = "fa fa-exchange changeColor cursor-pointer";
					    return;
					};
				};
			};
			changeGlyphShow($scope.id,$scope.listdiff,$scope.istreeparent)


			/**
			 * Prevent reloading except when list change
			 */
			$scope.$watch("listdiff", function() {
				changeGlyphShow($scope.id,$scope.listdiff,$scope.istreeparent);
		    },true);


			/**
			 * Init: change for current node
			 * Then: Call modal Change
			 */
			$scope.changeModal = function(id,listdiff,label,istreeparent,information){
			    if(!listdiff || !id){
                    return;
                };

			    var currentList = [];
				for(var i =0;i<listdiff.length;i++){
					if(isInList(listdiff[i],id,istreeparent)){
						currentList.push(listdiff[i]);
					};
				};

				var changeModalInstance = $modal.open({
					templateUrl: 'modalChange',
					size : 'lg',
					backdrop : 'static',
					windowClass : 'modal-change',
					controller: modalChangeDirectiveController,
					resolve : {
						list : function() {
							return currentList;
						},
						label : function() {
							return $scope.label;
						},
						information : function(){
						    return information;
						}
					}
				});
			};
		}
	}
}]);