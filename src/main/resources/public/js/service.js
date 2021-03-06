var as = angular.module('AboutMeApp.services', []);

as.service('Session', function () {
    this.create = function (profile) {
        this.nickname = profile.nickname;
        this.userRoles = [];
        angular.forEach(profile.authorities, function (value, key) {
            this.push(value.authority);
        }, this.userRoles);
    };

    this.invalidate = function () {
        this.nickname = null;
        this.userRoles = [];
    }
});

as.service('AuthService', function ($rootScope, $http, Session, $log, AUTH_EVENTS) {

    var authService = {};

    authService.loginConfirmed = function (profile) {
        $rootScope.loadingAccount = false;

        if (profile) {
            Session.create(profile);
            authService.authenticated = true;
            $rootScope.$broadcast(AUTH_EVENTS.signInSuccess, {
                data: profile
            });
        }
    };

    authService.signUp = function (user) {
        return $http.post('api/auth/signUp', user).then(function (response) {
            $log.debug(response);

            authService.loginConfirmed(response.data);
        });
    };

    authService.isAuthenticated = function () {
        return this.authenticated;
    };

    authService.signIn = function (user) {
        return $http.post('api/auth/signIn', user).then(function (response) {
            $log.log(response);

            authService.loginConfirmed(response.data);
        });
    };

    authService.signOut = function () {
        return $http.post('api/auth/signOut').then(function (response) {
            $log.debug(response);

            Session.invalidate();
            authService.authenticated = false;

            $rootScope.$broadcast(AUTH_EVENTS.signOutSuccess, {
                content: response
            });
        });
    };

    authService.isAuthorized = function (authorizedRoles) {
        if (!angular.isArray(authorizedRoles)) {
            if (authorizedRoles === '*') {
                return true;
            }
            authorizedRoles = [authorizedRoles];
        }
        var isAuthorized = false;

        angular.forEach(authorizedRoles, function (authorizedRole) {
            if (isAuthorized) {
                return;
            }
            if (authorizedRole === '*') {
                isAuthorized = true;
            } else if (!authService.isAuthenticated()) {
                isAuthorized = false;
            } else {
                isAuthorized = Session.userRoles.indexOf(authorizedRole) !== -1;
            }
        });

        return isAuthorized;
    };

    authService.getAccount = function () {
        $rootScope.loadingAccount = true;

        return $http.get('api/auth/account').then(function (response) {
            $log.debug(response);

            authService.loginConfirmed(response.data);
        });
    };

    return authService;
});

as.service('LocationService', function ($location) {
    var locationService = {};

    locationService.saveLocation = function (url, params) {
        if (!url || url.length === 0) {
            locationService.location = $location.path();
        } else {
            locationService.location = locationService.replaceParamsInUrl(url, params);
        }
    };

    locationService.gotoLast = function () {
        $location.path(locationService.location);
    };

    locationService.replaceParamsInUrl = function (url, params) {
        var resolvedUrl = url;

        for (var k in params) {
            resolvedUrl = resolvedUrl.replace(':' + k, params[k]);
        }

        return resolvedUrl;
    };

    return locationService;
});

as.service('DataService', function () {
    var dataService = {};

    dataService.data = {};

    dataService.set = function (target, data) {
        dataService.data[target] = data;
    };

    dataService.get = function (target) {
        return dataService.data[target];
    };

    return dataService;
});

as.service('FileService', function ($q, $log) {
    var fileService = {};

    var onLoad = function (reader, deferred, scope) {
        return function () {
            scope.$apply(function () {
                deferred.resolve(reader.result);
            });
        };
    };

    var onError = function (reader, deferred, scope) {
        return function () {
            scope.$apply(function () {
                deferred.reject(reader.result);
            });
        };
    };

    var getReader = function (deferred, scope) {
        var reader = new FileReader();
        reader.onload = onLoad(reader, deferred, scope);
        reader.onerror = onError(reader, deferred, scope);

        return reader;
    };

    fileService.readAsDataURL = function (file, scope) {
        var deferred = $q.defer();

        var reader = getReader(deferred, scope);
        reader.readAsDataURL(file);

        return deferred.promise;
    };

    return fileService;
});

as.service('LikeService', function ($http, SETTINGS) {
    var likeService = {};


    likeService.doLike = function (postId) {
        return $http.post('api/like/post/' + postId);
    };

    likeService.doDislike = function (postId) {
        return $http.post('api/dislike/post/' + postId);
    };

    return likeService;
});

