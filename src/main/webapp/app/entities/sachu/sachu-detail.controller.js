(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('SachuDetailController', SachuDetailController);

    SachuDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Sachu'];

    function SachuDetailController($scope, $rootScope, $stateParams, previousState, entity, Sachu) {
        var vm = this;

        vm.sachu = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sevakApp:sachuUpdate', function(event, result) {
            vm.sachu = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
