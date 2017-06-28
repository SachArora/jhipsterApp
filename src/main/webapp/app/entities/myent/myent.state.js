(function() {
    'use strict';

    angular
        .module('sevakApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('myent', {
            parent: 'entity',
            url: '/myent',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sevakApp.myent.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/myent/myents.html',
                    controller: 'MyentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('myent');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('myent-detail', {
            parent: 'myent',
            url: '/myent/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sevakApp.myent.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/myent/myent-detail.html',
                    controller: 'MyentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('myent');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Myent', function($stateParams, Myent) {
                    return Myent.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'myent',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('myent-detail.edit', {
            parent: 'myent-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/myent/myent-dialog.html',
                    controller: 'MyentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Myent', function(Myent) {
                            return Myent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('myent.new', {
            parent: 'myent',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/myent/myent-dialog.html',
                    controller: 'MyentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('myent', null, { reload: 'myent' });
                }, function() {
                    $state.go('myent');
                });
            }]
        })
        .state('myent.edit', {
            parent: 'myent',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/myent/myent-dialog.html',
                    controller: 'MyentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Myent', function(Myent) {
                            return Myent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('myent', null, { reload: 'myent' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('myent.delete', {
            parent: 'myent',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/myent/myent-delete-dialog.html',
                    controller: 'MyentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Myent', function(Myent) {
                            return Myent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('myent', null, { reload: 'myent' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
