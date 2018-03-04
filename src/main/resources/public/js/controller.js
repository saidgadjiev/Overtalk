var as = angular.module('OverTalkApp.controllers', []);

as.controller('OverTalkController', function ($scope, $location) {
});

as.controller('NewPostController', function ($scope, $http, $location) {
    $scope.save = function (isValid) {
        $scope.submitted = true;

        if (isValid) {
            console.log("Data:");
            console.log($scope.newPost);
            $http.post('api/post/', $scope.newPost).success(function (data) {
                console.log("Response post: {'api/post'}:");
                console.log(data);
                $location.path('/posts');
            });
        }
    };
    $scope.cancel = function () {
        $location.path("/posts");
    }
});

as.controller('LoginController', function ($scope, $location) {
    $scope.login = function () {
        $location.path('/posts');
    };
});

as.controller('PostsController', function ($scope, $http, $location) {
    (function () {
        $http.get('api/post').success(function (data) {
            console.log("Response get: {'api/post'}:");
            console.log(data);
            $scope.posts = data;
        })
    })();

    $scope.add = function () {
        $location.path('/posts/new');
    };

    $scope.comments = function (postId) {
        console.log('comments(' + postId + ')')
    }
});

as.controller('DetailsController', function ($scope, $http, $routeParams, $location) {
    var load = function () {
        $http.get('api/post/' + $routeParams.id + "/comments").success(function (data) {
            console.log("Response get: {'api/post/" + $routeParams.id + "/comments'}:");
            console.log(data);
            $scope.comments = data;
            $scope.post = data.post;
        })
    };

    load();

    $scope.addComment = function () {
        $('#commentDialog').modal('show');
    };

    $scope.save = function (isValid) {
        $scope.submitted = true;

        if (isValid) {
            console.log("Data:");
            console.log($scope.newComment);
            $http.post('api/post/' + $routeParams.id + '/comments', $scope.newComment).success(function (data) {
                console.log("Response get: {'api/post/" + $routeParams.id + "/comments'}:");
                console.log(data);
                load();
                $('#commentDialog').modal('hide');
            });
        }
    }
});