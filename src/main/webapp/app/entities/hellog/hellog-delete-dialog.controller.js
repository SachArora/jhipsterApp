(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('HellogDeleteController',HellogDeleteController);

    HellogDeleteController.$inject = ['$uibModalInstance', 'entity', 'Hellog'];

    function HellogDeleteController($uibModalInstance, entity, Hellog) {
        var vm = this;

        vm.hellog = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Hellog.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
