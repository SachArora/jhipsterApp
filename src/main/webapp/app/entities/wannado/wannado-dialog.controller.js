(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('WannadoDialogController', WannadoDialogController);

    WannadoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Wannado'];

    function WannadoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Wannado) {
        var vm = this;

        vm.wannado = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.wannado.id !== null) {
                Wannado.update(vm.wannado, onSaveSuccess, onSaveError);
            } else {
                Wannado.save(vm.wannado, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sevakApp:wannadoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
