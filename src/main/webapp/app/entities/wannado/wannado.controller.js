(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('WannadoController', WannadoController);

    WannadoController.$inject = ['Wannado'];

    function WannadoController(Wannado) {

        var vm = this;

        vm.wannados = [];

        loadAll();

        function loadAll() {
            Wannado.query(function(result) {
                vm.wannados = result;
                vm.searchQuery = null;
            });
        }
    }
})();
