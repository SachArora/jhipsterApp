(function() {
    'use strict';

    angular
        .module('sevakApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sachu', {
            parent: 'entity',
            url: '/sachu',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sevakApp.sachu.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sachu/sachus.html',
                    controller: 'SachuController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sachu');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sachu-detail', {
            parent: 'sachu',
            url: '/sachu/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sevakApp.sachu.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sachu/sachu-detail.html',
                    controller: 'SachuDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sachu');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Sachu', function($stateParams, Sachu) {
                    return Sachu.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sachu',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sachu-detail.edit', {
            parent: 'sachu-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sachu/sachu-dialog.html',
                    controller: 'SachuDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sachu', function(Sachu) {
                            return Sachu.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sachu.new', {
            parent: 'sachu',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sachu/sachu-dialog.html',
                    controller: 'SachuDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                friend_name: null,
                                friend_from: null,
                                roll_no: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sachu', null, { reload: 'sachu' });
                }, function() {
                    $state.go('sachu');
                });
            }]
        })
        .state('sachu.edit', {
            parent: 'sachu',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sachu/sachu-dialog.html',
                    controller: 'SachuDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sachu', function(Sachu) {
                            return Sachu.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sachu', null, { reload: 'sachu' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sachu.delete', {
            parent: 'sachu',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sachu/sachu-delete-dialog.html',
                    controller: 'SachuDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Sachu', function(Sachu) {
                            return Sachu.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sachu', null, { reload: 'sachu' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
