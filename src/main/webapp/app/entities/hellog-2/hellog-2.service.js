(function() {
    'use strict';
    angular
        .module('sevakApp')
        .factory('Hellog2', Hellog2);

    Hellog2.$inject = ['$resource'];

    function Hellog2 ($resource) {
        var resourceUrl =  'api/hellog-2-s/:id';

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
