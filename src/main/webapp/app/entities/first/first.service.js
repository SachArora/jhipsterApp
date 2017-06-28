(function() {
    'use strict';
    angular
        .module('sevakApp')
        .factory('First', First);

    First.$inject = ['$resource'];

    function First ($resource) {
        var resourceUrl =  'api/firsts/:id';

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
