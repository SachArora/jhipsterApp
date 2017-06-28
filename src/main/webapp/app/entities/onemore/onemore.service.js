(function() {
    'use strict';
    angular
        .module('sevakApp')
        .factory('Onemore', Onemore);

    Onemore.$inject = ['$resource'];

    function Onemore ($resource) {
        var resourceUrl =  'api/onemores/:id';

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
