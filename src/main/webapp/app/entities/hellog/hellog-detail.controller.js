(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('HellogDetailController', HellogDetailController);

    HellogDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Hellog'];

    function HellogDetailController($scope, $rootScope, $stateParams, previousState, entity, Hellog) {
        var vm = this;

        vm.hellog = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sevakApp:hellogUpdate', function(event, result) {
            vm.hellog = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
