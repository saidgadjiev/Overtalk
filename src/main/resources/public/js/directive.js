var as = angular.module('AboutMeApp.directives', []);

as.directive('uniqueUserName', function ($http, $q) {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function ($scope, $elem, $attr, ctrl) {
            ctrl.$asyncValidators.uniqueUserName = function (modelValue, viewValue) {
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

as.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function () {
                scope.$apply(function () {
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);
as.directive('access', [
    'AuthService',
    function (AuthService) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var roles = attrs.access.split(',');

                if (roles.length > 0) {
                    if (AuthService.isAuthorized(roles)) {
                        element.removeClass('hidden');
                    } else {
                        element.addClass('hidden');
                    }
                }
            }
        };
    }]
);