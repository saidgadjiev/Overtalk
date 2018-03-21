var as = angular.module('OverTalkApp.controllers', []);

as.controller('OverTalkController', function ($scope, $location) {
});

as.controller('NewPostController', function ($scope, $http, $location) {
    $scope.save = function (isValid) {
        $scope.submitted = true;

        if (isValid) {
            $http.post('api/post/', $scope.newPost).success(function (data) {
                $location.path('/posts');
            });
        }
    };
    $scope.cancel = function () {
        $location.path("/posts");
    }
});

as.controller('LoginController', function ($scope, $location, $log) {
    $scope.signIn = function () {
        $log.log('SignIn');
    };

    $scope.gotoSignUp = function () {
        $location.path('/signUp');
    }
});

as.controller('RegistrationController', function ($scope, $location, $log) {
    $scope.signUp = function () {
        $log.log('SignUp');
    };
});

as.controller('PostsController', function ($scope, $http, $location, $log) {
    $scope.currentPage = 1;
    $scope.itemsPerPage = 20;

    var loadPosts = function () {
        $http.get('api/post?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage).success(function (data) {
            $log.log(data);
            $scope.totalItems = data.totalElements;
            $scope.posts = data.content;
        })
    };

    loadPosts();

    $scope.add = function () {
        $location.path('/posts/new');
    };

    $scope.comments = function (postId) {
    };

    $scope.pageChanged = function() {
        $log.log('Page changed to: ' + $scope.currentPage);
        loadPosts();
    };
});

as.controller('DetailsController', function ($scope, $http, $routeParams, $location, $q, $log) {
    $scope.currentPage = 1;
    $scope.itemsPerPage = 20;

    var actionUrl = 'api/post/',
        loadComments = function () {
            $http.get(actionUrl + $routeParams.id + '/comments?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage).success(function (data) {
                $scope.comments = data.content;
                $scope.totalItems = data.totalElements;
            })
        },
        firstLoad = function () {
            $q.all([
                $http.get(actionUrl + $routeParams.id),
                $http.get(actionUrl + $routeParams.id + '/comments?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage)
            ]).then(function (result) {
                $log.log(result);
                $scope.post = result[0].data;
                $scope.comments = result[1].data.content;
                $scope.totalItems = result[1].data.totalElements;
            });
        };

    firstLoad();
    $scope.newComment = {};

    $scope.addComment = function () {
        $('#commentDialog').modal('show');
    };

    $scope.save = function (isValid) {
        $scope.submitted = true;

        if (isValid) {
            $http.post('api/post/' + $routeParams.id + '/comments', $scope.newComment).success(function (data) {
                $('#commentDialog').modal('hide');
                $scope.newComment = {};
                $scope.submitted = false;
                loadComments();
            });
        }
    };

    $scope.loadPage = function () {

    }
});