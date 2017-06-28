(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('OnemoreDialogController', OnemoreDialogController);

    OnemoreDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Onemore'];

    function OnemoreDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Onemore) {
        var vm = this;

        vm.onemore = entity;
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
            if (vm.onemore.id !== null) {
                Onemore.update(vm.onemore, onSaveSuccess, onSaveError);
            } else {
                Onemore.save(vm.onemore, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sevakApp:onemoreUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
