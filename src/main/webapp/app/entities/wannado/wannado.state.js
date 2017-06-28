(function() {
    'use strict';

    angular
        .module('sevakApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wannado', {
            parent: 'entity',
            url: '/wannado',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sevakApp.wannado.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wannado/wannados.html',
                    controller: 'WannadoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wannado');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('wannado-detail', {
            parent: 'wannado',
            url: '/wannado/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sevakApp.wannado.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wannado/wannado-detail.html',
                    controller: 'WannadoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wannado');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Wannado', function($stateParams, Wannado) {
                    return Wannado.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'wannado',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('wannado-detail.edit', {
            parent: 'wannado-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wannado/wannado-dialog.html',
                    controller: 'WannadoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Wannado', function(Wannado) {
                            return Wannado.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wannado.new', {
            parent: 'wannado',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wannado/wannado-dialog.html',
                    controller: 'WannadoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                opt1: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('wannado', null, { reload: 'wannado' });
                }, function() {
                    $state.go('wannado');
                });
            }]
        })
        .state('wannado.edit', {
            parent: 'wannado',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wannado/wannado-dialog.html',
                    controller: 'WannadoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Wannado', function(Wannado) {
                            return Wannado.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wannado', null, { reload: 'wannado' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wannado.delete', {
            parent: 'wannado',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wannado/wannado-delete-dialog.html',
                    controller: 'WannadoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Wannado', function(Wannado) {
                            return Wannado.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wannado', null, { reload: 'wannado' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
