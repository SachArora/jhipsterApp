(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('FirstController', FirstController);

    FirstController.$inject = ['First'];

    function FirstController(First) {

        var vm = this;

        vm.firsts = [];

        loadAll();

        function loadAll() {
            First.query(function(result) {
                vm.firsts = result;
                vm.searchQuery = null;
            });
        }
    }
})();
