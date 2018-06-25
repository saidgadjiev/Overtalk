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

        ctx.font = "8vh Seymour One";
        ctx.fillStyle = "black";
        ctx.textAlign = "center";
        ctx.fillText("Добро пожаловать!",W / 2,H / 2);
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

as.controller('NewPostController', function ($scope, $http, $location, DataService) {
    $scope.newPost = {};
    var data = DataService.get('NewPostController');

    var oldPost = data.post;
    var backUrl = data.backUrl;

    if (oldPost) {
        $scope.newPost = oldPost;
    } else {
        $scope.newPost = {};
    }

    $scope.save = function (isValid) {
        $scope.submitted = true;

        if (isValid) {
            if (!oldPost) {
                $http.post('api/post/create', $scope.newPost).success(function (data) {
                    $location.path(backUrl);
                });
            } else {
                $http.post('api/post/update', $scope.newPost).success(function (data) {
                    $location.path(backUrl);
                });
            }
        }
    };
    $scope.cancel = function () {
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

as.controller('PostsController', function ($scope, $http, $location, $log, DataService) {
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
        var data = {};

        data.backUrl = '/posts';
        DataService.set('NewPostController', data);
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

as.controller('DetailsController', function ($scope, $http, $routeParams, $location, $q, $log, DataService) {
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

    $scope.edit = function (post) {
        var data = {};

        data.backUrl = '/posts/' + post.id;
        data.post = post;
        DataService.set('NewPostController', data);
        $location.path('/posts/new');
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

            fd.append('file', $scope.logo);
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

as.controller('AboutMeController', function ($scope, $http, $log, DataService) {
    $http.get('/api/aboutme').success(function (response) {
        $scope.skills = response.content.skills;
        $scope.aboutme = response.content;
    });

    $scope.editBiography = function () {
        $scope.biography = $scope.aboutme.biography;

        $('#editBiographyDialog').modal('show');
    };

    $scope.addSkill = function () {
        DataService.set('EditBiographyController', {});
        $('#addSkillDialog').modal('show');
    };

    $scope.removeSkill = function (skill) {
        $log.log(skill);
    };

    $scope.skill = {};

    $scope.editSkill = function (skill) {
        $log.log(skill);
        $scope.skill.id = skill.id;
        $scope.skill.name = skill.name;
        $scope.skill.percentage = skill.percentage;
        $scope.editableSkill = skill;

        $('#addSkillDialog').modal('show');
    };

    $scope.saveSkill = function (isValid) {
        $scope.submittedSkill = true;

        if (isValid) {
            $log.log($scope.skill);

            if ($scope.editableSkill) {
                $http.post('/api/skill/update', $scope.skill)
                    .success(function (response) {
                        $('#addSkillDialog').modal('hide');

                        $log.log(response);

                        $scope.editableSkill.name = response.content.name;
                        $scope.editableSkill.percentage = response.content.percentage;

                        $scope.submittedSkill = false;
                        $scope.skill = {};
                    });
            } else {
                $http.post('/api/skill/create', $scope.skill)
                    .success(function (response) {
                        $('#addSkillDialog').modal('hide');

                        $scope.aboutme.skills.push(response.content);

                        $scope.submittedSkill = false;
                        $scope.skill = {};
                    });
            }
        }
    };

    $scope.saveBiography = function (isValid) {
        $scope.submittedBiography = true;

        if (isValid) {
            $log.log($scope.biography);

            $http.post('/api/aboutme/update', $scope.biography)
                .success(function (response) {
                    $scope.aboutme.biography = response.content;

                    $scope.submittedBiography = false;
                    $('#editBiographyDialog').modal('hide');
                });
        }
    };
});

