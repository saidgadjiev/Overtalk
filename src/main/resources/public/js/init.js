var as = angular.module('OverTalkApp', ['ngRoute', 'ngMessages', 'ui.bootstrap', 'OverTalkApp.controllers', 'OverTalkApp.services', 'OverTalkApp.directives']);

as.constant('USER_ROLES', {
    admin: 'ROLE_ADMIN',
    user: 'ROLE_USER'
});

as.constant('AUTH_EVENTS', {
    signUpSuccess: 'auth-signUp-success',
    signInSuccess: 'auth-signIn-success',
    signInFailed: 'auth-signIn-failed',
    signOutSuccess: 'auth-signOut-success',
    accessDenied: '403-access-denied',
    unauthorized: '401-unauthorized'
});

as.constant('RESPONSE_ERROR_EVENTS', {
    internalServerError: 'internal-server-error'
});

as.factory('AuthInterceptor', function ($rootScope, $q, AUTH_EVENTS) {
    return {
        'response': function (response) {
            console.log(response);

            return response;
        },
        'responseError': function (response) {
            console.log(response);

            if (response.status === 401) {
                $rootScope.$broadcast(AUTH_EVENTS.unauthorized);
            } else if (response.status === 403) {
                $rootScope.$broadcast(AUTH_EVENTS.accessDenied);
            }

            return $q.reject(response);
        }
    };
});

as.config(function ($routeProvider, $httpProvider) {
    $routeProvider.when('/', {
        templateUrl: 'html/main.html',
        publicAccess: true
    }).when('/signIn', {
        templateUrl: 'html/signIn.html',
        controller: 'LoginController'
    }).when('/posts', {
        templateUrl: 'html/posts/posts.html',
        controller: 'PostsController'
    }).when('/posts/new', {
        templateUrl: 'html/posts/new.html',
        controller: 'NewPostController'
    }).when('/posts/:id', {
        templateUrl: 'html/posts/details.html',
        controller: 'DetailsController'
    }).when('/signUp', {
        templateUrl: 'html/signUp.html',
        controller: 'RegistrationController'
    }).when('/users', {
        templateUrl: 'html/users.html',
        controller: 'UsersController'
    }).when('/projects', {
        templateUrl: 'html/projects.html',
        controller: 'ProjectsController'
    }).when('/403', {
        templateUrl: 'html/error/403.html',
        publicAccess: true
    }).when('/404', {
        templateUrl: 'html/error/404.html',
        publicAccess: true
    }).when('/500', {
        templateUrl: 'html/error/500.html',
        publicAccess: true
    }).otherwise({
        redirectTo: '/404'
    });
    $httpProvider.interceptors.push('AuthInterceptor');
});

as.run(function ($rootScope, $location, $log, LocationService, AUTH_EVENTS, RESPONSE_ERROR_EVENTS) {
    $rootScope.$on(AUTH_EVENTS.unauthorized, function () {
        LocationService.saveLocation();
        $location.path('/signIn');
    });
    $rootScope.$on(AUTH_EVENTS.accessDenied, function () {
        $location.path('/403');
    });
    $rootScope.$on(AUTH_EVENTS.signInSuccess, function () {
        LocationService.gotoLast();
    });
    $rootScope.$on(RESPONSE_ERROR_EVENTS.internalServerError, function () {
        $location.path('/500');
    });
});
