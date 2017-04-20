(function() {
    'use strict';
    angular
        .module('taskstpApp')
        .factory('Tarefa', Tarefa);

    Tarefa.$inject = ['$resource', 'DateUtils'];

    function Tarefa ($resource, DateUtils) {
        var resourceUrl =  'api/tarefas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dataCriacao = DateUtils.convertDateTimeFromServer(data.dataCriacao);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
