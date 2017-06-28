(function() {
    'use strict';

    angular
        .module('sevakApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('again', {
            parent: 'entity',
            url: '/again',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sevakApp.again.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/again/agains.html',
                    controller: 'AgainController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('again');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('again-detail', {
            parent: 'again',
            url: '/again/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'sevakApp.again.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/again/again-detail.html',
                    controller: 'AgainDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('again');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Again', function($stateParams, Again) {
                    return Again.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'again',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('again-detail.edit', {
            parent: 'again-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/again/again-dialog.html',
                    controller: 'AgainDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Again', function(Again) {
                            return Again.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('again.new', {
            parent: 'again',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/again/again-dialog.html',
                    controller: 'AgainDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                oneatt: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('again', null, { reload: 'again' });
                }, function() {
                    $state.go('again');
                });
            }]
        })
        .state('again.edit', {
            parent: 'again',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/again/again-dialog.html',
                    controller: 'AgainDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Again', function(Again) {
                            return Again.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('again', null, { reload: 'again' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('again.delete', {
            parent: 'again',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/again/again-delete-dialog.html',
                    controller: 'AgainDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Again', function(Again) {
                            return Again.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('again', null, { reload: 'again' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
