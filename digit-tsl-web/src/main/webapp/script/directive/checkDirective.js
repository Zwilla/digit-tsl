/**
 * Check : display check icon & open detail in modal on click
 * Type : html tag
 * Parameter :
 ** id - string : id of current node/element
 ** listcheck - array[object] : list of check
 ** label - string : modal title
 ** istreeparent - boolean : show/hide child check on node
 */
digitTslWeb.directive('check',[ '$modal','$filter','$document', function($modal,$filter,$document){
	return {
		restrict: 'EA',
        replace: true,
        transclude: true,
        scope : {
        	id: '=',
        	listcheck : '=',
        	label : '=',
        	istreeparent : '@',
        	disabled : '=',

        },
		template: '<div ng-click="checkModal(label,istreeparent)" ng-bind-html="iconSanitize" ng-class="style" ng-id="id" ng-disabled="disabled"></div>',
		link : function($scope,element,attrs){
			$scope.currentCheck = [];
			$scope.istreeparent = $scope.istreeparent=="true";

			/**
			 * Show icon
			 * Return an "html" sanitized in modal check
			 **/
			var checkGlyphShow = function(id,listcheck,istreeparent){
				if(!listcheck || !id){
				    return;
				};

				$scope.style="invisible";
				for(var i =0;i<listcheck.length;i++){
					if(isInList(listcheck[i],id,istreeparent)){
					    if($scope.disabled!=undefined && !$scope.disabled){
					        $scope.style="check-directive-disabled";
					    }else{
					        $scope.style="check-directive";
					    }
						if(listcheck[i].status=="ERROR"){
							$scope.iconSanitize =
								'<span class="fa-stack cursor-pointer marginL-1">'+
				    				'<i class="fa fa-circle fa-stack-1x white" ></i>'+
				    				'<span class="fa fa-exclamation-circle fa-stack-1x errorColor"></span>'+
				    			'</span>';
							return;
						}else if(listcheck[i].status=="WARNING"){
							$scope.iconSanitize = "<span class='fa fa-exclamation-triangle warnColor cursor-pointer'></span>"
						}else{
							$scope.iconSanitize =
								'<span class="fa-stack cursor-pointer marginL-1">'+
				    				'<i class="fa fa-circle fa-stack-1x white" ></i>'+
				    				'<span class="fa fa-question-circle fa-stack-1x infoColor"></span>'+
				    			'</span>';
						}
					};
				};
			};
			checkGlyphShow($scope.id,$scope.listcheck,$scope.istreeparent);

			/**
			 * Prevent reloading except when list change
			 */
			$scope.$watch("listcheck", function() {
				checkGlyphShow($scope.id,$scope.listcheck,$scope.istreeparent);
		    },true);

			/**
			 * Init: Iterate on all list & get current node checks
			 **/
			var initCurrentCheck = function (id,listcheck,istreeparent){
			    if(!listcheck || !id){
                    return;
                };

				$scope.currentCheck = [];
				for(var i =0;i<listcheck.length;i++){
					if(isInList(listcheck[i],id,istreeparent)){
						$scope.currentCheck.push(listcheck[i]);
					};
				};
			};

			/**
			 * Call modal check
			 */
			$scope.checkModal = function(label,istreeparent){
				initCurrentCheck($scope.id,$scope.listcheck,$scope.istreeparent);
				var checkModalInstance = $modal.open({
					templateUrl: 'modalCheck',
					size : 'lg',
					backdrop : 'static',
					windowClass : 'modal-check',
					controller: modalCheckDirectiveController,
					resolve : {
						list : function() {
							return $scope.currentCheck;
						},
						label : function() {
							return $scope.label;
						}
					}
				});
			};

		}
	}
}]);