(function() {
    'use strict';

    angular
        .module('taskstpApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tarefa', {
            parent: 'entity',
            url: '/tarefa?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tarefas'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tarefa/tarefas.html',
                    controller: 'TarefaController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('tarefa-detail', {
            parent: 'tarefa',
            url: '/tarefa/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tarefa'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tarefa/tarefa-detail.html',
                    controller: 'TarefaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Tarefa', function($stateParams, Tarefa) {
                    return Tarefa.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tarefa',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tarefa-detail.edit', {
            parent: 'tarefa-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tarefa/tarefa-dialog.html',
                    controller: 'TarefaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tarefa', function(Tarefa) {
                            return Tarefa.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tarefa.new', {
            parent: 'tarefa',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tarefa/tarefa-new.html',
                    //templateUrl: 'app/entities/tarefa/tarefa-dialog.html',
                    controller: 'TarefaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                descricao: null,
                                dataCriacao: null,
                                resposta: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tarefa', null, { reload: 'tarefa' });
                }, function() {
                    $state.go('tarefa');
                });
            }]
        })
        .state('tarefa.edit', {
            parent: 'tarefa',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tarefa/tarefa-dialog.html',
                    controller: 'TarefaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tarefa', function(Tarefa) {
                            return Tarefa.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tarefa', null, { reload: 'tarefa' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tarefa.delete', {
            parent: 'tarefa',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tarefa/tarefa-delete-dialog.html',
                    controller: 'TarefaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tarefa', function(Tarefa) {
                            return Tarefa.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tarefa', null, { reload: 'tarefa' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
