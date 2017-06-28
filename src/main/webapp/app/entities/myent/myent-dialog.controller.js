(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('MyentDialogController', MyentDialogController);

    MyentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Myent'];

    function MyentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Myent) {
        var vm = this;

        vm.myent = entity;
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
            if (vm.myent.id !== null) {
                Myent.update(vm.myent, onSaveSuccess, onSaveError);
            } else {
                Myent.save(vm.myent, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sevakApp:myentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
