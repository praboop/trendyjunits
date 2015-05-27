var gridApp = angular.module("GridApp",[]);

gridApp.controller('GridControl', function ($scope, $http) {
	$scope.name = "Test Results";
	
	$http.get('results.json').success(function(data){
         $scope.tableData = data;
         $scope.Math = window.Math;
         $scope.rowClicked = function(rowName) {
        	 $("tr[name=" + rowName + "]").toggleClass("visibleDiv hiddenDiv");
         }
    });
	

	
	$scope.myFilter = function(query) {
		return function() {
			alert("Filter got invoked");
			return true;
		}
	};
});