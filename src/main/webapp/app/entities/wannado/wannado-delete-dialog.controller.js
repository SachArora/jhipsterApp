(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('WannadoDeleteController',WannadoDeleteController);

    WannadoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Wannado'];

    function WannadoDeleteController($uibModalInstance, entity, Wannado) {
        var vm = this;

        vm.wannado = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Wannado.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
