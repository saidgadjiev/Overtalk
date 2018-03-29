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

as.controller('LoginController', function ($scope, $http, $location, $log, AuthService) {
    if (AuthService.isAuthenticated()) {
        $location.path('/');
    }
    $scope.inputPasswordType = 'password';

    $scope.hideShowPassword = function(){
        if ($scope.inputPasswordType === 'password')
            $scope.inputPasswordType = 'text';
        else
            $scope.inputPasswordType = 'password';
    };
    //Для инициализации bootstrap-show-password
    //bootstrap-show-password
    //$("#password").password();

    $scope.signIn = function (isValid) {
        $scope.submitted = true;

        if (isValid) {
            AuthService.signIn($scope.user)
                .then(function (data) {
                    $location.path('/');
                }).catch(function (response) {
                if (response.status === 400 && response.data.message === "Username or password wrong") {
                }
            });
        }
    };
    $scope.gotoSignUp = function () {
        $location.path('/signUp');
    };
});

as.controller('RegistrationController', function ($scope, $http, $location, $log, AuthService) {
    $scope.checkUsername = function () {
        $scope.userNameExist = false;
        $http.get('api/user/exist?userName=' + $scope.newUser.userName).catch(function (response) {
            $scope.userNameExist = response.status === 302;
        });
    };
    $scope.inputPasswordType = 'password';

    $scope.hideShowPassword = function(){
        if ($scope.inputPasswordType === 'password')
            $scope.inputPasswordType = 'text';
        else
            $scope.inputPasswordType = 'password';
    };
    $scope.signUp = function (isValid) {
        $scope.submitted = true;

        if (isValid) {
            AuthService.signUp($scope.newUser)
                .then(function (data) {
                    $location.path('/');
                }).catch(function (response) {
                if (response.status === 409) {
                    $scope.userNameExist = true;
                } else if (response.status === 400) {
                    $scope.somethingWentWrong = true;
                }

                $log.error(response);
            });
        }
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

as.controller('UsersController', function ($scope, $http, $log) {
    $scope.currentPage = 1;
    $scope.itemsPerPage = 20;

    var loadUsers = function () {
        $http.get('api/users?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage).success(function (data) {
            $log.log(data);
            $scope.totalItems = data.content.totalElements;
            $scope.users = data.content.content;
        })
    };

    loadUsers();

    $scope.pageChanged = function () {
        $log.log('Page changed to: ' + $scope.currentPage);
        loadUsers();
    };
});