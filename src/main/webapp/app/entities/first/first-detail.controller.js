(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('FirstDetailController', FirstDetailController);

    FirstDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'First'];

    function FirstDetailController($scope, $rootScope, $stateParams, previousState, entity, First) {
        var vm = this;

        vm.first = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sevakApp:firstUpdate', function(event, result) {
            vm.first = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
