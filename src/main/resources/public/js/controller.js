var as = angular.module('AboutMeApp.controllers', []);

as.controller('AboutMeController', function ($scope, LocationService, AuthService, $location) {
    $scope.signOut = function () {
        AuthService.signOut().then(function () {
            LocationService.saveLocation('/');
            $location.path('/signIn');
        });
    };

    $scope.isActiveNavItem = function (url) {
        var currentUrl = $location.path();

        if (url === '/') {
            return currentUrl === url;
        }

        return currentUrl.indexOf(url) !== -1;
    };
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
    $scope.inputPasswordType = 'password';

    $scope.hideShowPassword = function () {
        if ($scope.inputPasswordType === 'password')
            $scope.inputPasswordType = 'text';
        else
            $scope.inputPasswordType = 'password';
    };
    //Для инициализации bootstrap-show-password
    //bootstrap-show-password
    //$("#passwordShowPass").password();

    $scope.signIn = function (isValid) {
        $scope.submitted = true;

        if (isValid) {
            AuthService.signIn($scope.user)
                .catch(function (response) {
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

as.controller('RegistrationController', function ($scope, $http, $location, $log, AuthService) {
    $scope.checkUsername = function () {
        $scope.userNameExist = false;
        $http.get('api/user/exist?userName=' + $scope.newUser.userName).catch(function (response) {
            $scope.userNameExist = response.status === 302;
        });
    };
    $scope.inputPasswordType = 'password';

    $scope.hideShowPassword = function () {
        if ($scope.inputPasswordType === 'password')
            $scope.inputPasswordType = 'text';
        else
            $scope.inputPasswordType = 'password';
    };
    $scope.inputConfirmPasswordType = 'password';

    $scope.hideShowConfirmPassword = function () {
        if ($scope.inputConfirmPasswordType === 'password')
            $scope.inputConfirmPasswordType = 'text';
        else
            $scope.inputConfirmPasswordType = 'password';
    };
    $scope.signUp = function (isValid) {
        $scope.submitted = true;

        if (isValid) {
            AuthService.signUp($scope.newUser)
                .catch(function (response) {
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

    $scope.like = function (post) {
        $scope.likeInfo = {};
        $scope.likeInfo.postId = post.id;

        $http.post('api/like/post', $scope.likeInfo)
            .success(function (response) {
                likeInfo = response.content;
                post.liked = likeInfo.liked;
                post.likesCount = likeInfo.likesCount;
            });
    };

    $scope.dislike = function (post) {
        $scope.likeInfo = {};
        $scope.likeInfo.postId = post.id;

        $http.post('api/dislike/post', $scope.likeInfo)
            .success(function (response) {
                likeInfo = response.content;
                post.liked = likeInfo.liked;
                post.likesCount = likeInfo.likesCount;
            });
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

    $scope.save = function () {
        if (!$scope.newComment.content || $scope.newComment.content === 0) {
            $http.post('api/post/' + $routeParams.id + '/comments', $scope.newComment).success(function (data) {
                $scope.newComment = {};
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

as.controller('NewProjectController', function ($scope, $http, $log, $location) {
    $scope.save = function (isValid) {
        $scope.submitted = true;

        if (isValid) {
            var fd = new FormData();

            fd.append('file', file);
            fd.append('data', angular.toJson($scope.newProject));

            $http.post("/api/project", fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
                .success(function () {
                    $location.path('/projects');
                })
                .catch(function (reason) {
                    $log.log(reason);
                });
        }
    };
    $scope.cancel = function () {
        $location.path("/projects");
    }
});

as.controller('ProjectsController', function ($scope, $http, $log, $location) {
    $http.get('api/project').success(function (data) {
        $log.log(data);
        $scope.projects = data.content;
    });
    $scope.add = function () {
        $location.path('/projects/new');
    };
});
