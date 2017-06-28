(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('OnemoreDeleteController',OnemoreDeleteController);

    OnemoreDeleteController.$inject = ['$uibModalInstance', 'entity', 'Onemore'];

    function OnemoreDeleteController($uibModalInstance, entity, Onemore) {
        var vm = this;

        vm.onemore = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Onemore.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
