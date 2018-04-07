var as = angular.module('OverTalkApp.controllers', []);

as.controller('OverTalkController', function ($scope, $http, $log, LocationService, AUTH_EVENTS, USER_ROLES, Session, AuthService, $location) {
    $scope.authenticated = AuthService.isAuthenticated();

    $scope.signOut = function () {
        AuthService.signOut().then(function () {
            LocationService.saveLocation('/');
            $location.path('/signIn');
        });
    };
    $scope.$on(AUTH_EVENTS.signInSuccess, function () {
        $scope.nickName = Session.nickName;
        $scope.authenticated = true;
    });
    $scope.$on(AUTH_EVENTS.signUpSuccess, function () {
        $scope.nickName = Session.nickName;
        $scope.authenticated = true;
    });
    $scope.$on(AUTH_EVENTS.signOutSuccess, function () {
        $scope.authenticated = false;
        $scope.nickName = '';
        $location.path('/signIn');
    });

    $scope.isAuthorized = function (authorizedRoles) {
        return AuthService.isAuthorized(authorizedRoles);
    };
    $scope.userRoles = USER_ROLES;

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

    $scope.hideShowPassword = function(){
        if ($scope.inputPasswordType === 'password')
            $scope.inputPasswordType = 'text';
        else
            $scope.inputPasswordType = 'password';
    };
    $scope.inputConfirmPasswordType = 'password';

    $scope.hideShowConfirmPassword = function(){
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
        if (!$scope.newComment.content || $scope.newComment.content  === 0) {
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

as.controller('ProjectsController', function ($scope, $http, $log) {
});

var myApp = angular.module('myApp', []);

myApp.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

myApp.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function(file, uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        fd.append('data', angular.toJson({'title': 'title'}));
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
            .success(function(){
            })
            .error(function(){
            });
    }
}]);

myApp.controller('myCtrl', ['$scope', 'fileUpload', function($scope, fileUpload){

    $scope.uploadFile = function(){
        var file = $scope.myFile;
        console.log('file is ' );
        console.dir(file);
        var uploadUrl = "http://localhost:8089/api/projects";
        fileUpload.uploadFileToUrl(file, uploadUrl);
    };

}]);