var as = angular.module('OverTalkApp.directives', []);

as.directive('uniqueUserName', function () {
    return {
        require: 'ngModel',
        link: function($scope, $elem, $attr, ctrl) {
            ctrl.$asyncValidators.uniqueUserName = function(modelValue, viewValue) {
                var username = viewValue;
                var deferred = $q.defer();

                $http.get('api/user/exist?userName=' + $scope.newUser.userName).catch(function (response) {
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