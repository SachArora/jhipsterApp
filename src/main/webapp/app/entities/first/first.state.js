(function() {
    'use strict';

    angular
        .module('sevakApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('first', {
            parent: 'entity',
            url: '/first',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sevakApp.first.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/first/firsts.html',
                    controller: 'FirstController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('first');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('first-detail', {
            parent: 'first',
            url: '/first/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sevakApp.first.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/first/first-detail.html',
                    controller: 'FirstDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('first');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'First', function($stateParams, First) {
                    return First.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'first',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('first-detail.edit', {
            parent: 'first-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/first/first-dialog.html',
                    controller: 'FirstDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['First', function(First) {
                            return First.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('first.new', {
            parent: 'first',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/first/first-dialog.html',
                    controller: 'FirstDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                branch: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('first', null, { reload: 'first' });
                }, function() {
                    $state.go('first');
                });
            }]
        })
        .state('first.edit', {
            parent: 'first',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/first/first-dialog.html',
                    controller: 'FirstDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['First', function(First) {
                            return First.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('first', null, { reload: 'first' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('first.delete', {
            parent: 'first',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/first/first-delete-dialog.html',
                    controller: 'FirstDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['First', function(First) {
                            return First.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('first', null, { reload: 'first' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
