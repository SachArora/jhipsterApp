(function() {
    'use strict';

    angular
        .module('sevakApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('hellog', {
            parent: 'entity',
            url: '/hellog',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sevakApp.hellog.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hellog/hellogs.html',
                    controller: 'HellogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hellog');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('hellog-detail', {
            parent: 'hellog',
            url: '/hellog/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sevakApp.hellog.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hellog/hellog-detail.html',
                    controller: 'HellogDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hellog');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Hellog', function($stateParams, Hellog) {
                    return Hellog.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'hellog',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('hellog-detail.edit', {
            parent: 'hellog-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hellog/hellog-dialog.html',
                    controller: 'HellogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Hellog', function(Hellog) {
                            return Hellog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hellog.new', {
            parent: 'hellog',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hellog/hellog-dialog.html',
                    controller: 'HellogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                abc: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('hellog', null, { reload: 'hellog' });
                }, function() {
                    $state.go('hellog');
                });
            }]
        })
        .state('hellog.edit', {
            parent: 'hellog',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hellog/hellog-dialog.html',
                    controller: 'HellogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Hellog', function(Hellog) {
                            return Hellog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hellog', null, { reload: 'hellog' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hellog.delete', {
            parent: 'hellog',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hellog/hellog-delete-dialog.html',
                    controller: 'HellogDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Hellog', function(Hellog) {
                            return Hellog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hellog', null, { reload: 'hellog' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
