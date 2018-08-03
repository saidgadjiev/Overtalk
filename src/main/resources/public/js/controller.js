var as = angular.module('AboutMeApp.controllers', []);

as.controller('MainController', function () {
    var canvas = document.getElementById("canv");
    var ctx = canvas.getContext("2d");
    // Big Word Array
    var words = ["Spring", "Hibernate", "PostgreSQL", "MySQL", "H2", "Tomcat", "Jetty", "Vaadin"];

    // Utilities
    function getRandomColor() {
        var letters = '0123456789ABCDEF';
        var color = '#';
        for (var i = 0; i < 6; i++) {
            color += letters[Math.floor(Math.random() * 16)];
        }
        return color;
    }

    function randomColor() {
        return '#' + Math.random().toString(16).slice(2, 8);
    }

    function randomWord() {
        var word = words[Math.floor(Math.random() * words.length)];
        return word;
    }

    function randomInt(min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }

    //Make the canvas occupy the full page
    var W = window.innerWidth,
        H = window.innerHeight;
    canvas.width = W;
    canvas.height = H;
    var particles = [];
    var mouse = {};
    //Lets create some particles now
    var particle_count = 20;
    for (var i = 0; i < particle_count; i++) {
        particles.push(new particle());
    }

    function particle() {
        //speed, life, location, life, colors
        //speed range = -2.5 to 2.5
        this.speed = {
            x: -2.5 + Math.random() * 5,
            y: -2.5 + Math.random() * 5
        };
        //location = center of the screen
        if (mouse.x && mouse.y) {
            this.location = {
                x: mouse.x,
                y: mouse.y
            };
        } else {
            this.location = {
                x: W / 2,
                y: H / 2
            };
        }
        this.color = getRandomColor()

        this.font = {
            size: randomInt(3, 15)
        };

        this.word = randomWord()
    }

    function draw() {
        ctx.globalCompositeOperation = "source-over";
        //Painting the canvas black
        ctx.fillStyle = "#ffffff";
        ctx.fillRect(0, 0, W, H);

        for (var i = 0; i < particles.length; i++) {
            var p = particles[i];
            ctx.beginPath();
            ctx.font = p.font.size + "vh Luckiest Guy";
            ctx.textAlign = "center";
            ctx.transition = "all 2s ease";
            ctx.fillStyle = p.color;
            ctx.fillText(p.word, p.location.x, p.location.y);
            ctx.fill();
            ctx.stroke();

            //lets move the particles
            p.location.x += p.speed.x;
            p.location.y += p.speed.y;

            if (p.location.x < 0) {
                p.location.x = W;
            } else if (p.location.x > W) {
                p.location.x = 0;
            }
            if (p.location.y < 0) {
                p.location.y = H;
            } else if (p.location.y > H) {
                p.location.y = 0;
            }

            // Make 'em big and small
            // Warning: Causes extreme lag
            //p.font.size += randomInt(-0.1, 0.1)
        }
    }

    setInterval(draw, 10);
});

as.controller('AboutMeAppController', function ($scope, LocationService, AuthService, $location) {
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

as.controller('NewPostController', function ($scope, $http, $location, $routeParams, $log, DataService) {
    $scope.newPost = {};
    var data = DataService.get('NewPostController');

    var oldPost = data.post;
    var backUrl = data.backUrl;

    if (oldPost) {
        $scope.newPost = oldPost;
    } else {
        $scope.newPost = {};
    }

    var actionUrl = 'api/post/';

    $scope.doSave = function (isValid) {
        $scope.submitted = true;

        if (isValid) {
            if (!oldPost) {
                $http.post(actionUrl + $routeParams.id + '/create', $scope.newPost).then(function (response) {
                    $location.path(backUrl);
                });
            } else {
                $http.post(actionUrl + 'update', $scope.newPost).then(function (response) {
                    $location.path(backUrl);
                });
            }
        }
    };
    $scope.doCancel = function () {
        $location.path(backUrl);
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
                    if (response.status === 400) {
                        $scope.somethingWentWrong = true;
                    }

                    $log.error(response);
                });
        }
    };
});

