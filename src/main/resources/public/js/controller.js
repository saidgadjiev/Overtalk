var as = angular.module('OverTalkApp.controllers', []);

as.controller('OverTalkController', function ($scope, $http, AUTH_EVENTS, USER_ROLES, AuthService, $location) {
    $scope.authenticated = AuthService.isAuthenticated();

    $scope.signOut = function () {
        AuthService.signOut();
    };
    $scope.$on(AUTH_EVENTS.signInSuccess, function () {
        $scope.authenticated = true;
    });
    $scope.$on(AUTH_EVENTS.signUpSuccess, function () {
        $scope.authenticated = true;
    });
    $scope.$on(AUTH_EVENTS.signOutSuccess, function () {
        $scope.authenticated = false;
        $location.path('/signIn');
    });

    $scope.isAuthorized = function (authorizedRoles) {
        return AuthService.isAuthorized(authorizedRoles);
    };
    $scope.userRoles = USER_ROLES;
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

as.controller('LoginController', function ($scope, $http, $location, $log, AuthService, AUTH_EVENTS) {
    if (AuthService.isAuthenticated()) {
        $location.path('/main');
    }
    $scope.signIn = function (isValid) {
        $scope.submitted = true;

        if (isValid) {
            AuthService.signIn($scope.user)
                .then(function (data) {
                    $scope.submitted = false;
                    $scope.userNameOrPasswordError = false;
                    $location.path('/main');
                }).catch(function (response) {
                    if (response.status === 400 && response.data.message === "Username or password wrong") {
                        $scope.userNameOrPasswordError = true;
                    }
            });
        }
    };
    $scope.gotoSignUp = function () {
        $location.path('/signUp');
    };
});

as.controller('RegistrationController', function ($scope, $http, $location, $log, AuthService, AUTH_EVENTS) {
    $scope.checkUsername = function () {
        $http.get('api/user/exist?userName=' + $scope.newUser.userName).success(function (data) {
            $scope.userNameExist = data.code === 409;
        })
    };

    $scope.signUp = function (isValid) {
        $scope.submitted = true;

        if (isValid) {
            $scope.submitted = false;
            AuthService.signUp($scope.newUser);
        }
    };

    $scope.$on(AUTH_EVENTS.signUpSuccess, function (data) {
        $scope.submitted = false;
        $location.path('/main');
    })
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

    $scope.pageChanged = function () {
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
});

as.controller('UsersController', function ($scope, AuthService, AUTH_EVENTS, USER_ROLES) {
    if (!AuthService.isAuthorized(USER_ROLES.admin)) {
        $scope.$emit(AUTH_EVENTS.accessDenied);
    }
});