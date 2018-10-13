var as = angular.module('AboutMeApp.directives', []);

as.directive('uniqueUserName', function ($http, $q) {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function ($scope, $elem, $attr, ctrl) {
            ctrl.$asyncValidators.uniqueUserName = function (modelValue, viewValue) {
                var username = viewValue;
                var deferred = $q.defer();

                $http.get('api/username/exist/' + username).catch(function (response) {
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

as.directive('uniqueNickName', function ($http, $q) {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function ($scope, $elem, $attr, ctrl) {
            ctrl.$asyncValidators.uniqueNickName = function (modelValue, viewValue) {
                var nickname = viewValue;
                var deferred = $q.defer();

                $http.get('api/nickname/exist/' + nickname).catch(function (response) {
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
                scope.upload();
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

                scope.$watch(
                    function () {
                        return AuthService.isAuthorized(roles)
                    },
                    function (newValue, oldValue) {
                        if (newValue) {
                            element.removeClass('hidden');
                        } else {
                            element.addClass('hidden');
                        }
                });
            }
        };
    }]
);

as.directive('hljs', [function () {
        return {
            restrict: 'A',
            link: function (scope, element) {
                hljs.highlightBlock(element[0]);
            }
        };
    }]
);

as.directive('ellipsis', function () {
    return {
        restrict: 'A',
        link: function (scope, element) {

        }
    };
});

as.directive('compile', function ($compile) {
    return function (scope, element, attrs) {
        scope.$watch(
            function (scope) {
                // watch the 'compile' expression for changes
                return scope.$eval(attrs.compile);
            },
            function (value) {
                // when the 'compile' expression changes
                // assign it into the current DOM
                element.html(value);

                // compile the new DOM and link it to the current
                // scope.
                // NOTE: we only compile .childNodes so that
                // we don't get into infinite loop compiling ourselves
                $compile(element.contents())(scope);
            }
        );
    };
});

as.directive('errSrc', function() {
    return {
        link: function(scope, element, attrs) {
            element.bind('error', function() {
                if (attrs.src !== attrs.errSrc) {
                    attrs.$set('src', attrs.errSrc);
                }
            });
        }
    }
});
