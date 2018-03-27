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
            $log.debug(data);
            Session.create(data);
            authService.authenticated = true;
            $rootScope.$broadcast(AUTH_EVENTS.signUpSuccess, {
                content: data
            });
        }).error(function (data) {
            $log.error(data);
        });
    };

    authService.isAuthenticated = function () {
        return this.authenticated;
    };

    authService.signIn = function (user) {
        return $http.post('api/auth/signIn', user).success(function (data) {
            $log.debug(data);
            Session.create(data);
            authService.authenticated = true;
            $rootScope.$broadcast(AUTH_EVENTS.signInSuccess, {
                data: data
            });
        }).error(function (data) {
            $log.error(data);
        });
    };

    authService.signOut = function () {
        return $http.post('api/auth/signOut').success(function (data) {
            $log.debug(data);
            Session.invalidate();
            authService.authenticated = false;
            $rootScope.$broadcast(AUTH_EVENTS.signOutSuccess, {
                content: data
            });
        }).error(function (data) {
            $log.error(data);
        });
    };

    authService.isAuthorized = function (role) {
        if (!authService.authenticated) {
            return false;
        }

        return Session.userRoles.indexOf(role) !== -1;
    };

    return authService;
});