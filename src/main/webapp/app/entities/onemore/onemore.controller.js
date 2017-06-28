(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('OnemoreController', OnemoreController);

    OnemoreController.$inject = ['Onemore'];

    function OnemoreController(Onemore) {

        var vm = this;

        vm.onemores = [];

        loadAll();

        function loadAll() {
            Onemore.query(function(result) {
                vm.onemores = result;
                vm.searchQuery = null;
            });
        }
    }
})();