as.controller('PostController', function ($scope,
                                          $http,
                                          $location,
                                          $log,
                                          $routeParams,
                                          $q,
                                          LikeService,
                                          DataService) {
    $scope.category = DataService.get('PostController');

    $scope.currentPage = 1;
    $scope.itemsPerPage = 20;

    var postUrl = 'api/post/',
        loadPosts = function () {
            $http.get(postUrl + $routeParams.id + '/posts?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage)
                .then(function (response) {
                    $log.log(response.data);
                    $scope.totalItems = response.data.totalElements;
                    $scope.posts = response.data.content;
                    if (!$scope.category) {
                        $http.get('api/category/' + $routeParams.id).then(function (value) {
                            $scope.category = value.data;
                        });
                    }
                })
        };

    loadPosts();

    $scope.doCreate = function () {
        var data = {};

        data.backUrl = '/categories/' + $routeParams.id + '/posts';
        DataService.set('NewPostController', data);
        $location.path('/categories/' + $routeParams.id + '/posts/new');
    };

    $scope.doChangePage = function () {
        $log.log('Page changed to: ' + $scope.currentPage);
        loadPosts();
    };

    $scope.doLike = function (post) {
        $scope.likeInfo = {};
        $scope.likeInfo.postId = post.id;

        LikeService.doLike($scope.likeInfo)
            .then(function (response) {
                post.liked = response.data.liked;
                post.likesCount = response.data.likesCount;
            });
    };

    $scope.doDislike = function (post) {
        $scope.likeInfo = {};
        $scope.likeInfo.postId = post.id;

        LikeService.doDislike($scope.likeInfo)
            .then(function (response) {
                post.liked = response.data.liked;
                post.likesCount = response.data.likesCount;
            });
    };

    $scope.doDelete = function (post) {
        $http.post(postUrl + 'delete/' + post.id).then(function () {
            $log.log('delete post' + post);
            $scope.posts.splice($scope.posts.indexOf(post), 1);
        });
    };
    $scope.doEdit = function (post) {
        var data = {};

        data.backUrl = '/categories/' + $routeParams.id + '/posts';
        data.post = post;
        DataService.set('NewPostController', data);
        $location.path('/categories/' + $routeParams.id + '/posts/new');
    };

    $scope.gotoComments = function (post) {
        DataService.set('DetailsController', post);
        $location.path('/categories/' + $scope.category.id + '/posts/' + post.id)
    };
});

as.controller('DetailsController', function ($scope,
                                             $http,
                                             $routeParams,
                                             $location,
                                             $q,
                                             $log,
                                             $uibModal,
                                             Session,
                                             LocationService,
                                             AuthService,
                                             LikeService,
                                             DataService) {
    $scope.post = DataService.get('DetailsController');

    if (!$scope.post) {
        $http.get('api/post/' + $routeParams.postId).then(function (value) {
            $scope.post = value.data;
        });
    }
    $scope.currentPage = 1;
    $scope.itemsPerPage = 20;

    var commentUrl = 'api/comment/',
        loadComments = function () {
            $http.get(commentUrl + $routeParams.postId + '/comments?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage).then(function (response) {
                $scope.comments = response.data.content;
                $scope.totalItems = response.data.totalElements;
            })
        };

    loadComments();

    $scope.newComment = {};

    $scope.canEdit = function (comment) {
        if (AuthService.isAuthorized('ROLE_ADMIN')) {
            return true;
        }
        if (Session.nickName === comment.nickName) {
            return true;
        }

        return false;
    };

    $scope.doCreateComment = function (valid) {
        if (valid) {
            $http.post(commentUrl + $routeParams.postId + '/create', $scope.newComment)
                .then(function (response) {
                    loadComments();

                    $scope.newComment = {};
                });
        }
    };

    $scope.doEditPost = function (post) {
        var data = {};

        data.backUrl = '/categories/' + $routeParams.categoryId + '/posts/' + post.id;
        data.post = post;
        DataService.set('NewPostController', data);
        $location.path('/categories/' + $routeParams.categoryId + '/posts/new');
    };

    $scope.doEditComment = function (comment) {
        var editCommentModal = $uibModal.open({
            templateUrl: 'editComment.html',
            controller: function ($scope, $uibModalInstance, $log) {
                $scope.comment = {};

                $scope.comment.id = comment.id;
                $scope.comment.content = comment.content;

                $scope.ok = function () {
                    $scope.submitted = true;

                    if ($scope.editCommentForm.$valid) {
                        $scope.submitted = false;
                        $uibModalInstance.close($scope.comment);
                    }
                };

                $scope.cancel = function () {
                    $uibModalInstance.dismiss('cancel');
                };

                $scope.dismiss = function () {
                    $uibModalInstance.dismiss('cancel');
                };

                $log.log('Open edit comment modal');
            }
        });

        editCommentModal.result.then(function (editedComment) {
            $http.post(commentUrl + 'update', editedComment)
                .then(function (response) {
                    comment.content = response.data.content;
                });
        });
    };

    $scope.doLike = function (post) {
        $scope.likeInfo = {};
        $scope.likeInfo.postId = post.id;

        LikeService.doLike($scope.likeInfo)
            .then(function (response) {
                post.liked = response.data.liked;
                post.likesCount = response.data.likesCount;
            });
    };

    $scope.doDislike = function (post) {
        $scope.likeInfo = {};
        $scope.likeInfo.postId = post.id;

        LikeService.doDislike($scope.likeInfo)
            .then(function (response) {
                post.liked = response.data.liked;
                post.likesCount = response.data.likesCount;
            });
    };

    $scope.doDeleteComment = function (comment) {
        $http.post(commentUrl + 'delete', comment).then(function () {
            $log.log('delete comment' + comment);
            $scope.comments.splice($scope.comments.indexOf(comment), 1);
        });
    };

    $scope.doDeletePost = function (post) {
        $http.post('api/post/delete/' + post.id).then(function () {
            $log.log('delete post' + post);

            $location.path('/categories/' + $routeParams.categoryId + '/posts');
        });
    };
});

