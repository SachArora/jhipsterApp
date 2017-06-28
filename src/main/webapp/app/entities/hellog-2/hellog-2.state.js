(function() {
    'use strict';

    angular
        .module('sevakApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('hellog-2', {
            parent: 'entity',
            url: '/hellog-2',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sevakApp.hellog2.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hellog-2/hellog-2-s.html',
                    controller: 'Hellog2Controller',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hellog2');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('hellog-2-detail', {
            parent: 'hellog-2',
            url: '/hellog-2/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sevakApp.hellog2.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hellog-2/hellog-2-detail.html',
                    controller: 'Hellog2DetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hellog2');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Hellog2', function($stateParams, Hellog2) {
                    return Hellog2.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'hellog-2',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('hellog-2-detail.edit', {
            parent: 'hellog-2-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hellog-2/hellog-2-dialog.html',
                    controller: 'Hellog2DialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Hellog2', function(Hellog2) {
                            return Hellog2.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hellog-2.new', {
            parent: 'hellog-2',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hellog-2/hellog-2-dialog.html',
                    controller: 'Hellog2DialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                def: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('hellog-2', null, { reload: 'hellog-2' });
                }, function() {
                    $state.go('hellog-2');
                });
            }]
        })
        .state('hellog-2.edit', {
            parent: 'hellog-2',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hellog-2/hellog-2-dialog.html',
                    controller: 'Hellog2DialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Hellog2', function(Hellog2) {
                            return Hellog2.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hellog-2', null, { reload: 'hellog-2' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hellog-2.delete', {
            parent: 'hellog-2',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hellog-2/hellog-2-delete-dialog.html',
                    controller: 'Hellog2DeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Hellog2', function(Hellog2) {
                            return Hellog2.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hellog-2', null, { reload: 'hellog-2' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
