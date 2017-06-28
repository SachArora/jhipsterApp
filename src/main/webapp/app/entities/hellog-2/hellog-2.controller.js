(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('Hellog2Controller', Hellog2Controller);

    Hellog2Controller.$inject = ['Hellog2'];

    function Hellog2Controller(Hellog2) {

        var vm = this;

        vm.hellog2S = [];

        loadAll();

        function loadAll() {
            Hellog2.query(function(result) {
                vm.hellog2S = result;
                vm.searchQuery = null;
            });
        }
    }
})();