as.service('HtmlEncoder', function () {
    var HtmlToken = {
        WORD: 0,
        TAG_OPEN: 1,
        TAG_CLOSE: 2
    };
    var HtmlTokenizeState = {
        DEFAULT: 0,
        TAG_START: 1,
        TAG_END: 2,
        WORD_START: 3,
        WORD_END: 4,
        TAG_CLOSE: 5
    };

    var HtmlLexer = {};

    HtmlLexer.tokenizeState = HtmlTokenizeState.DEFAULT;
    HtmlLexer.value = '';
    HtmlLexer.position = -1;
    HtmlLexer.expression = '';
    HtmlLexer.tokenize = function () {
        HtmlLexer.position = -1;
        HtmlLexer.value = '';
        HtmlLexer.tokenizeState = HtmlTokenizeState.DEFAULT;
        var lexems = [];
        var ch;

        while ((ch = HtmlLexer.getNext()) != -1) {
            var lexem = HtmlLexer.nextLexem(ch);

            if (lexem !== null) {
                lexems.push(lexem);
            }
        }
        if (HtmlLexer.tokenizeState !== HtmlTokenizeState.DEFAULT) {
            lexems.push(HtmlLexer.createToken(HtmlToken.WORD, false));
        }

        return lexems;
    };
    HtmlLexer.getNext = function () {
        HtmlLexer.position += 1;
        return HtmlLexer.position < HtmlLexer.expression.length ?
            HtmlLexer.expression.charAt(HtmlLexer.position) : -1;
    };
    HtmlLexer.nextLexem = function (ch) {
        switch (HtmlLexer.tokenizeState) {
            case HtmlTokenizeState.DEFAULT:
                if (ch == '<') {
                    HtmlLexer.tokenizeState = HtmlTokenizeState.TAG_START;
                    HtmlLexer.position -= 1;
                } else {
                    HtmlLexer.tokenizeState = HtmlTokenizeState.WORD_START;
                    HtmlLexer.position -= 1;
                }
                break;
            case HtmlTokenizeState.TAG_START:
                HtmlLexer.value += ch;

                if (ch == '/') {
                    HtmlLexer.tokenizeState = HtmlTokenizeState.TAG_CLOSE;
                } else if (ch == '>') {
                    HtmlLexer.tokenizeState = HtmlTokenizeState.TAG_END;
                    HtmlLexer.position -= 1;
                }
                break;
            case HtmlTokenizeState.TAG_CLOSE:
                HtmlLexer.value += ch;

                if (ch == '>') {
                    return HtmlLexer.createToken(HtmlToken.TAG_CLOSE, false);
                }
                break;
            case HtmlTokenizeState.TAG_END:
                return HtmlLexer.createToken(HtmlToken.TAG_OPEN, false);
            case HtmlTokenizeState.WORD_START:
                if (ch == '<') {
                    HtmlLexer.tokenizeState = HtmlTokenizeState.WORD_END;
                    HtmlLexer.position -= 1;
                } else {
                    HtmlLexer.value += ch;
                }
                break;
            case HtmlTokenizeState.WORD_END:
                return HtmlLexer.createToken(HtmlToken.WORD, true);
        }

        return null;
    };
    HtmlLexer.createToken = function (token, back) {
        var lexem = {
            token: token,
            value: HtmlLexer.value
        };

        HtmlLexer.value = '';

        HtmlLexer.tokenizeState = HtmlTokenizeState.DEFAULT;
        if (back) {
            HtmlLexer.position -= 1;
        }

        return lexem;
    };

    var HtmlEncoder = {};

    HtmlEncoder.escape = function (str) {
        return str.replace("<", "&lt;").replace(">", "&gt;");
    };

    HtmlEncoder.encode = function (html) {
        if (!html) {
            return '';
        }
        HtmlLexer.expression = html;

        var lexems = HtmlLexer.tokenize();
        var encoded = '';

        for (var i = 0; i < lexems.length; ++i) {
            var lexem = lexems[i];

            switch (lexem.token) {
                case HtmlToken.WORD:
                    encoded += HtmlEncoder.escape(lexem.value);
                    break;
                case HtmlToken.TAG_CLOSE: {
                    if (lexem.value === '</code>') {
                        encoded += lexem.value;
                        encoded += '</pre>';
                    } else {
                        encoded += HtmlEncoder.escape(lexem.value);
                    }
                    break;
                }
                case HtmlToken.TAG_OPEN: {
                    if (lexem.value.startsWith('<code')) {
                        encoded += '<pre>';
                        encoded += lexem.value.substring(0, 5) + ' hljs' + lexem.value.substring(5);
                    } else {
                        encoded += HtmlEncoder.escape(lexem.value);
                    }
                    break;
                }
            }
        }

        return encoded;
    };

    return HtmlEncoder;
});

as.service('AboutMeService', function ($http) {
    var service = {};

    service.getAboutMe = function (callback) {
        return $http.get('/api/aboutme').then(callback);
    };

    service.update = function (post, placeOfResidence, callback) {
        var json = {
            post: post,
            placeOfResidence: placeOfResidence
        };

        return $http.patch('/api/aboutme/update', json).then(callback);
    };

    return service;
});

