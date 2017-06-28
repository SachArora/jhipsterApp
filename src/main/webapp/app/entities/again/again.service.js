(function() {
    'use strict';
    angular
        .module('sevakApp')
        .factory('Again', Again);

    Again.$inject = ['$resource'];

    function Again ($resource) {
        var resourceUrl =  'api/agains/:id';

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
