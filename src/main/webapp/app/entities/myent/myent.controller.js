(function() {
    'use strict';

    angular
        .module('sevakApp')
        .controller('MyentController', MyentController);

    MyentController.$inject = ['Myent'];

    function MyentController(Myent) {

        var vm = this;

        vm.myents = [];

        loadAll();

        function loadAll() {
            Myent.query(function(result) {
                vm.myents = result;
                vm.searchQuery = null;
            });
        }
    }
})();
