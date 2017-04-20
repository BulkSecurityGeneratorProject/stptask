(function() {
    'use strict';

    angular
        .module('taskstpApp')
        .controller('TarefaDetailController', TarefaDetailController);

    TarefaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tarefa', 'User'];

    function TarefaDetailController($scope, $rootScope, $stateParams, previousState, entity, Tarefa, User) {
        var vm = this;

        vm.tarefa = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('taskstpApp:tarefaUpdate', function(event, result) {
            vm.tarefa = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
