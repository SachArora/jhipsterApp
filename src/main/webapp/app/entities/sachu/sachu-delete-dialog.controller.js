(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('SachuDeleteController',SachuDeleteController);

    SachuDeleteController.$inject = ['$uibModalInstance', 'entity', 'Sachu'];

    function SachuDeleteController($uibModalInstance, entity, Sachu) {
        var vm = this;

        vm.sachu = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Sachu.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
