(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('FirstDeleteController',FirstDeleteController);

    FirstDeleteController.$inject = ['$uibModalInstance', 'entity', 'First'];

    function FirstDeleteController($uibModalInstance, entity, First) {
        var vm = this;

        vm.first = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            First.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