as.controller('UsersController', function ($scope, $http, $log) {
    $scope.currentPage = 1;
    $scope.itemsPerPage = 20;

    var loadUsers = function () {
        $http.get('api/users?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage).then(function (response) {
            $log.log(response);
            $scope.totalItems = response.data.totalElements;
            $scope.users = response.data.content;
        })
    };

    loadUsers();

    $scope.pageChanged = function () {
        $log.log('Page changed to: ' + $scope.currentPage);
        loadUsers();
    };
});

as.controller('NewProjectController', function ($scope, $http, $log, $location, FileService, DataService) {
    $scope.data = DataService.get('NewProjectController');
    $scope.project = {};

    if ($scope.data) {
        $scope.project.id = $scope.data.id;
        $scope.project.name = $scope.data.name;
        $scope.project.description = $scope.data.description;
        $scope.project.projectLink = $scope.data.projectLink;
        $scope.project.logoPath = $scope.data.logoPath;
        $scope.logoPath = '/api/file/logo/' + $scope.data.logoPath;
    }

    var actionUrl = 'api/project/';

    $scope.doSave = function (isValid) {
        $scope.submitted = true;

        if (isValid) {
            var fd = new FormData();

            fd.append('file', $scope.logo);
            fd.append('data', angular.toJson($scope.project));

            if ($scope.data) {
                $http.post(actionUrl + 'update', fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                })
                    .then(function (response) {
                        $location.path('/projects');
                    })
                    .catch(function (reason) {
                        $log.log(reason);
                    });
            } else {
                $http.post(actionUrl + 'create', fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                })
                    .then(function () {
                        $location.path('/projects');
                    })
                    .catch(function (reason) {
                        $log.log(reason);
                    });
            }
        }
    };
    $scope.cancel = function () {
        $location.path("/projects");
    };
    $scope.upload = function () {
        FileService.readAsDataURL($scope.logo, $scope)
            .then(function (result) {
                $scope.logoPath = result;
            });
    };
});

as.controller('ProjectController', function ($scope,
                                             $http,
                                             $log,
                                             $location,
                                             $uibModal,
                                             IMAGE,
                                             DataService) {
    $scope.defaultUrl = IMAGE.defaultUrl;

    $http.get('api/project').then(function (response) {
        $log.log(response);
        $scope.projects = response.data;
    });

    $scope.doCreate = function () {
        $location.path('/projects/new');
    };

    $scope.doOpenDetails = function (project) {
        $uibModal.open({
            templateUrl: 'projectDetails.html',
            controller: function ($scope, $uibModalInstance) {
                $scope.details = project;

                $scope.doEdit = function (project) {
                    DataService.set('NewProjectController', project);
                    $location.path('/projects/new');
                };

                $scope.dismiss = function () {
                    $uibModalInstance.dismiss('cancel');
                };
            }
        });
    };

    $scope.doEdit = function (project) {
        DataService.set('NewProjectController', project);
        $location.path('/projects/new');
    };
});

