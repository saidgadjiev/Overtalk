var as = angular.module('AboutMeApp.services', []);

as.service('Session', function () {
    this.create = function (data) {
        this.nickName = data.nickName;
        this.userRoles = [];
        angular.forEach(data.authorities, function (value, key) {
            this.push(value.authority);
        }, this.userRoles);
    };

    this.invalidate = function () {
        this.nickName = null;
        this.userRoles = [];
    }
});

as.service('AuthService', function ($rootScope, $http, Session, $log, AUTH_EVENTS) {
    var authService = {};

    authService.signUp = function (user) {
        return $http.post('api/auth/signUp', user).success(function (response) {
            $log.debug(response);
            Session.create(response.content);
            authService.authenticated = true;
            $rootScope.$broadcast(AUTH_EVENTS.signUpSuccess, {
                content: response
            });

            return response;
        });
    };

    authService.isAuthenticated = function () {
        return this.authenticated;
    };

    authService.signIn = function (user) {
        return $http.post('api/auth/signIn', user).success(function (response) {
            $log.debug(response);
            Session.create(response.content);
            authService.authenticated = true;
            $rootScope.$broadcast(AUTH_EVENTS.signInSuccess, {
                data: response
            });

            return response;
        });
    };

    authService.signOut = function () {
        return $http.post('api/auth/signOut').success(function (response) {
            $log.debug(response);
            Session.invalidate();
            authService.authenticated = false;
            $rootScope.$broadcast(AUTH_EVENTS.signOutSuccess, {
                content: response
            });

            return response;
        });
    };

    authService.isAuthorized = function (authorizedRoles) {
        if (!angular.isArray(authorizedRoles)) {
            if (authorizedRoles === '*') {
                return true;
            }
            authorizedRoles = [authorizedRoles];
        }
        var isAuthorized = false;

        angular.forEach(authorizedRoles, function (authorizedRole) {
            if (isAuthorized) {
                return;
            }
            if (authorizedRole === '*') {
                isAuthorized = true;
            } else if (!authService.authenticated) {
                isAuthorized = false;
            } else {
                isAuthorized = Session.userRoles.indexOf(authorizedRole) !== -1;
            }
        });

        return isAuthorized;
    };

    return authService;
});

as.service('LocationService', function ($location) {
    var locationService = {};

    locationService.location = '/';
    locationService.saveLocation = function (url) {
        if (!url || url.length === 0) {
            locationService.location = $location.path();
        } else {
            locationService.location = url;
        }
    };

    locationService.gotoLast = function () {
        $location.path(locationService.location);
    };

    return locationService;
});

as.service('DataService', function () {
    var dataService = {};

    dataService.data = {};

    dataService.set = function (target, data) {
        dataService.data[target] = data;
    };

    dataService.get = function (target) {
        return dataService.data[target];
    };

    return dataService;
});