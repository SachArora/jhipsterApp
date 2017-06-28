(function() {
    'use strict';
    angular
        .module('sevakApp')
        .factory('Wannado', Wannado);

    Wannado.$inject = ['$resource'];

    function Wannado ($resource) {
        var resourceUrl =  'api/wannados/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
