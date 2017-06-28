(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('AgainController', AgainController);

    AgainController.$inject = ['Again'];

    function AgainController(Again) {

        var vm = this;

        vm.agains = [];

        loadAll();

        function loadAll() {
            Again.query(function(result) {
                vm.agains = result;
                vm.searchQuery = null;
            });
        }
    }
})();
