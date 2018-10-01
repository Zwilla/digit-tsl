digitTslWeb.controller('newsController',['$scope','$modal','httpFactory','$q','$filter','showModal','appConstant',
       function ($scope,$modal,httpFactory,$q,$filter,showModal,appConstant){

            $scope.loadingNews = appConstant.newsController.loading_news;
            $scope.load = false;
            
            var initCriteria = function(){
                httpFactory.get("/api/audit/criteriaList",$scope.auditController_criteria_loading_error).then(function(criterias) {
                    $scope.criterias = criterias;
                });
            };
            initCriteria();
            
            $scope.getNews = function(){
                httpFactory.post("/api/audit/all_news",$scope.auditSearch,$scope.auditController_search_error).then(function(datas) {
                    initTable(datas);
                })
                .finally(function(){
                    $scope.load=true;
                });
            }
            
            var resetSearchDTO = function(){
                var lastWeek = new Date();
                lastWeek.setDate(lastWeek.getDate() - 7);
                
                $scope.auditSearch = {
                        countryCode : "",
                        target : null,
                        action : null,
                        startDate : lastWeek,
                        endDate : new Date(),
                        maxResult : 0
                };
            }
            
            initNews = function(){
                resetSearchDTO();
                $scope.getNews();
            }
            initNews();
            
             $scope.resetSearch = function(){
                 resetSearchDTO();
                 $scope.getNews();
             };
            
             /**
              * Init table options
              */
             var initTable = function(datas){
                 var head = [
                     {label : "", size : "100px;"},
                     {label : "", size : "100px;"},
                     {label : "", size : "800px;"},
                 ];
                 
                 var list = [];
                 for(var i=0;i<datas.length;i++){
                     var obj = new Object();
                     obj.date = $filter('date')(datas[i].date, "yyyy-MM-dd");
                     obj.time = $filter('date')(datas[i].date, "HH:mm:ss");
                     obj.infos = datas[i].infos +": "+"<a ng-href='/tl-manager/tl/"+datas[i].tlId+"' class='cursor-pointer'>"+datas[i].tlInfo+"</a>.";
                     list.push(obj);
                 }
                 $scope.tableOptions = {
                     nbItemPerPage : 20,
                     listObj : list,
                     thead : head,
                     displayHeading : false
                 };
             };
             
             
             /**
              * Calendar
              */
             $scope.open = function($event) {
                 $event.preventDefault();
                 $event.stopPropagation();
                 $scope.opened = true;
             };
             
             $scope.openEnd = function($event) {
                 $event.preventDefault();
                 $event.stopPropagation();
                 $scope.openedEnd = true;
             };
        

}]);