var as = angular.module('AboutMeApp', ['ngRoute', 'ngMessages', 'ngSanitize', 'ui.bootstrap', 'angular.filter', 'angularSpinkit', 'AboutMeApp.controllers', 'AboutMeApp.services', 'AboutMeApp.directives']);

as.constant('USER_ROLES', {
    all: '*',
    admin: 'ROLE_ADMIN',
    user: 'ROLE_USER'
});

as.constant('SETTINGS', {
    webSocketUrl: '/aboutme',
    likeTopic: '/topic/likes',
    commentTopic: '/topic/comments'
});

as.constant('IMAGE', {
    defaultUrl: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAPcAAADMCAMAAACY78UPAAAAM1BMVEW8vsDn6Onq6+y5u73CxMbIycvh4uPW2NnR09Tc3d/AwsTk5ebU1dfg4eK9v8HBw8XMzc+TddevAAADGElEQVR4nO3c63KqMBRAYZINKiLo+z/tEblIMKng8cJO1venMx1xWEOKQUKzDAAAAAAAAAAAAAAAAAASJ9IUhcivd+Pb5FDZq7pIq7yprbmxp5TCZWcGKYXLyZp7ePHr3fkaMVP7ZA54YZ3wZLoPTrfNf70/3zLrPv56f74l1XHeJHpeS/VzLJMquXlLV5ncPFX6zv66pOyvS46HuPOltJc+XLKi6I9+JvXw6zhds6/nsYfC23kujze8zTaP4d3p3UYb3mU/fHLJvv91pOFj9nyiInXM4eNhfZyfxRwul2B2G24iDZf+GiwwTxm+iYgtfDzaoelZE2X4k6Pduod/c8c+65rd+Wsy3pjuNSai8Kb/uWQMN83z1wDAtxSftN3zvFT2g7b7vevkNucH0L01dNNNN910x9f9rtmKsu5T/ia1qm77rvt8UtJN98bQPe+Wm1feUnV3c6qryuzP6/dcc7cUxr46+jV35+PkY/1qDs3d5m71XSDF3Zfpqsy1O6+3e7LnrXS6q2m2f/FxuCiWbuO96xe+T6q42xnnlW+VRxlem6u32zmv+b78b9dCVMG31NvtDHTPMD/awGqf28Z6u5/MW7qNQiNdc7fk1a3cGl/2sAbfX6W5u/0TLo0tD75thsEQmLvr7u4vRH2bjH/8/ims9u7QFpMPOd9HXFTdk1P99Ikq7yqviLqlGkd08/TBwYi6d+PSPKmNwzPSo+nulmPfwsV9XtI70mPpHlaht+HnWbZvpEfSLeNkPZ89BR4Y6XF037OvhfuHw+0Z6XF055NU97I8NNKj6D56U1272Q2HGLoXZD/M7PV3L1wN4X4Dp7978SIQZ6Sr7/aevv0H3Ll2V969PNsd6cq75bI82xnpurvXZQe+hVbYveSD2wkfR7rq7vPK7MlI19wt/inpnwd82F5x90urN+2531htt5Qvrc+spd9aa/ewmGmtflvF3f/1lnTTvTV0p9pt3tata919ss9ZfADdW0M33XTTTXck3Yn+X4vs/K6nQn2S+bfZAAAAAAAAAAAAAAAAAAAAAAAAAAAAANL0D2K5KQJNX8MVAAAAAElFTkSuQmCC'
});

as.constant('WEB_SOCKET_EVENTS', {
    likeEvent: 'like-event',
    commentEvent: 'comment-event'
});

as.constant('AUTH_EVENTS', {
    signInSuccess: 'auth-signIn-success',
    signInFailed: 'auth-signIn-failed',
    signOutSuccess: 'auth-signOut-success',
    accessDenied: '403-access-denied',
    unauthorized: '401-unauthorized'
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
                $rootScope.$broadcast(AUTH_EVENTS.unauthorized, {status: 401});
            } else if (response.status === 403) {
                $rootScope.$broadcast(AUTH_EVENTS.accessDenied, {status: 403});
            }

            return $q.reject(response);
        }
    };
});

