(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('SachuController', SachuController);

    SachuController.$inject = ['Sachu'];

    function SachuController(Sachu) {

        var vm = this;

        vm.sachus = [];

        loadAll();

        function loadAll() {
            Sachu.query(function(result) {
                vm.sachus = result;
                vm.searchQuery = null;
            });
        }
    }
})();
