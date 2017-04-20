(function() {
    'use strict';

    angular
        .module('taskstpApp')
        .controller('TarefaController', TarefaController);

    TarefaController.$inject = ['$scope','$state', 'Tarefa', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal'];

    function TarefaController($scope, $state, Tarefa, ParseLinks, AlertService, paginationConstants, pagingParams, Principal) {

        var vm = this;
        vm.tarefas=[];
        vm.account = null;
        vm.scope = $scope;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openDetail = openDetail;
        vm.showDetail = false;
        vm.pegarTarefa = pegarTarefa;
        vm.largarTarefa = largarTarefa;
        vm.salvarTarefa = salvarTarefa;
        vm.completarTarefa = completarTarefa;

        loadAll();
        getAccount();

        function loadAll () {
            Tarefa.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.tarefas = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
        function openDetail(tarefaSelecionada){
            console.log('ok aqui ' + tarefaSelecionada);
            vm.showDetail = true;
            vm.tarefaSelecionada = tarefaSelecionada;

        }
        function pegarTarefa(tarefa){
            console.log('pegar tarefa');
            console.log(tarefa);
            console.log(vm.account);
            tarefa.userId = vm.account.id;
            tarefa.userLogin = vm.account.login;
            Tarefa.update(tarefa, onSaveSuccess, onSaveError);

        }

        function onSaveSuccess(result){
            console.log(result);
            console.log('sucesso ao atualizar tarefa');
            $scope.$emit('taskstpApp:tarefaUpdate', result);
        }

        function onSaveError(){
            console.log('erro ao atualizar tarefa');
        }

        function largarTarefa(tarefa){
            tarefa.userId = null;
            tarefa.userLogin = null;
            Tarefa.update(tarefa, onSaveSuccess, onSaveError);
        }

        function salvarTarefa(tarefa){
            console.log('salvar tarefa');
            console.log(tarefa);
            Tarefa.update(tarefa, onSaveSuccess, onSaveError);
        }

        function completarTarefa(tarefa){
            console.log('completar tarefa');
            console.log(tarefa);
            tarefa.status="FECHADA";
            Tarefa.update(tarefa, onSaveSuccess, onSaveError);
        }

         function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
             });
          }



    }
})();
