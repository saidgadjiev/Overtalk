var as = angular.module('OverTalkApp.directives', []);

as.directive('uniqueUserName', function ($http, $q) {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function($scope, $elem, $attr, ctrl) {
            ctrl.$asyncValidators.uniqueUserName = function(modelValue, viewValue) {
                var username = viewValue;
                var deferred = $q.defer();

                $http.get('api/user/exist?userName=' + username).catch(function (response) {
                    if (response.status === 302) {
                        deferred.reject();
                    } else {
                        deferred.resolve();
                    }
                });

                return deferred.promise;
            }
        }
    }
});