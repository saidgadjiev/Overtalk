var as = angular.module('OverTalkApp.controllers', []);

as.controller('OverTalkController', function ($scope, $location) {
});

as.controller('NewPostController', function ($scope, $location) {

});

as.controller('LoginController', function ($scope, $location) {
    $scope.login = function () {
        $location.path('/posts');
    };
});

as.controller('PostsController', function ($scope, $location) {
    $scope.add = function () {
        $location.path('/posts/new');
    };
});