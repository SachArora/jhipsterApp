(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('SachuDialogController', SachuDialogController);

    SachuDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sachu'];

    function SachuDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Sachu) {
        var vm = this;

        vm.sachu = entity;
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
            if (vm.sachu.id !== null) {
                Sachu.update(vm.sachu, onSaveSuccess, onSaveError);
            } else {
                Sachu.save(vm.sachu, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sevakApp:sachuUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
