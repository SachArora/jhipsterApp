(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('AgainDetailController', AgainDetailController);

    AgainDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Again'];

    function AgainDetailController($scope, $rootScope, $stateParams, previousState, entity, Again) {
        var vm = this;

        vm.again = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sevakApp:againUpdate', function(event, result) {
            vm.again = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
