(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('FirstDialogController', FirstDialogController);

    FirstDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'First'];

    function FirstDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, First) {
        var vm = this;

        vm.first = entity;
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
            if (vm.first.id !== null) {
                First.update(vm.first, onSaveSuccess, onSaveError);
            } else {
                First.save(vm.first, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sevakApp:firstUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
