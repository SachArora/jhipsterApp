(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('HellogDialogController', HellogDialogController);

    HellogDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Hellog'];

    function HellogDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Hellog) {
        var vm = this;

        vm.hellog = entity;
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
            if (vm.hellog.id !== null) {
                Hellog.update(vm.hellog, onSaveSuccess, onSaveError);
            } else {
                Hellog.save(vm.hellog, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sevakApp:hellogUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
