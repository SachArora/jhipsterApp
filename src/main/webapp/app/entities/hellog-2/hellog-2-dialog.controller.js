(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('Hellog2DialogController', Hellog2DialogController);

    Hellog2DialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Hellog2'];

    function Hellog2DialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Hellog2) {
        var vm = this;

        vm.hellog2 = entity;
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
            if (vm.hellog2.id !== null) {
                Hellog2.update(vm.hellog2, onSaveSuccess, onSaveError);
            } else {
                Hellog2.save(vm.hellog2, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sevakApp:hellog2Update', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
