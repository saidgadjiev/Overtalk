var as = angular.module('AboutMeApp', ['ngRoute', 'ngMessages', 'ui.bootstrap', 'AboutMeApp.controllers', 'AboutMeApp.services', 'AboutMeApp.directives']);

as.constant('USER_ROLES', {
    all: '*',
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

as.config(function ($routeProvider, $httpProvider, USER_ROLES) {
    $routeProvider.when('/', {
        templateUrl: 'html/main.html',
        publicAccess: true,
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/signIn', {
        templateUrl: 'html/auth/signIn.html',
        controller: 'LoginController',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/posts', {
        templateUrl: 'html/posts/posts.html',
        controller: 'PostsController',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/posts/new', {
        templateUrl: 'html/posts/new.html',
        controller: 'NewPostController',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.admin]
        }
    }).when('/posts/:id', {
        templateUrl: 'html/posts/details.html',
        controller: 'DetailsController',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/signUp', {
        templateUrl: 'html/auth/signUp.html',
        controller: 'RegistrationController',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/users', {
        templateUrl: 'html/users/users.html',
        controller: 'UsersController',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.admin]
        }
    }).when('/projects', {
        templateUrl: 'html/projects/projects.html',
        controller: 'ProjectsController',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/projects/new', {
        templateUrl: 'html/projects/new.html',
        controller: 'NewProjectController',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.admin]
        }
    }).when('/403', {
        templateUrl: 'html/error/403.html',
        publicAccess: true,
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/404', {
        templateUrl: 'html/error/404.html',
        publicAccess: true,
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/500', {
        templateUrl: 'html/error/500.html',
        publicAccess: true,
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).otherwise({
        redirectTo: '/404',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    });
    $httpProvider.interceptors.push('AuthInterceptor');
});

as.run(function ($rootScope,
                 $location,
                 $log,
                 LocationService,
                 AUTH_EVENTS,
                 RESPONSE_ERROR_EVENTS,
                 Session,
                 AuthService) {
    $rootScope.$on('$routeChangeStart', function (event, next) {
        //Если пользователь уже залогинен то он не может перейти на страницу логину
        if (next.originalPath === "/login" && $rootScope.authenticated) {
            event.preventDefault();
        }
        //Если пользователь незалогинен и вьюшка требует логин то не даем перейти на страницу
        else if (next.access && next.access.loginRequired && !$rootScope.authenticated) {
            event.preventDefault();
            $rootScope.$broadcast(AUTH_EVENTS.unauthorized, {});
        }else if(next.access && !AuthService.isAuthorized(next.access.authorizedRoles)) {
            event.preventDefault();
            $rootScope.$broadcast(AUTH_EVENTS.accessDenied, {});
        }
    });
    $rootScope.$on(AUTH_EVENTS.unauthorized, function () {
        LocationService.saveLocation();
        $location.path('/signIn');
    });
    $rootScope.$on(AUTH_EVENTS.accessDenied, function () {
        $location.path('/403');
    });
    $rootScope.$on(AUTH_EVENTS.signInSuccess, function () {
        $rootScope.nickName = Session.nickName;
        $rootScope.authenticated = true;
        LocationService.gotoLast();
    });
    $rootScope.$on(AUTH_EVENTS.signUpSuccess, function () {
        $rootScope.nickName = Session.nickName;
        $rootScope.authenticated = true;
    });
    $rootScope.$on(RESPONSE_ERROR_EVENTS.internalServerError, function () {
        $location.path('/500');
    });
    $rootScope.$on(AUTH_EVENTS.signOutSuccess, function () {
        $rootScope.authenticated = false;
        $rootScope.nickName = null;
        $location.path('/signIn');
    });
});