as.controller('AboutMeController', function ($scope,
                                             $http,
                                             $uibModal,
                                             $log) {
    $http.get('/api/aboutMe').then(function (response) {
        $scope.aboutMe = response.data;
    });

    $scope.editAboutMe = function (aboutMe) {
        var editAboutMeModal = $uibModal.open({
            templateUrl: 'editAboutMe.html',
            controller: function ($scope, $uibModalInstance) {
                $scope.aboutMe = {};

                $scope.aboutMe.placeOfResidence = aboutMe.placeOfResidence;
                $scope.aboutMe.post = aboutMe.post;

                $scope.ok = function () {
                    $scope.submitted = true;

                    if ($scope.editAboutMeForm.$valid) {
                        $scope.submitted = false;
                        $uibModalInstance.close($scope.aboutMe);
                    }
                };

                $scope.cancel = function () {
                    $uibModalInstance.dismiss('cancel');
                };

                $scope.dismiss = function () {
                    $uibModalInstance.dismiss('cancel');
                };
            }
        });

        editAboutMeModal.result.then(function (value) {
            $http.post('/api/aboutMe/update', value)
                .then(function (response) {
                    aboutMe.placeOfResidence = response.data.placeOfResidence;
                    aboutMe.post = response.data.post;
                });
        });
    };

    $scope.addSkill = function (aboutMe) {
        var addSkillModal = $uibModal.open({
            templateUrl: 'createOrEditSkill.html',
            controller: function ($scope, $uibModalInstance) {
                $scope.skill = {};

                $scope.ok = function () {
                    $scope.submitted = true;

                    if ($scope.skillForm.$valid) {
                        $scope.submitted = false;
                        $uibModalInstance.close($scope.skill);
                    }
                };

                $scope.cancel = function () {
                    $uibModalInstance.dismiss('cancel');
                };

                $scope.dismiss = function () {
                    $uibModalInstance.dismiss('cancel');
                };
            }
        });

        addSkillModal.result.then(function (value) {
            $http.post('/api/skill/create', value)
                .then(function (response) {
                    aboutMe.skills.push(response.data);
                });
        });
    };

    $scope.removeSkill = function (skill) {
        $http.post('/api/skill/remove/' + skill.id).then(function () {
            $log.log('delete skill ' + skill);
            $scope.aboutMe.skills.splice($scope.aboutMe.skills.indexOf(skill), 1);
        });
    };

    $scope.skill = {};

    $scope.editSkill = function (skill) {
        $log.log(skill);

        var editSkillModal = $uibModal.open({
            templateUrl: 'createOrEditSkill.html',
            controller: function ($scope, $uibModalInstance) {
                $scope.skill = {};

                $scope.skill.id = skill.id;
                $scope.skill.name = skill.name;
                $scope.skill.percentage = skill.percentage;

                $scope.ok = function () {
                    $scope.submitted = true;

                    if ($scope.skillForm.$valid) {
                        $scope.submitted = false;
                        $uibModalInstance.close($scope.skill);
                    }
                };

                $scope.cancel = function () {
                    $uibModalInstance.dismiss('cancel');
                };
            }
        });

        editSkillModal.result.then(function (value) {
            $http.post('/api/skill/update', value)
                .then(function (response) {
                    $log.log(response);

                    skill.name = response.data.name;
                    skill.percentage = response.data.percentage;
                });
        });
    };
});

as.controller('CategoryController', function ($scope,
                                              $location,
                                              $http,
                                              $log,
                                              $uibModal,
                                              DataService) {
    $scope.currentPage = 1;
    $scope.itemsPerPage = 20;

    var actionUrl = 'api/category/';

    var loadCategories = function () {
        $http.get('api/category?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage).then(function (response) {
            $log.log(response);
            $scope.totalItems = response.data.totalElements;
            $scope.categories = response.data.content;
        })
    };

    loadCategories();

    $scope.pageChanged = function () {
        $log.log('Page changed to: ' + $scope.currentPage);
        loadCategories();
    };

    $scope.add = function () {
        var createCategoryModal = $uibModal.open({
            templateUrl: 'createOrEditCategory.html',
            controller: function ($scope, $uibModalInstance) {
                $scope.category = {};

                $scope.ok = function () {
                    $scope.submitted = true;

                    if ($scope.categoryForm.$valid) {
                        $scope.submitted = false;
                        $uibModalInstance.close($scope.category);
                    }
                };

                $scope.cancel = function () {
                    $uibModalInstance.dismiss('cancel');
                };

                $scope.dismiss = function () {
                    $uibModalInstance.dismiss('cancel');
                };
            }
        });

        createCategoryModal.result.then(function (value) {
            $http.post(actionUrl + 'create', value)
                .then(function () {
                    loadCategories();
                });
        });
    };

    $scope.edit = function (category) {
        var editCategoryModal = $uibModal.open({
            templateUrl: 'createOrEditCategory.html',
            controller: function ($scope, $uibModalInstance) {
                $scope.category = {};

                $scope.category.id = category.id;
                $scope.category.name = category.name;
                $scope.category.description = category.description;

                $scope.ok = function () {
                    $scope.submitted = true;

                    if ($scope.categoryForm.$valid) {
                        $scope.submitted = false;
                        $uibModalInstance.close($scope.category);
                    }
                };

                $scope.cancel = function () {
                    $uibModalInstance.dismiss('cancel');
                };
            }
        });

        editCategoryModal.result.then(function (value) {
            $http.post(actionUrl + 'update', value)
                .then(function (response) {
                    category.name = response.data.name;
                    category.description = response.data.description;
                });
        });
    };

    $scope.delete = function (category) {
        $http.post(actionUrl + 'delete/' + category.id).then(function () {
            $log.log('delete category ' + category);
            $scope.categories.splice($scope.categories.indexOf(category), 1);
        });
    };

    $scope.gotoPosts = function (category) {
        DataService.set('PostController', category);
        $location.path('/categories/' + category.id + '/posts');
    };
});


