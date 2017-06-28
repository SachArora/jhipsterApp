(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('AgainDialogController', AgainDialogController);

    AgainDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Again'];

    function AgainDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Again) {
        var vm = this;

        vm.again = entity;
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
            if (vm.again.id !== null) {
                Again.update(vm.again, onSaveSuccess, onSaveError);
            } else {
                Again.save(vm.again, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sevakApp:againUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
