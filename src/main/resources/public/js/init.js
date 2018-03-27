var as = angular.module('OverTalkApp', ['ngRoute', 'ui.bootstrap', 'OverTalkApp.controllers', 'OverTalkApp.services']);

as.constant('USER_ROLES', {
    admin: 'ROLE_ADMIN',
    user: 'ROLE_USER'
});

as.constant('AUTH_EVENTS', {
    signUpSuccess: 'auth-signUp-success',
    signInSuccess: 'auth-signIn-success',
    signInFailed: '',
    signOutSuccess: 'auth-signOut-success',
    accessDenied: '403-access-denied',
    unauthorized: '401-unauthorized'
});

as.factory('AuthInterceptor', function ($q) {
    return {
        'response': function (response) {
            console.log(response);

            return response;
        },
        'responseError': function (response) {
            console.log(response);

            return $q.reject(response);
        }
    };
});

as.config(function ($routeProvider, $httpProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'html/signIn.html',
            controller: 'LoginController'
        })
        .when('/main', {
            templateUrl: 'html/main.html',
            publicAccess: true
        })
        .when('/signIn', {
            templateUrl: 'html/signIn.html',
            controller: 'LoginController'
        })
        .when('/posts', {
            templateUrl: 'html/posts/posts.html',
            controller: 'PostsController'
        }).when('/posts/new', {
        templateUrl: 'html/posts/new.html',
        controller: 'NewPostController'
    })
        .when('/posts/:id', {
            templateUrl: 'html/posts/details.html',
            controller: 'DetailsController'
        }).when('/signUp', {
        templateUrl: 'html/signUp.html',
        controller: 'RegistrationController'
    }).when('/403', {
        templateUrl: 'html/403.html',
        publicAccess: true
    }).when('/users', {
        templateUrl: 'html/users.html',
        controller: 'UsersController'
    });
    $httpProvider.interceptors.push('AuthInterceptor');
});

as.run(function ($rootScope, $location, AUTH_EVENTS) {
    $rootScope.$on(AUTH_EVENTS.unauthorized, function () {
        $location.path('/signIn');
    });
    $rootScope.$on(AUTH_EVENTS.accessDenied, function () {
        $location.path('/403');
    });
});
