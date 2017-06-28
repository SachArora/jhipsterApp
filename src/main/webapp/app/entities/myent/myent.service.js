(function() {
    'use strict';
    angular
        .module('sevakApp')
        .factory('Myent', Myent);

    Myent.$inject = ['$resource'];

    function Myent ($resource) {
        var resourceUrl =  'api/myents/:id';

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
