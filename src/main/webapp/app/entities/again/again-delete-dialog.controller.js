(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('AgainDeleteController',AgainDeleteController);

    AgainDeleteController.$inject = ['$uibModalInstance', 'entity', 'Again'];

    function AgainDeleteController($uibModalInstance, entity, Again) {
        var vm = this;

        vm.again = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Again.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
