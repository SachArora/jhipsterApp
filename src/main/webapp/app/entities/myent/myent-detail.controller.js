(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('MyentDetailController', MyentDetailController);

    MyentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Myent'];

    function MyentDetailController($scope, $rootScope, $stateParams, previousState, entity, Myent) {
        var vm = this;

        vm.myent = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sevakApp:myentUpdate', function(event, result) {
            vm.myent = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
