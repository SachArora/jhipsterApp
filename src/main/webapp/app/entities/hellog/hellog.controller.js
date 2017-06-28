(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('HellogController', HellogController);

    HellogController.$inject = ['Hellog'];

    function HellogController(Hellog) {

        var vm = this;

        vm.hellogs = [];

        loadAll();

        function loadAll() {
            Hellog.query(function(result) {
                vm.hellogs = result;
                vm.searchQuery = null;
            });
        }
    }
})();
