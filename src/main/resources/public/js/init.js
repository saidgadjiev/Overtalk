var as = angular.module('OverTalkApp', ['ngRoute', 'OverTalkApp.controllers']);

as.config(function ($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'html/login.html',
            controller: 'LoginController'
        })
        .when('/login', {
            templateUrl: 'html/login.html',
            controller: 'LoginController'
        })
        .when('/posts', {
            templateUrl: 'html/posts/posts.html',
            controller: 'PostsController'
        })
        .when('/posts/new', {
            templateUrl: 'html/posts/new.html',
            controller: 'NewPostController'
        });
});
