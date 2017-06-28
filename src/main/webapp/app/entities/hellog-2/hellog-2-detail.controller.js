(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('Hellog2DetailController', Hellog2DetailController);

    Hellog2DetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Hellog2'];

    function Hellog2DetailController($scope, $rootScope, $stateParams, previousState, entity, Hellog2) {
        var vm = this;

        vm.hellog2 = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sevakApp:hellog2Update', function(event, result) {
            vm.hellog2 = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
