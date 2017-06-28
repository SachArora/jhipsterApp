(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('Hellog2DeleteController',Hellog2DeleteController);

    Hellog2DeleteController.$inject = ['$uibModalInstance', 'entity', 'Hellog2'];

    function Hellog2DeleteController($uibModalInstance, entity, Hellog2) {
        var vm = this;

        vm.hellog2 = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Hellog2.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
