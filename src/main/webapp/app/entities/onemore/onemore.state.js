(function() {
    'use strict';

    angular
        .module('sevakApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('onemore', {
            parent: 'entity',
            url: '/onemore',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sevakApp.onemore.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/onemore/onemores.html',
                    controller: 'OnemoreController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('onemore');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('onemore-detail', {
            parent: 'onemore',
            url: '/onemore/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sevakApp.onemore.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/onemore/onemore-detail.html',
                    controller: 'OnemoreDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('onemore');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Onemore', function($stateParams, Onemore) {
                    return Onemore.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'onemore',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('onemore-detail.edit', {
            parent: 'onemore-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/onemore/onemore-dialog.html',
                    controller: 'OnemoreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Onemore', function(Onemore) {
                            return Onemore.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('onemore.new', {
            parent: 'onemore',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/onemore/onemore-dialog.html',
                    controller: 'OnemoreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dept: null,
                                block: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('onemore', null, { reload: 'onemore' });
                }, function() {
                    $state.go('onemore');
                });
            }]
        })
        .state('onemore.edit', {
            parent: 'onemore',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/onemore/onemore-dialog.html',
                    controller: 'OnemoreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Onemore', function(Onemore) {
                            return Onemore.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('onemore', null, { reload: 'onemore' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('onemore.delete', {
            parent: 'onemore',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/onemore/onemore-delete-dialog.html',
                    controller: 'OnemoreDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Onemore', function(Onemore) {
                            return Onemore.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('onemore', null, { reload: 'onemore' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
