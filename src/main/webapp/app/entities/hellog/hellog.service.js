(function() {
    'use strict';
    angular
        .module('sevakApp')
        .factory('Hellog', Hellog);

    Hellog.$inject = ['$resource'];

    function Hellog ($resource) {
        var resourceUrl =  'api/hellogs/:id';

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
