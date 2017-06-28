(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('OnemoreDetailController', OnemoreDetailController);

    OnemoreDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Onemore'];

    function OnemoreDetailController($scope, $rootScope, $stateParams, previousState, entity, Onemore) {
        var vm = this;

        vm.onemore = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sevakApp:onemoreUpdate', function(event, result) {
            vm.onemore = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
