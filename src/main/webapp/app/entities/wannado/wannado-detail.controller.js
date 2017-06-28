(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('WannadoDetailController', WannadoDetailController);

    WannadoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Wannado'];

    function WannadoDetailController($scope, $rootScope, $stateParams, previousState, entity, Wannado) {
        var vm = this;

        vm.wannado = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sevakApp:wannadoUpdate', function(event, result) {
            vm.wannado = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