as.service('SkillService', function ($http) {
    var service = {};

    service.create = function (name, callback) {
        var json = {
            name: name
        };

        return $http.post('/api/skill/create', json).then(callback);
    };

    service.update = function (id, name, callback) {
        var json = {
            name: name
        };

        $http.patch('/api/skill/update/' + id, json).then(callback);
    };

    service.delete = function (id, callback) {
        $http.delete('/api/skill/delete/' + id).then(callback);
    };

    return service;
});

as.service('CategoryService', function ($http) {
    var service = {};

    service.getPage = function (page, size, callback) {
        $http.get('api/category?page=' + (page - 1) + '&size=' + size).then(callback);
    };

    service.create = function (name, description, index, callback) {
        var json = {
            name: name,
            description: description,
            index: index
        };

        $http.post('api/category/create', json).then(callback);
    };

    service.getById = function (id, callback) {
        $http.get('api/category/' + id).then(callback);
    };

    service.update = function (id, name, description, index, callback) {
        var json = {
            name: name,
            description: description,
            index: index
        };

        $http.patch('api/category/update/' + id, json).then(callback);
    };

    service.delete = function (id, callback) {
        $http.delete('api/category/delete/' + id).then(callback);
    };

    return service;
});

as.service('PostService', function ($http) {
    var service = {};

    service.getPage = function (categoryId, page, size, callback) {
        $http.get('api/post/' + categoryId + '/posts?page=' + (page - 1) + '&size=' + size).then(callback);
    };

    service.create = function (categoryId, title, content, callback) {
        var json = {
            title: title,
            content: content
        };

        $http.post('api/post/' + categoryId + '/create', json).then(callback);
    };

    service.getById = function (id, callback) {
        $http.get('api/post/' + id).then(callback);
    };

    service.update = function (id, title, content, callback) {
        var json = {
            title: title,
            content: content
        };

        $http.patch('api/post/update/' + id, json).then(callback);
    };

    service.delete = function (id, callback) {
        $http.delete('api/post/delete/' + id).then(callback);
    };

    return service;
});

as.service('CommentService', function ($http) {
    var service = {};

    service.getPage = function (postId, page, size, callback) {
        $http.get('api/comment/' + postId + '/comments?page=' + (page - 1) + '&size=' + size).then(callback);
    };

    service.create = function (postId, content, callback) {
        var json = {
            content: content
        };

        $http.post('api/comment/' + postId + '/create', json).then(callback);
    };

    service.update = function (postId, id, content, callback) {
        var json = {
            content: content
        };

        $http.patch('api/comment/' + postId + '/update/' + id, json).then(callback);
    };

    service.delete = function (postId, id, callback) {
        $http.delete('api/comment/' + postId + '/delete/' + id).then(callback);
    };

    return service;
});

as.service('ProjectService', function ($http) {
    var service = {};

    service.getAll = function (callback) {
        $http.get('api/project').then(callback);
    };

    service.create = function (name, description, projectLink, technologies, features, callback) {
        var json = {
            name: name,
            description: description,
            projectLink: projectLink,
            technologies: technologies,
            features: features
        };

        $http.post('api/project/create', json).then(callback);
    };

    service.update = function (id, name, description, projectLink, technologies, features, callback) {
        var json = {
            name: name,
            description: description,
            projectLink: projectLink,
            technologies: technologies,
            features: features
        };

        $http.patch('api/project/update/' + id, json).then(callback);
    };

    return service;
});

as.service('UserService', function ($http) {
    var service = {};

    service.getPage = function (page, size, callback) {
        $http.get('api/users?page=' + (page - 1) + '&size=' + size).then(callback);
    };

    return service;
});

as.service('WebSocket', function ($rootScope, SETTINGS, WEB_SOCKET_EVENTS) {
    var service = {}, socket = {
        client: null,
        stomp: null
    };

    var startListener = function () {
        socket.stomp.subscribe(SETTINGS.likeTopic, function (data) {
            $rootScope.$broadcast(WEB_SOCKET_EVENTS.likeEvent, angular.fromJson(data.body));
        });

        socket.stomp.subscribe(SETTINGS.commentTopic, function (data) {
            $rootScope.$broadcast(WEB_SOCKET_EVENTS.commentEvent, angular.fromJson(data.body));
        });
    };

    service.initialize = function () {
        socket.client = new SockJS(SETTINGS.webSocketUrl);
        socket.stomp = Stomp.over(socket.client);
        socket.stomp.connect({}, startListener);
    };

    return service;
});

as.service('TruncateService', function () {
    var service = {};

    service.truncate = function (value, length) {
        if (value.length > length)
            return value.substring(0, length) + '...';
        else
            return value;
    };

    return service;
});