(function() {
    'use strict';

    angular
        .module('taskstpApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tarefa-my-suffix', {
            parent: 'entity',
            url: '/tarefa-my-suffix?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tarefas'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tarefa/tarefasmySuffix.html',
                    controller: 'TarefaMySuffixController',
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
        .state('tarefa-my-suffix-detail', {
            parent: 'tarefa-my-suffix',
            url: '/tarefa-my-suffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tarefa'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tarefa/tarefa-my-suffix-detail.html',
                    controller: 'TarefaMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Tarefa', function($stateParams, Tarefa) {
                    return Tarefa.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tarefa-my-suffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tarefa-my-suffix-detail.edit', {
            parent: 'tarefa-my-suffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tarefa/tarefa-my-suffix-dialog.html',
                    controller: 'TarefaMySuffixDialogController',
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
        .state('tarefa-my-suffix.new', {
            parent: 'tarefa-my-suffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tarefa/tarefa-my-suffix-dialog.html',
                    controller: 'TarefaMySuffixDialogController',
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
                    $state.go('tarefa-my-suffix', null, { reload: 'tarefa-my-suffix' });
                }, function() {
                    $state.go('tarefa-my-suffix');
                });
            }]
        })
        .state('tarefa-my-suffix.edit', {
            parent: 'tarefa-my-suffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tarefa/tarefa-my-suffix-dialog.html',
                    controller: 'TarefaMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tarefa', function(Tarefa) {
                            return Tarefa.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tarefa-my-suffix', null, { reload: 'tarefa-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tarefa-my-suffix.delete', {
            parent: 'tarefa-my-suffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tarefa/tarefa-my-suffix-delete-dialog.html',
                    controller: 'TarefaMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tarefa', function(Tarefa) {
                            return Tarefa.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tarefa-my-suffix', null, { reload: 'tarefa-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