as.config(function ($routeProvider, $httpProvider, $locationProvider, USER_ROLES) {
    $routeProvider.when('/', {
        templateUrl: 'html/main.html',
        controller: 'MainController',
        publicAccess: true,
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/aboutme', {
        templateUrl: 'html/aboutme/aboutme.html',
        controller: 'AboutMeController',
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
    }).when('/categories', {
        templateUrl: 'html/category/categories.html',
        controller: 'CategoryController',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/categories/:id/posts', {
        templateUrl: 'html/post/posts.html',
        controller: 'PostController',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/categories/:id/posts/new', {
        templateUrl: 'html/post/new.html',
        controller: 'NewPostController',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.admin]
        }
    }).when('/categories/:categoryId/posts/:postId', {
        templateUrl: 'html/post/details.html',
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
        templateUrl: 'html/user/users.html',
        controller: 'UsersController',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.admin]
        }
    }).when('/projects', {
        templateUrl: 'html/project/projects.html',
        controller: 'ProjectController',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
        }
    }).when('/projects/new', {
        templateUrl: 'html/project/new.html',
        controller: 'NewProjectController',
        access: {
            loginRequired: true,
            authorizedRoles: [USER_ROLES.admin]
        }
    }).when('/loading', {
        templateUrl: 'html/loading.html',
        access: {
            loginRequired: false,
            authorizedRoles: [USER_ROLES.all]
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
    $locationProvider.hashPrefix('');
});

function initWebSocket() {

}

as.run(function ($rootScope,
                 $location,
                 $log,
                 LocationService,
                 AUTH_EVENTS,
                 Session,
                 AuthService,
                 WebSocket) {
    WebSocket.initialize();

    $rootScope.$on('$routeChangeStart', function (event, next) {
        //Если пользователь уже залогинен то он не может перейти на страницу логину
        if (next.originalPath === "/login" && $rootScope.authenticated) {
            event.preventDefault();
        }
        //Если пользователь незалогинен и вьюшка требует логин то не даем перейти на страницу
        else if (next.access && next.access.loginRequired && !$rootScope.authenticated) {
            event.preventDefault();

            $rootScope.$broadcast(AUTH_EVENTS.unauthorized, {});
        } else if (next.access && !AuthService.isAuthorized(next.access.authorizedRoles)) {
            event.preventDefault();

            $rootScope.$broadcast(AUTH_EVENTS.accessDenied, {});
        }
    });

    $rootScope.$on(AUTH_EVENTS.unauthorized, function (data) {
        if ($rootScope.loadingAccount && data.status !== 401) {
            LocationService.saveLocation();

            $location.path('/loading');
        } else {
            $location.path('/signIn');
        }
    });

    $rootScope.$on(AUTH_EVENTS.accessDenied, function () {
        $location.path('/403');
    });

    $rootScope.$on(AUTH_EVENTS.signInSuccess, function () {
        $rootScope.nickname = Session.nickname;
        $rootScope.authenticated = true;

        LocationService.gotoLast();
    });

    $rootScope.$on(AUTH_EVENTS.signOutSuccess, function () {
        $rootScope.authenticated = false;
        $rootScope.nickname = null;

        $location.path('/signIn');
    });

    $rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
        if (previous && previous.$$route) {
            var path = previous.$$route.originalPath;

            if (path !== '/signIn' && path !== 'signUp' && path !== '/loading') {
                LocationService.saveLocation(path, previous.pathParams);
            } else {
                LocationService.saveLocation('/aboutme', {});
            }
        }
    });

    $rootScope.isAuthorized = function (roles) {
        return AuthService.isAuthorized(roles);
    };

    AuthService.getAccount();
});
