var as = angular.module('OverTalkApp.services', []);

as.service('Session', function () {
    this.create = function (data) {
        this.userRoles = [];
        angular.forEach(data.authorities, function (value, key) {
            this.push(value.authority);
        }, this.userRoles);
    };

    this.invalidate = function () {
        this.userRoles = [];
    }
});

as.service('AuthService', function ($rootScope, $http, Session, $log, AUTH_EVENTS) {
    var authService = {};

    authService.signUp = function (user) {
        return $http.post('api/auth/signUp', user).success(function (data) {
            $log.log("SignUp success");
            $log.log(data);
            Session.create(data);
            authService.authenticated = true;
            $rootScope.$broadcast(AUTH_EVENTS.signUpSuccess, {
                content: data
            });
        });
    };

    authService.isAuthenticated = function () {
        return this.authenticated;
    };

    authService.signIn = function (user) {
        return $http.post('api/auth/signIn', user).success(function (data) {
            $log.log("SignIn success");
            Session.create(data);
            authService.authenticated = true;
            $rootScope.$broadcast(AUTH_EVENTS.signInSuccess, {
                data: data
            });
        }).error(function (data) {
            $rootScope.$broadcast(AUTH_EVENTS.signInFailed, {
                data: data
            });
        })
    };

    authService.signOut = function () {
        return $http.post('api/auth/signOut').success(function (data) {
            Session.invalidate();
            authService.authenticated = false;
            $rootScope.$broadcast(AUTH_EVENTS.signOutSuccess, {
                content: data
            });
        })
    };

    authService.isAuthorized = function (role) {
        if (!authService.authenticated) {
            return false;
        }

        return Session.userRoles.indexOf(role) !== -1;
    };

    return authService;
});