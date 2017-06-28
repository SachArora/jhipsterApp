(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('MyentDeleteController',MyentDeleteController);

    MyentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Myent'];

    function MyentDeleteController($uibModalInstance, entity, Myent) {
        var vm = this;

        vm.myent = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Myent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
